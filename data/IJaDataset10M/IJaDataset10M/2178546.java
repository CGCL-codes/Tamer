package com.eryos.android.cigarettecounter.ui;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.widget.Button;
import com.eryos.android.cigarettecounter.R;

public class Credits extends Dialog {

    private static final String LOG_TAG = "ChartPopup";

    private Button closeButton;

    public Credits(Context _context) {
        super(_context);
        Log.w(LOG_TAG, getClass().getSimpleName() + " : new CreditsPopup()");
        this.setContentView(R.layout.credits_dialog);
        this.setTitle("Credits");
        WebView content = (WebView) findViewById(R.id.content_label);
        content.loadUrl("file:///android_asset/credits.html");
        closeButton = (Button) findViewById(R.id.popup_share_button);
        closeButton.setEnabled(true);
        this.show();
        closeButton.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_DOWN) {
                    dismiss();
                    return false;
                }
                return true;
            }
        });
    }
}
