package com.android.frameworktest.view;

import com.android.frameworktest.R;
import android.os.Bundle;
import android.app.Activity;

/**
 * This activity contains Views with various widths and heights. The goal is to exercise the
 * drawing cache when width is null, height is null or both.
 */
public class ZeroSized extends Activity {

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.zero_sized);
    }
}
