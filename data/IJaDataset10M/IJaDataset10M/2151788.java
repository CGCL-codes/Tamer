package com.google.hackathon.reviewgetter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.WebView;

public class WebActivity extends Activity {

    private final int FP = ViewGroup.LayoutParams.FILL_PARENT;

    private WebView webview;

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        webview = new WebView(this);
        setContentView(webview, new ViewGroup.LayoutParams(FP, FP));
        Intent intent = this.getIntent();
        Bundle icicle2 = intent.getExtras();
        String url = icicle2.getString("DetailUrl");
        webview.loadUrl(url);
        webview.requestFocus();
    }
}
