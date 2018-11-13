package contecttest.example.com.clock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class Draw extends View {

    private int mWidth, mHeight;
    float clockRadius;

    Paint redPaint = new Paint();
    Paint mPaint = new Paint();
    Paint textPaint = new Paint();
    Paint hourHandPaint = new Paint();
    Paint minuteHandPaint = new Paint();

    RefreshHandler refreshHandler = new RefreshHandler();
    Time time = new Time();
    Timer timer = new Timer();
    TimerTask task;


    public Draw(Context context) {
        super(context);
    }

    public Draw(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setUpTimer();
        timer.schedule(task,0, 1000);
        paintInitialize();
    }

    public Draw(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        clockRadius = mWidth * 0.45f;
        canvas.translate(mWidth/2, mHeight/2);

        mPaint.setStrokeWidth(10);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.rgb(100,100,100));
        canvas.drawCircle(0, 0, clockRadius, mPaint);

        for (int i = 0; i < 360; i+=6){
            canvas.drawLine(0, -clockRadius, 0, clockSignLength(i), mPaint);
            canvas.rotate(6);
        }

        time.setToNow();
        float hourBase = (time.hour % 12) * 30;
        float hourAnglesAdjust = (time.minute / 60f) * 30;
        float hourAngles = hourBase + hourAnglesAdjust;
        float minuteAngles = time.minute / 60f * 360;
        float secondAngles = time.second * 6;
        canvas.rotate(hourAngles);
        canvas.drawLine(0, -clockRadius * 0.55f, 0, 0, hourHandPaint);
        canvas.rotate(-hourAngles);
        canvas.rotate(minuteAngles);
        canvas.drawLine(0, -clockRadius * 0.8f, 0, 0, minuteHandPaint);
        canvas.rotate(-minuteAngles);
        canvas.rotate(secondAngles);
        canvas.drawLine(0, -clockRadius * 0.8f, 0, 0, redPaint);

        mPaint.setColor(Color.rgb(50,50,50));
        mPaint.setStrokeWidth(30);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(0,0,30, mPaint);

        canvas.rotate(-secondAngles);
        canvas.drawText(
                (time.hour < 10 ? "0"+time.hour : time.hour)  + ":" +
                        (time.minute < 10 ? "0"+time.minute : time.minute) + ":" +
                        (time.second < 10 ? "0"+time.second : time.second),
                -mWidth * 0.26f, mHeight * 0.4f, textPaint);
    }

    private void paintInitialize(){
        redPaint.setColor(Color.rgb(255,0,0));
        redPaint.setStyle(Paint.Style.FILL);
        redPaint.setStrokeWidth(10);

        textPaint.setColor(Color.BLUE);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(200);

        hourHandPaint.setColor(Color.rgb(50, 50, 50));
        hourHandPaint.setStyle(Paint.Style.FILL);
        hourHandPaint.setStrokeWidth(50);

        minuteHandPaint.setColor(Color.rgb(100, 100, 100));
        minuteHandPaint.setStyle(Paint.Style.FILL);
        minuteHandPaint.setStrokeWidth(30);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        mHeight = h;
        mWidth = w;
    }

    private float clockSignLength(int index){

        float length;
        if (index == 0 || index == 90 || index == 180 || index == 270) {
            length =  -clockRadius * 0.85f;
        }else if(index == 30 || index == 60 || index == 120 || index == 150 || index == 210 || index== 240 || index == 300 || index == 330) {
            length = -clockRadius * 0.9f;
        }else {
            length = -clockRadius * 0.95f;
        }
        return length;
    }

    private void setUpTimer(){
        task = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                refreshHandler.sendMessage(message);
            }
        };
    }

    class RefreshHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case 1:
                    invalidate();
                    break;
            }
        }
    }
}
