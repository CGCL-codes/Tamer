package org.jazzteam.solit.activity;

import org.jazzteam.solit.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;

public class MapActivity extends Activity {

    private static final FrameLayout.LayoutParams ZOOM_PARAMS = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        WebView webView = (WebView) findViewById(R.id.webview_map);
        webView.loadUrl("file:///android_asset/city.html");
        FrameLayout mContentView = (FrameLayout) getWindow().getDecorView().findViewById(android.R.id.content);
        final View zoom = webView.getZoomControls();
        mContentView.addView(zoom, ZOOM_PARAMS);
        zoom.setVisibility(View.VISIBLE);
    }
}
