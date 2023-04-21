package com.commonsware.android.SquirrelCam;

import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.content.Context;
import android.net.Uri;

public class MediaScannerNotifier implements MediaScannerConnectionClient {

    private MediaScannerConnection mConnection;

    private String mPath;

    private String mMimeType;

    public MediaScannerNotifier(Context context, String path, String mimeType) {
        mPath = path;
        mMimeType = mimeType;
        mConnection = new MediaScannerConnection(context, this);
        mConnection.connect();
    }

    public void onMediaScannerConnected() {
        mConnection.scanFile(mPath, mMimeType);
    }

    public void onScanCompleted(String path, Uri uri) {
        mConnection.disconnect();
    }
}
