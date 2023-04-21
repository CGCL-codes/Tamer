package com.example.android.apis.app;

import com.example.android.apis.R;
import android.app.Activity;
import android.os.Bundle;

/**
 * Example of removing yourself from the history stack after forwarding to
 * another activity.
 */
public class ForwardTarget extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forward_target);
    }
}
