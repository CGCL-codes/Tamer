package org.hld.mht;

import java.io.File;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class WebViewActivity extends Activity {

    WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.web);
        webView = (WebView) findViewById(R.id.WebView);
        Intent intent = getIntent();
        if (intent == null) {
            showMessage("啥都没啊……");
        } else {
            String path = intent.getExtras().getString("path");
            if (path == null) {
                showMessage("不知道要去哪找文件……");
            } else if (!new File(path).isFile()) {
                showMessage(path + "文件无效");
            } else {
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebChromeClient(new WebChromeClient() {

                    @Override
                    public void onProgressChanged(WebView view, int newProgress) {
                        WebViewActivity.this.setProgress(newProgress * 100);
                    }

                    @Override
                    public void onReceivedTitle(WebView view, String title) {
                        WebViewActivity.this.setTitle(title);
                    }
                });
                webView.loadUrl("file://" + Uri.encode(path, "/\\"));
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            android.os.Process.killProcess(android.os.Process.myPid());
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showMessage(String msg) {
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        webView.loadData(Uri.encode(msg, "UTF-8"), "text/plain", "UTF-8");
    }
}
