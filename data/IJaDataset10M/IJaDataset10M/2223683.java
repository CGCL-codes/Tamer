package com.manning.aip.iweb;

import static android.webkit.ConsoleMessage.MessageLevel.ERROR;
import java.io.File;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class InterWebActivity extends Activity {

    private static final int REQUEST_PIC = 5;

    private static final int REQUEST_CONTACT = 4;

    private static final String LOG_TAG = "InterWebActivity";

    private WebView webView;

    private InterWebInterface webInterface;

    private static int onCreateCount = 0;

    private int onResumeCount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        webView = (WebView) findViewById(R.id.web);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webInterface = new InterWebInterface();
        webView.addJavascriptInterface(webInterface, "android");
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Log.d(LOG_TAG, String.format("WebView JsAlert message = %s", url, message));
                return false;
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                StringBuilder msg = new StringBuilder(consoleMessage.messageLevel().name()).append('\t').append(consoleMessage.message()).append('\t').append(consoleMessage.sourceId()).append(" (").append(consoleMessage.lineNumber()).append(")\n");
                if (consoleMessage.messageLevel() == ERROR) {
                    Log.e(LOG_TAG, msg.toString());
                } else {
                    Log.d(LOG_TAG, msg.toString());
                }
                return true;
            }
        });
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d(LOG_TAG, "Loading url=" + url);
                return false;
            }
        });
        webView.loadUrl("file:///android_asset/interweb.html");
        onCreateCount++;
    }

    @Override
    protected void onResume() {
        super.onResume();
        onResumeCount++;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CONTACT && resultCode == RESULT_OK) {
            webInterface.executeContactCallback(data.getData());
        }
        if (requestCode == REQUEST_PIC && resultCode == RESULT_OK) {
            webInterface.executePicCallback(data.getData());
        }
    }

    private String getContactDisplayName(Uri contactUri) {
        String[] projection = { ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME };
        Cursor cursor = managedQuery(contactUri, projection, null, null, null);
        if (cursor.moveToNext()) {
            return cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }
        return "Couldn't find it";
    }

    private String getPictureData(Uri pictureUri) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = managedQuery(pictureUri, projection, null, null, null);
        if (cursor.moveToNext()) {
            return cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
        }
        return "";
    }

    class InterWebInterface {

        String callback;

        public String getCreateCount() {
            return String.valueOf(onCreateCount);
        }

        public String getResumeCount() {
            return String.valueOf(onResumeCount);
        }

        public String getUserName() {
            AccountManager mgr = AccountManager.get(InterWebActivity.this);
            Account gAccount = mgr.getAccountsByType("com.google")[0];
            return gAccount.name;
        }

        public void selectContact(String callback) {
            this.callback = callback;
            Intent intentContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intentContact, REQUEST_CONTACT);
        }

        public void selectPicture(String callback) {
            this.callback = callback;
            Intent intentPicture = new Intent(Intent.ACTION_GET_CONTENT);
            intentPicture.setType("image/*");
            startActivityForResult(intentPicture, REQUEST_PIC);
        }

        protected void executeContactCallback(Uri contact) {
            String name = getContactDisplayName(contact);
            webView.loadUrl(String.format("javascript:contactCallback('%s')", name));
        }

        protected void executePicCallback(Uri picture) {
            String filePath = getPictureData(picture);
            File f = new File(filePath);
            String uri = Uri.fromFile(f).toString();
            webView.loadUrl(String.format("javascript:pictureCallback('%s')", uri));
        }
    }
}
