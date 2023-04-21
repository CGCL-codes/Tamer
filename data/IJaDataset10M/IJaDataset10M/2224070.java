package org.frankkie.parcdroidprj;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Config;
import android.util.Log;
import android.view.View;

public class Compass extends Activity {

    private static final String TAG = "Compass";

    private SensorManager mSensorManager;

    private SampleView mView;

    private float[] mValues;

    private final SensorEventListener mListener = new SensorEventListener() {

        public void onSensorChanged(int sensor, float[] values) {
            if (Config.LOGD) {
            }
            mValues = values;
            if (mView != null) {
                mView.invalidate();
            }
        }

        public void onAccuracyChanged(int sensor, int accuracy) {
        }

        public void onSensorChanged(SensorEvent arg0) {
            if (Config.LOGD) {
            }
            mValues = arg0.values;
            if (mView != null) {
                mView.invalidate();
            }
        }

        public void onAccuracyChanged(Sensor arg0, int arg1) {
        }
    };

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mView = new SampleView(this);
        setContentView(mView);
    }

    @Override
    protected void onResume() {
        if (Config.LOGD) {
            Log.d(TAG, "onResume");
        }
        super.onResume();
        mSensorManager.registerListener(mListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStop() {
        if (Config.LOGD) {
            Log.d(TAG, "onStop");
        }
        mSensorManager.unregisterListener(mListener);
        super.onStop();
    }

    private class SampleView extends View {

        private Paint mPaint = new Paint();

        private Path mPath = new Path();

        private boolean mAnimate;

        private long mNextTime;

        public SampleView(Context context) {
            super(context);
            mPath.moveTo(0, -50);
            mPath.lineTo(-20, 60);
            mPath.lineTo(0, 50);
            mPath.lineTo(20, 60);
            mPath.close();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            Paint paint = mPaint;
            canvas.drawColor(Color.WHITE);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            int w = canvas.getWidth();
            int h = canvas.getHeight();
            int cx = w / 2;
            int cy = h / 2;
            canvas.translate(cx, cy);
            if (mValues != null) {
                canvas.rotate(-mValues[0]);
            }
            canvas.drawPath(mPath, mPaint);
        }

        @Override
        protected void onAttachedToWindow() {
            mAnimate = true;
            super.onAttachedToWindow();
        }

        @Override
        protected void onDetachedFromWindow() {
            mAnimate = false;
            super.onDetachedFromWindow();
        }
    }
}
