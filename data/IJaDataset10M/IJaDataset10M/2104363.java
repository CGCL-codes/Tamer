package com.android.magicsmoke;

import android.app.Activity;
import android.app.WallpaperManager;
import android.app.WallpaperInfo;
import android.app.Dialog;
import android.service.wallpaper.IWallpaperConnection;
import android.service.wallpaper.IWallpaperService;
import android.service.wallpaper.IWallpaperEngine;
import android.service.wallpaper.WallpaperSettingsActivity;
import android.content.ServiceConnection;
import android.content.Intent;
import android.content.Context;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.RemoteException;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewGroup;
import android.view.Window;
import android.view.LayoutInflater;
import android.util.Log;
import android.widget.TextView;

public class MagicSmokeSelector extends Activity {

    private static final String LOG_TAG = "MagicSmokeSelector";

    private WallpaperManager mWallpaperManager;

    private WallpaperConnection mWallpaperConnection;

    private Intent mWallpaperIntent;

    private SharedPreferences mSharedPref;

    private int mCurrentPreset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selector);
        mWallpaperIntent = new Intent(this, MagicSmoke.class);
        mWallpaperManager = WallpaperManager.getInstance(this);
        mWallpaperConnection = new WallpaperConnection(mWallpaperIntent);
        mSharedPref = getSharedPreferences("magicsmoke", Context.MODE_PRIVATE);
        mCurrentPreset = mSharedPref.getInt("preset", MagicSmokeRS.DEFAULT_PRESET);
        if (mCurrentPreset >= MagicSmokeRS.mPreset.length) {
            mCurrentPreset = 0;
            updatePrefs();
        }
    }

    public void setLiveWallpaper(View v) {
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mWallpaperConnection != null && mWallpaperConnection.mEngine != null) {
            try {
                mWallpaperConnection.mEngine.setVisibility(true);
            } catch (RemoteException e) {
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mWallpaperConnection != null && mWallpaperConnection.mEngine != null) {
            try {
                mWallpaperConnection.mEngine.setVisibility(false);
            } catch (RemoteException e) {
            }
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!mWallpaperConnection.connect()) {
            mWallpaperConnection = null;
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mWallpaperConnection != null) {
            mWallpaperConnection.disconnect();
        }
        mWallpaperConnection = null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mCurrentPreset == 0) {
                    mCurrentPreset = MagicSmokeRS.mPreset.length - 1;
                } else {
                    mCurrentPreset--;
                }
                updatePrefs();
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void updatePrefs() {
        Editor edit = mSharedPref.edit();
        edit.putInt("preset", mCurrentPreset);
        edit.commit();
    }

    class WallpaperConnection extends IWallpaperConnection.Stub implements ServiceConnection {

        final Intent mIntent;

        IWallpaperService mService;

        IWallpaperEngine mEngine;

        boolean mConnected;

        WallpaperConnection(Intent intent) {
            mIntent = intent;
        }

        public boolean connect() {
            synchronized (this) {
                if (!bindService(mIntent, this, Context.BIND_AUTO_CREATE)) {
                    return false;
                }
                mConnected = true;
                return true;
            }
        }

        public void disconnect() {
            synchronized (this) {
                mConnected = false;
                if (mEngine != null) {
                    try {
                        mEngine.destroy();
                    } catch (RemoteException e) {
                    }
                    mEngine = null;
                }
                unbindService(this);
                mService = null;
            }
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            if (mWallpaperConnection == this) {
                mService = IWallpaperService.Stub.asInterface(service);
                try {
                    final View view = findViewById(R.id.backgroundview);
                    final View root = view.getRootView();
                    mService.attach(this, view.getWindowToken(), WindowManager.LayoutParams.TYPE_APPLICATION_MEDIA_OVERLAY, true, root.getWidth(), root.getHeight());
                } catch (RemoteException e) {
                    Log.w(LOG_TAG, "Failed attaching wallpaper; clearing", e);
                }
            }
        }

        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mEngine = null;
            if (mWallpaperConnection == this) {
                Log.w(LOG_TAG, "Wallpaper service gone: " + name);
            }
        }

        public void attachEngine(IWallpaperEngine engine) {
            synchronized (this) {
                if (mConnected) {
                    mEngine = engine;
                    try {
                        engine.setVisibility(true);
                    } catch (RemoteException e) {
                    }
                } else {
                    try {
                        engine.destroy();
                    } catch (RemoteException e) {
                    }
                }
            }
        }

        public ParcelFileDescriptor setWallpaper(String name) {
            return null;
        }
    }
}
