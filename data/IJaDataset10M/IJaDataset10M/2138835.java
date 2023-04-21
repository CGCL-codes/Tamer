package com.android.wallpaper.fall;

import android.app.Activity;
import android.os.Bundle;

public class Fall extends Activity {

    private FallView mView;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        mView = new FallView(this);
        setContentView(mView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mView.onPause();
        Runtime.getRuntime().exit(0);
    }
}
