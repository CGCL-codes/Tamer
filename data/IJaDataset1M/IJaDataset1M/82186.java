package android.com.abb;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import java.lang.Math;
import java.lang.System;
import java.lang.Thread;
import junit.framework.Assert;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    class GameThread extends Thread {

        public GameThread(Context context, Handler handler, SurfaceHolder surface_holder) {
            mContext = context;
            mHandler = handler;
            mPaused = false;
            mRunning = true;
            mSurfaceHolder = surface_holder;
        }

        @Override
        public void run() {
            Log.d("GameThread::run", "Starting game thread...");
            synchronized (this) {
                while (mGame == null && mRunning) {
                    try {
                        wait();
                    } catch (java.lang.InterruptedException ex) {
                        continue;
                    }
                }
                Assert.assertEquals("GameView thread must only be run once.", mGraphics, null);
                mGraphics = new Graphics();
                mGraphics.initialize(mSurfaceHolder);
                mGame.initializeGraphics(mGraphics);
            }
            final float kMaxFrameRate = 30.0f;
            final float kMinFrameRate = 6.0f;
            final float kMinTimeStep = 1.0f / kMaxFrameRate;
            final float kMaxTimeStep = 1.0f / kMinFrameRate;
            long time = System.nanoTime();
            while (mRunning) {
                synchronized (this) {
                    while (mPaused && mRunning) {
                        try {
                            wait();
                        } catch (java.lang.InterruptedException ex) {
                            continue;
                        }
                    }
                }
                long current_time = System.nanoTime();
                float time_step = (float) (current_time - time) * 1.0e-9f;
                time = current_time;
                if (false && time_step < kMinTimeStep) {
                    float remaining_time = kMinTimeStep - time_step;
                    time_step = kMinTimeStep;
                    try {
                        long sleep_milliseconds = (long) (remaining_time * 1.0e3f);
                        remaining_time -= sleep_milliseconds * 1.0e-3f;
                        int sleep_nanoseconds = (int) (remaining_time * 1.0e9f);
                        Thread.sleep(sleep_milliseconds, sleep_nanoseconds);
                    } catch (InterruptedException ex) {
                    }
                } else {
                }
                time_step = Math.max(time_step, kMinTimeStep);
                time_step = Math.min(time_step, kMaxTimeStep);
                try {
                    synchronized (this) {
                        if (mGraphics.beginFrame()) {
                            mGame.onFrame(mGraphics, time_step);
                        } else {
                            mHandler.sendMessage(mHandler.obtainMessage(kKillMessage));
                        }
                    }
                } finally {
                    mGraphics.endFrame();
                }
                hideLoadingDialog();
                String notification = mGame.getPendingNotification();
                if (notification != null) {
                    mHandler.sendMessage(mHandler.obtainMessage(kNotificationMessage, notification));
                }
            }
            Log.d("GameThread::run", "Freeing graphics resources...");
            mGraphics.destroy();
            Log.d("GameThread::run", "Finished game thread.");
        }

        public synchronized void setGame(Game game) {
            mGame = game;
            notifyAll();
        }

        public synchronized void pause(boolean pause) {
            mPaused = pause;
            notifyAll();
        }

        public synchronized void surfaceChanged(SurfaceHolder surface_holder, int width, int height) {
            if (mGraphics != null) {
                mGraphics.surfaceChanged(surface_holder, width, height);
            }
        }

        public synchronized void halt() {
            mRunning = false;
            notifyAll();
        }

        private Context mContext;

        private Game mGame;

        private Graphics mGraphics;

        private Handler mHandler;

        private boolean mPaused;

        private boolean mRunning;

        private SurfaceHolder mSurfaceHolder;
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        getHolder().addCallback(this);
        getHolder().setType(SurfaceHolder.SURFACE_TYPE_GPU);
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                Log.d("GameView::Handler::handleMessage", "Received message: " + msg.what);
                if (msg.what == kNotificationMessage) {
                    Builder dialog = new AlertDialog.Builder(mContext);
                    dialog.setMessage(((String) msg.obj) + " (Press back to continue.)");
                    dialog.show();
                } else if (msg.what == kKillMessage) {
                    Activity current_activity = (Activity) mContext;
                    current_activity.finish();
                }
            }
        };
    }

    public void setGame(Game game) {
        mGame = game;
        if (mGameThread != null) {
            mGameThread.setGame(game);
        }
    }

    /** Standard override to get key-press events. */
    @Override
    public boolean onKeyDown(int key_code, KeyEvent msg) {
        synchronized (mGame) {
            return mGame.onKeyDown(key_code);
        }
    }

    /** Standard override for key-up. We actually care about these, so we can turn
   * off the engine or stop rotating. */
    @Override
    public boolean onKeyUp(int key_code, KeyEvent msg) {
        synchronized (mGame) {
            return mGame.onKeyUp(key_code);
        }
    }

    /** Standard window-focus override. Notice focus lost so we can pause on focus
   * lost. e.g. user switches to take a call. */
    @Override
    public void onWindowFocusChanged(boolean window_has_focus) {
        super.onWindowFocusChanged(window_has_focus);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            event.recycle();
            return true;
        }
        synchronized (mGame) {
            mGame.onMotionEvent(event);
        }
        event.recycle();
        return true;
    }

    /** Callback invoked when the Surface has been created and is ready to be
   * used. */
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("GameView::surfaceCreated", "Creating new game thread...");
        showLoadingDialog();
        setFocusable(true);
        getHolder().addCallback(this);
        getHolder().setType(SurfaceHolder.SURFACE_TYPE_GPU);
        Assert.assertTrue(mGameThread == null);
        mGameThread = new GameThread(mContext, mHandler, holder);
        mGameThread.setGame(mGame);
        mGameThread.start();
        mGameThreadStarted = true;
    }

    /** Callback invoked when the surface dimensions change. */
    public void surfaceChanged(SurfaceHolder surface_holder, int format, int width, int height) {
        mGameThread.surfaceChanged(surface_holder, width, height);
    }

    /** Callback invoked when the Surface has been destroyed and must no longer be
   * touched. */
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        mGameThread.halt();
        while (retry) {
            try {
                mGameThread.join();
                mGameThread = null;
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    private synchronized void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = ProgressDialog.show(mContext, null, "Loading...", true, false);
            mLoadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }
    }

    private synchronized void hideLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    private Context mContext;

    private Game mGame;

    private GameThread mGameThread;

    private boolean mGameThreadStarted;

    private Handler mHandler;

    private ProgressDialog mLoadingDialog;

    private boolean mProfiling;

    private static final int kKillMessage = 696;

    private static final int kNotificationMessage = 666;

    private static final int kProfileKey = KeyEvent.KEYCODE_T;

    private static final String kProfilePath = "abb.trace";
}
