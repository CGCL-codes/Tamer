package net.sourceforge.lichttools;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;

/**
 * LichtTools -- simulate a bulb.
 *
 *
 * <p><b>Developer Notes<b></p>
 * 
 * Command used to create the keystore:
 * <p><tt>keytool -genkey -v -keystore android_release_lichttools.keystore -alias android_lichttools -keyalg RSA -keysize 2048 -validity 32750</tt></p>
 * @author Thomas Aglassinger
 */
public class LichtTools extends Activity implements OnClickListener, OnLongClickListener {

    private static final String KEY_LICHT_STATE_BEFORE_DISCO = "lichtStateBeforeDisco";

    private static final String KEY_LICHT_STATE = "lichtState";

    private static final int DISCO_DELAY_IN_MILLIS = 250;

    private enum LichtState {

        LICHT, FINSTER, DISCO
    }

    ;

    /**
	 * Runnable to update the disco color.
	 */
    private class DiscoColorUpdater implements Runnable {

        private long mPreviousUpdateMillis;

        private int mDiscoColorIndex;

        public DiscoColorUpdater() {
            mPreviousUpdateMillis = SystemClock.uptimeMillis();
        }

        @Override
        public void run() {
            assert mDiscoHandler != null;
            mLichtView.setBackgroundColor(mDiscoColors[mDiscoColorIndex]);
            mDiscoColorIndex = (mDiscoColorIndex + 1) % mDiscoColors.length;
            mDiscoHandler.postAtTime(this, mPreviousUpdateMillis + DISCO_DELAY_IN_MILLIS);
            mPreviousUpdateMillis = SystemClock.uptimeMillis();
        }
    }

    private LichtState mLichtState;

    private LichtState mLichtStateBeforeDisco;

    private int[] mDiscoColors;

    private int mFinsterColor;

    private int mLichtColor;

    private View mLichtView;

    private Handler mDiscoHandler;

    private DiscoColorUpdater mDiscoUpdater;

    /**
	 * Set up the view and various disco helpers, then restore the state from
	 * <code>savedInstanceState</code>.
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Resources res = getResources();
        mLichtColor = res.getColor(R.color.licht);
        mFinsterColor = res.getColor(R.color.finster);
        mDiscoColors = new int[] { res.getColor(R.color.disco1), res.getColor(R.color.disco2), res.getColor(R.color.disco3) };
        setContentView(R.layout.main);
        mLichtView = findViewById(R.id.lichtview);
        mLichtView.setOnClickListener(this);
        mLichtView.setOnLongClickListener(this);
        mDiscoHandler = new Handler();
        mDiscoUpdater = new DiscoColorUpdater();
        if (savedInstanceState != null) {
            mLichtStateBeforeDisco = lichtStateFrom(savedInstanceState.getString(KEY_LICHT_STATE_BEFORE_DISCO));
            setLichtState(lichtStateFrom(savedInstanceState.getString(KEY_LICHT_STATE)));
        } else {
            setLichtState(LichtState.LICHT);
        }
    }

    /**
	 * <code>Name</code> as <code>LichtState</code>. If <code>name</code> is
	 * <code>null</code> or does not describe a proper <code>LichtState</code>,
	 * use <code>LichtState.LICHT</code>.
	 */
    private LichtState lichtStateFrom(String name) {
        LichtState result;
        try {
            result = Enum.valueOf(LichtState.class, name);
        } catch (Exception error) {
            result = LichtState.LICHT;
        }
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = true;
        int menuId = item.getItemId();
        if (menuId == R.id.about) {
            about();
        } else {
            result = super.onOptionsItemSelected(item);
        }
        return result;
    }

    @Override
    protected void onPause() {
        super.onPause();
        ifNecessaryStopDisco();
    }

    /**
	 * If necessary, restart the disco.
	 */
    @Override
    protected void onResume() {
        super.onResume();
        if (mLichtState == LichtState.DISCO) {
            startDisco();
        }
    }

    /**
	 * The application version as described in the AndroidManifest.xml.
	 */
    private String getVersionName() {
        String result;
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(this.getPackageName(), 0);
            result = packageInfo.versionName;
        } catch (NameNotFoundException error) {
            result = "0.0";
        }
        return result;
    }

    /**
	 * Open the "About" alert and handle buttons pressed.
	 */
    private void about() {
        String versionName = getVersionName();
        String titleText = getString(R.string.dialog_about_title, versionName);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titleText);
        builder.setMessage(R.string.dialog_about_text);
        builder.setCancelable(true);
        builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.setNeutralButton(R.string.dialog_about_button_visit_site, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                Uri uri = Uri.parse("http://lichttools.sourceforge.net/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        builder.show();
    }

    /**
	 * Store the state of the light in <code>outState</code>.
	 */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_LICHT_STATE, mLichtState.name());
        if (mLichtStateBeforeDisco != null) {
            outState.putString(KEY_LICHT_STATE_BEFORE_DISCO, mLichtStateBeforeDisco.name());
        }
        super.onSaveInstanceState(outState);
    }

    private void startDisco() {
        mLichtStateBeforeDisco = mLichtState;
        setLichtState(LichtState.DISCO);
        mDiscoHandler.removeCallbacks(mDiscoUpdater);
        mDiscoHandler.post(mDiscoUpdater);
    }

    private void stopDisco() {
        assert mLichtState == LichtState.DISCO;
        mDiscoHandler.removeCallbacks(mDiscoUpdater);
        setLichtState(mLichtStateBeforeDisco);
    }

    private void ifNecessaryStopDisco() {
        if (mLichtState == LichtState.DISCO) {
            stopDisco();
        }
    }

    private void setLichtState(LichtState newLichtState) {
        if (newLichtState == LichtState.LICHT) {
            mLichtView.setBackgroundColor(mLichtColor);
        } else if (newLichtState == LichtState.FINSTER) {
            mLichtView.setBackgroundColor(mFinsterColor);
        } else if (newLichtState == LichtState.DISCO) {
        } else {
            throw new IllegalArgumentException("newLichtState=" + newLichtState);
        }
        mLichtState = newLichtState;
    }

    /** Toggle light or stop disco. */
    @Override
    public void onClick(View view) {
        assert view == mLichtView;
        if (mLichtState == LichtState.LICHT) {
            setLichtState(LichtState.FINSTER);
        } else if (mLichtState == LichtState.FINSTER) {
            setLichtState(LichtState.LICHT);
        } else if (mLichtState == LichtState.DISCO) {
            stopDisco();
        } else {
            throw new IllegalArgumentException("mLichtState=" + mLichtState);
        }
    }

    /** Toggle disco. */
    @Override
    public boolean onLongClick(View v) {
        if (mLichtState == LichtState.DISCO) {
            stopDisco();
        } else {
            startDisco();
        }
        return true;
    }
}
