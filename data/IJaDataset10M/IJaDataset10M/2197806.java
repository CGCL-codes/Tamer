package org.geometerplus.android.fbreader.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BookDownloaderCallback extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (NetworkView.Instance().isInitialized()) {
            NetworkView.Instance().fireModelChangedAsync();
        }
    }
}
