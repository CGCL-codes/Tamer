package com.retain;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebView.PictureListener;
import android.webkit.WebViewClient;
import com.retain.dialog.DeleteDialog;
import com.retain.dialog.FindDialog;
import com.retain.dialog.RenameDialog;

public class WebViewActivity extends BrowserActivity {

    private static final String LOG_TAG = "WebViewActivity";

    public static final String SHOW_TITLE = "showTitle";

    private WebView mWebView;

    private FindDialog mFindDialog;

    private PageInfo mPageInfo;

    private String mHostPart;

    private String mCameraAction;

    private boolean mShowTitle = false;

    private boolean mRememberPos = false;

    private WebDbAdapter mDbAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        long rowId = i.getLongExtra(WebDbAdapter.KEY_ROWID, -1);
        Log.d(LOG_TAG, "Requesting rowId=" + rowId);
        mDbAdapter = new WebDbAdapter(this);
        mDbAdapter.open();
        mPageInfo = mDbAdapter.fetchEntry(rowId);
        if (mPageInfo == null) {
            Log.e(LOG_TAG, "Missing row id=" + rowId);
            AppUtils.showToastLong(this, "Page is no longer available");
            startActivity(new Intent(this, RetainActivity.class));
            finish();
            return;
        }
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String uaString = sp.getString(this.getString(R.string.pref_key_user_agent), this.getString(R.string.default_user_agent));
        int bgColor = SettingsManager.BACKGROUND_COLOR_DEFAULT;
        boolean useDefaultColors = sp.getBoolean(this.getString(R.string.pref_key_default_colors), true);
        if (!useDefaultColors) bgColor = sp.getInt(this.getString(R.string.pref_key_bgcolor), SettingsManager.BACKGROUND_COLOR_DEFAULT);
        mCameraAction = sp.getString(this.getString(R.string.pref_key_camera_action), this.getString(R.string.default_camera_button));
        mShowTitle = sp.getBoolean(this.getString(R.string.pref_key_show_title), true);
        mRememberPos = sp.getBoolean(this.getString(R.string.pref_key_remember_pos), false);
        if (!mShowTitle) requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.web_view);
        mWebView = (WebView) findViewById(com.retain.R.id.webview2);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUserAgentString(uaString);
        webSettings.setUseWideViewPort(true);
        webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        try {
            URI uri = new URI(mPageInfo.getUrl());
            mHostPart = uri.getScheme() + "://" + uri.getHost();
        } catch (URISyntaxException urise) {
            Log.d(LOG_TAG, "exception " + urise.getMessage());
        }
        mWebView.setBackgroundColor(bgColor);
        mWebView.setNetworkAvailable(true);
        mWebView.setWebChromeClient(new RetainWebChromeClient());
        setRequestedOrientation(mPageInfo.getViewOption());
        if (mShowTitle) setTitle(mPageInfo.getTitle());
        Log.d(LOG_TAG, "Loading " + mPageInfo.getFilePath());
        File file = new File(mPageInfo.getFilePath());
        if (!file.exists() || !file.canRead()) {
            String res = AppUtils.fromRawResourceFile(R.raw.retain_404, this);
            mWebView.setBackgroundColor(-11119018);
            mWebView.loadData(res, "text/html", "utf-8");
        } else {
            mWebView.loadUrl("file://" + mPageInfo.getFilePath());
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_CAMERA) {
            Log.d(LOG_TAG, "Camera pressed: " + keyCode + "/" + mCameraAction);
            if (mCameraAction.equals("select")) return beginSelectText(); else if (mCameraAction.equals("find")) return beginFindText(); else if (mCameraAction.equals("rename")) {
                renameTitle();
                return true;
            }
        } else if ((event.getFlags() & KeyEvent.FLAG_LONG_PRESS) == KeyEvent.FLAG_LONG_PRESS) return beginSelectText();
        return super.onKeyDown(keyCode, event);
    }

    private void persistScroll() {
        if (mDbAdapter.isOpen() && mWebView != null) {
            float contentHeight = ((float) mWebView.getContentHeight()) * mWebView.getScale();
            float scrollY = ((float) mWebView.getScrollY());
            float perc = scrollY / contentHeight;
            Log.d(LOG_TAG, "=" + Float.toString(mWebView.getScrollY()) + " contentHeight=" + contentHeight + " scale=" + Float.toString(mWebView.getScale()) + " perc=" + Float.toString(perc));
            mPageInfo.setScrollBy(perc);
        }
    }

    @Override
    public void finish() {
        persistScroll();
        if (mDbAdapter != null) mDbAdapter.close();
        super.finish();
    }

    @Override
    protected void onPause() {
        persistScroll();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.web_view_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.itemOpenUrl:
                final Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse(mPageInfo.getUrl()));
                startActivity(viewIntent);
                break;
            case R.id.itemShare:
                final Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, mPageInfo.getUrl());
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mPageInfo.getTitle());
                startActivity(Intent.createChooser(shareIntent, mPageInfo.getTitle()));
                break;
            case R.id.itemSelectText:
                beginSelectText();
                break;
            case R.id.itemDelete:
                {
                    DeleteDialog dialog = new DeleteDialog(this, mPageInfo, mDbAdapter, new DeleteHandlerInterface.OnDeleteItemListener() {

                        @Override
                        public void onDeleteItem(long rowId) {
                            finish();
                        }
                    });
                    dialog.prompt();
                }
                break;
            case R.id.itemRefresh:
                {
                    final Intent i = new Intent(this, DownloaderActivity.class);
                    i.putExtra(Intent.EXTRA_TEXT, mPageInfo.getUrl());
                    i.putExtra(WebDbAdapter.KEY_ROWID, mPageInfo.getRowId());
                    startActivity(i);
                    finish();
                }
                break;
            case R.id.itemCopyUrl:
                {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    clipboard.setText(mPageInfo.getUrl());
                    AppUtils.showToastShort(this, getString(R.string.copy_url_msg));
                }
                break;
            case R.id.itemRename:
                {
                    renameTitle();
                    break;
                }
            case R.id.itemLandscape:
                {
                    mPageInfo.setViewOption(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    setRequestedOrientation(mPageInfo.getViewOption());
                    break;
                }
            case R.id.itemPortrait:
                {
                    mPageInfo.setViewOption(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    setRequestedOrientation(mPageInfo.getViewOption());
                    break;
                }
            case R.id.itemSystemDefined:
                {
                    mPageInfo.setViewOption(ActivityInfo.SCREEN_ORIENTATION_USER);
                    setRequestedOrientation(mPageInfo.getViewOption());
                    break;
                }
            case R.id.itemFindText:
                {
                    beginFindText();
                    break;
                }
        }
        return true;
    }

    private void renameTitle() {
        RenameDialog rd = new RenameDialog(this, mPageInfo, mDbAdapter, new RenameHandlerInterface.OnRenameItemListener() {

            @Override
            public void onRenameItem(String title) {
                if (mShowTitle) setTitle(title);
                AppUtils.showToastShort(WebViewActivity.this, WebViewActivity.this.getString(R.string.renamedto) + " " + title);
            }
        });
        rd.show();
    }

    private boolean beginSelectText() {
        KeyEvent shiftPressEvent = new KeyEvent(0, 0, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_SHIFT_LEFT, 0, 0);
        shiftPressEvent.dispatch(mWebView);
        return true;
    }

    private boolean beginFindText() {
        if (null == mFindDialog) {
            mFindDialog = new FindDialog(this);
        }
        mFindDialog.setWebView(mWebView);
        mFindDialog.show();
        return true;
    }

    final class RetainWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int progress) {
            WebViewActivity.this.setProgress(progress * 100);
        }
    }

    final class RetainWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            WebViewActivity.this.setProgress(10000);
            int contentHeight = mWebView.getContentHeight();
            int scroll = (int) (mPageInfo.getScroll() * contentHeight);
            if (!mRememberPos || scroll < 1) return;
            mWebView.scrollTo(0, 0);
            mWebView.scrollBy(0, scroll);
            Log.d(LOG_TAG, "Finished Loading percent " + mPageInfo.getScroll() + ", scroll=" + scroll + ", contentHeight=" + contentHeight);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(LOG_TAG, "shouldOverrideUrlLoading called: " + url);
            String useUrl = url;
            if (url.startsWith("file://")) useUrl = mHostPart + url.substring(7); else if (url.startsWith("/")) useUrl = mHostPart + url; else if (url.startsWith("?")) useUrl = mPageInfo.getUrl() + url;
            Log.d(LOG_TAG, "Loading url: " + useUrl);
            try {
                Uri uri = Uri.parse(useUrl);
                final Intent viewIntent = new Intent("android.intent.action.VIEW", uri);
                startActivity(viewIntent);
            } catch (Exception e) {
                Log.e(LOG_TAG, "Exception: " + e);
                AppUtils.showToastLong(WebViewActivity.this, "Unable to load " + useUrl);
            }
            return true;
        }
    }
}
