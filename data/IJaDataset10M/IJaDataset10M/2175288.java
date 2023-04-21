package howbuy.android.palmfund.activity.content;

import com.mobclick.android.MobclickAgent;
import howbuy.android.palmfund.R;
import howbuy.android.util.Cons;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FContentWebViewActivity extends Activity implements OnClickListener {

    private static final String TAG = "FContentWebViewActivity";

    private Button leftBtn;

    private TextView textView;

    private WebView webView;

    private String url;

    private String name;

    private LinearLayout pLayout;

    private void initView() {
        leftBtn = (Button) findViewById(R.id.title_left_btn);
        textView = (TextView) findViewById(R.id.title_name);
        webView = (WebView) findViewById(R.id.web);
        pLayout = (LinearLayout) findViewById(R.id.progress_lay);
        url = getIntent().getStringExtra(Cons.Intent_normal);
        name = getIntent().getStringExtra(Cons.Intent_name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.f_content_webview);
        initView();
        MobclickAgent.onError(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pLayout.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
            }
        });
        webView.loadUrl(url);
        textView.setText(name);
        leftBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
