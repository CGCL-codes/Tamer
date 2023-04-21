package android.webkit.cts;

import com.android.cts.stub.R;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class WebViewStubActivity extends Activity {

    private WebView mWebView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_layout);
        mWebView = (WebView) findViewById(R.id.web_page);
    }

    public WebView getWebView() {
        return mWebView;
    }
}
