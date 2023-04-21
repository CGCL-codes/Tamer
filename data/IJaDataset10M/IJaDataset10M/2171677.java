package org.geometerplus.android.fbreader.network;

import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.app.Service;
import android.content.Intent;
import org.geometerplus.zlibrary.core.network.ZLNetworkException;

public class LibraryInitializationService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        final NetworkView view = NetworkView.Instance();
        if (!view.isInitialized()) {
            stopSelf();
            return;
        }
        final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                if (msg.what > 0 && msg.obj == null) {
                    view.finishBackgroundUpdate();
                }
                stopSelf();
            }
        };
        final Thread thread = new Thread(new Runnable() {

            public void run() {
                int code = 0;
                String error = null;
                try {
                    try {
                        view.runBackgroundUpdate(false);
                    } catch (ZLNetworkException e) {
                        error = e.getMessage();
                    }
                    code = 1;
                } finally {
                    handler.sendMessage(handler.obtainMessage(code, error));
                }
            }
        });
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }
}
