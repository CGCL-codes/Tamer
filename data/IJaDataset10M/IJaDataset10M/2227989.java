package com.beefeng.android.example.ndk;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;

public class Plasma extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new PlasmaView(this));
    }

    static {
        System.loadLibrary("plasma");
    }
}

class PlasmaView extends View {

    private Bitmap mBitmap;

    private long mStartTime;

    private static native void renderPlasma(Bitmap bitmap, long time_ms);

    public PlasmaView(Context context) {
        super(context);
        final int W = 200;
        final int H = 200;
        mBitmap = Bitmap.createBitmap(W, H, Bitmap.Config.RGB_565);
        mStartTime = System.currentTimeMillis();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        renderPlasma(mBitmap, System.currentTimeMillis() - mStartTime);
        canvas.drawBitmap(mBitmap, 0, 0, null);
        invalidate();
    }
}
