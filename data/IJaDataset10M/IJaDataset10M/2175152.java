package com.android.providers.media;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.media.IMediaScannerListener;
import android.media.IMediaScannerService;
import android.media.MediaScanner;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.Process;
import android.os.SystemProperties;
import android.provider.MediaStore;
import android.util.Config;
import android.util.Log;
import java.io.File;
import java.util.Locale;

public class MediaScannerService extends Service implements Runnable {

    private static final String TAG = "MediaScannerService";

    private volatile Looper mServiceLooper;

    private volatile ServiceHandler mServiceHandler;

    private PowerManager.WakeLock mWakeLock;

    private void openDatabase(String volumeName) {
        try {
            ContentValues values = new ContentValues();
            values.put("name", volumeName);
            getContentResolver().insert(Uri.parse("content://media/"), values);
        } catch (IllegalArgumentException ex) {
            Log.w(TAG, "failed to open media database");
        }
    }

    private void closeDatabase(String volumeName) {
        try {
            getContentResolver().delete(Uri.parse("content://media/" + volumeName), null, null);
        } catch (Exception e) {
            Log.w(TAG, "failed to close media database " + volumeName + " exception: " + e);
        }
    }

    private MediaScanner createMediaScanner() {
        MediaScanner scanner = new MediaScanner(this);
        Locale locale = getResources().getConfiguration().locale;
        if (locale != null) {
            String language = locale.getLanguage();
            String country = locale.getCountry();
            String localeString = null;
            if (language != null) {
                if (country != null) {
                    scanner.setLocale(language + "_" + country);
                } else {
                    scanner.setLocale(language);
                }
            }
        }
        return scanner;
    }

    private void scan(String[] directories, String volumeName) {
        mWakeLock.acquire();
        ContentValues values = new ContentValues();
        values.put(MediaStore.MEDIA_SCANNER_VOLUME, volumeName);
        Uri scanUri = getContentResolver().insert(MediaStore.getMediaScannerUri(), values);
        Uri uri = Uri.parse("file://" + directories[0]);
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_STARTED, uri));
        try {
            if (volumeName.equals(MediaProvider.EXTERNAL_VOLUME)) {
                openDatabase(volumeName);
            }
            MediaScanner scanner = createMediaScanner();
            scanner.scanDirectories(directories, volumeName);
        } catch (Exception e) {
            Log.e(TAG, "exception in MediaScanner.scan()", e);
        }
        getContentResolver().delete(scanUri, null, null);
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_FINISHED, uri));
        mWakeLock.release();
    }

    @Override
    public void onCreate() {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        Thread thr = new Thread(null, this, "MediaScannerService");
        thr.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        while (mServiceHandler == null) {
            synchronized (this) {
                try {
                    wait(100);
                } catch (InterruptedException e) {
                }
            }
        }
        if (intent == null) {
            Log.e(TAG, "Intent is null in onStartCommand: ", new NullPointerException());
            return Service.START_NOT_STICKY;
        }
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        msg.obj = intent.getExtras();
        mServiceHandler.sendMessage(msg);
        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        while (mServiceLooper == null) {
            synchronized (this) {
                try {
                    wait(100);
                } catch (InterruptedException e) {
                }
            }
        }
        mServiceLooper.quit();
    }

    public void run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND + Process.THREAD_PRIORITY_LESS_FAVORABLE);
        Looper.prepare();
        mServiceLooper = Looper.myLooper();
        mServiceHandler = new ServiceHandler();
        Looper.loop();
    }

    private Uri scanFile(String path, String mimeType) {
        String volumeName = MediaProvider.INTERNAL_VOLUME;
        String externalStoragePath = Environment.getExternalStorageDirectory().getPath();
        if (path.startsWith(externalStoragePath)) {
            volumeName = MediaProvider.EXTERNAL_VOLUME;
            openDatabase(volumeName);
        }
        MediaScanner scanner = createMediaScanner();
        return scanner.scanSingleFile(path, volumeName, mimeType);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final IMediaScannerService.Stub mBinder = new IMediaScannerService.Stub() {

        public void requestScanFile(String path, String mimeType, IMediaScannerListener listener) {
            if (Config.LOGD) {
                Log.d(TAG, "IMediaScannerService.scanFile: " + path + " mimeType: " + mimeType);
            }
            Bundle args = new Bundle();
            args.putString("filepath", path);
            args.putString("mimetype", mimeType);
            if (listener != null) {
                args.putIBinder("listener", listener.asBinder());
            }
            startService(new Intent(MediaScannerService.this, MediaScannerService.class).putExtras(args));
        }

        public void scanFile(String path, String mimeType) {
            requestScanFile(path, mimeType, null);
        }
    };

    private final class ServiceHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            Bundle arguments = (Bundle) msg.obj;
            String filePath = arguments.getString("filepath");
            try {
                if (filePath != null) {
                    IBinder binder = arguments.getIBinder("listener");
                    IMediaScannerListener listener = (binder == null ? null : IMediaScannerListener.Stub.asInterface(binder));
                    Uri uri = scanFile(filePath, arguments.getString("mimetype"));
                    if (listener != null) {
                        listener.scanCompleted(filePath, uri);
                    }
                } else {
                    String volume = arguments.getString("volume");
                    String[] directories = null;
                    if (MediaProvider.INTERNAL_VOLUME.equals(volume)) {
                        directories = new String[] { Environment.getRootDirectory() + "/media" };
                    } else if (MediaProvider.EXTERNAL_VOLUME.equals(volume)) {
                        directories = new String[] { Environment.getExternalStorageDirectory().getPath() };
                    }
                    if (directories != null) {
                        if (Config.LOGD) Log.d(TAG, "start scanning volume " + volume);
                        scan(directories, volume);
                        if (Config.LOGD) Log.d(TAG, "done scanning volume " + volume);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception in handleMessage", e);
            }
            stopSelf(msg.arg1);
        }
    }

    ;
}
