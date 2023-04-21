package com.ntu.way2fungames.darkdecent;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.TextView;

public class BasicView extends SurfaceView implements Callback {

    protected BasicViewThread thread;

    TextView mStatusText;

    protected SurfaceHolder holder;

    public BasicView(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        holder.addCallback(this);
        thread = new BasicViewThread(holder, context, new Handler() {

            @Override
            public void handleMessage(Message m) {
                mStatusText.setVisibility(m.getData().getInt("viz"));
                mStatusText.setText(m.getData().getString("text"));
            }
        });
        setFocusable(true);
    }

    public BasicView(Context context, AttributeSet attrs, boolean b) {
        super(context, attrs);
    }

    public void setTextView(TextView textView) {
        mStatusText = textView;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        thread.setSurfaceSize(width, height);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        Thread.State bob;
        bob = thread.getState();
        if (thread.getState() == Thread.State.NEW) {
            thread.setState(BasicViewThread.STATE_RUNNING);
            thread.setRunning(true);
            thread.start();
        }
        if (thread.getState() == Thread.State.TERMINATED) {
            thread.setRunning(true);
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (!hasWindowFocus) thread.pause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent msg) {
        return thread.doKeyDown(keyCode, msg);
    }

    /**
	     * Standard override for key-up. We actually care about these, so we can
	     * turn off the engine or stop rotating.
	     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent msg) {
        Log.v("way2fungames", "View keyUp");
        return thread.doKeyUp(keyCode, msg);
    }

    public void onPause() {
        thread.pause();
    }
}
