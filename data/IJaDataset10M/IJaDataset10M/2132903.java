package com.retain;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class DownloaderActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String webPageUrl = intent.getStringExtra(Intent.EXTRA_TEXT).trim();
        long rowId = intent.getLongExtra(WebDbAdapter.KEY_ROWID, 0);
        if (webPageUrl != null) {
            int idx = webPageUrl.indexOf('\n');
            if (idx >= 0) webPageUrl = webPageUrl.substring(idx + 1, webPageUrl.length());
            DownloadHandler.getInstance(this).download(webPageUrl, rowId);
        }
        setResult(RESULT_OK, null);
        finish();
    }
}
