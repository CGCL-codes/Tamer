package com.google.code.drift.utils;

import com.google.code.drift.R;
import com.google.code.drift.api.AppSkyApp;
import android.content.Context;
import android.os.Vibrator;

public class AlertUtils {

    private AudioUtils ring;

    private Vibrator vibrator;

    private AppSkyApp app;

    private Context context;

    private long[] pattern = { 0, 300, 200, 200 };

    public AlertUtils(Context context) {
        this.context = context;
        this.app = (AppSkyApp) context.getApplicationContext();
        initChatRingAndVibrator();
    }

    /**
	 * 播放铃声或震动！
	 */
    public void alert() {
        if (app.isChatRing() && ring != null) {
            ring.play();
        }
        if (app.isChatVibrator() && vibrator != null) {
            vibrator.vibrate(pattern, -1);
        }
    }

    public void release() {
        if (ring != null) {
            ring.release();
            ring = null;
        }
        if (vibrator != null) {
            vibrator.cancel();
            vibrator = null;
        }
        app = null;
        context = null;
    }

    private void initChatRingAndVibrator() {
        ring = new AudioUtils(context, R.raw.ring);
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }
}
