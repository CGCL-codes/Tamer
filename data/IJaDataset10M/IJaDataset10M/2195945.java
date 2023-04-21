package org.openintents.qrcode;

import org.openintents.extensions.qrcode.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class InfoActivity extends Activity {

    private static final String TAG = "InfoActivity";

    private static final boolean debug = !false;

    public static final int DIALOG_INFO = 1;

    public static final int DIALOG_GET_FROM_MARKET = 2;

    /**
	 * Whether dialog is simply pausing while hidden by another activity
	 * or when configuration changes.
	 * If this is false, then we can safely finish this activity if a dialog
	 * gets dismissed.
	 */
    private boolean mIsPausing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        if (i != null && savedInstanceState == null) {
            if (debug) Log.d(TAG, "new dialog");
            if (isApplicationAvailable()) {
                showDialog(DIALOG_INFO);
            } else {
                showDialog(DIALOG_GET_FROM_MARKET);
            }
        }
    }

    Intent getApplicationIntent() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("content://org.openintents.notepad/notes"));
        return intent;
    }

    boolean isApplicationAvailable() {
        return IntentUtils.isIntentAvailable(this, getApplicationIntent());
    }

    void launchApplication() {
        startActivity(getApplicationIntent());
        finish();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        switch(id) {
            case DIALOG_INFO:
                dialog = buildInfoDialog();
                break;
            case DIALOG_GET_FROM_MARKET:
                dialog = new GetFromMarketDialog(this, RD.string.notepad_not_available, RD.string.notepad_get, RD.string.notepad_market_uri, RD.string.notepad_developer_uri);
        }
        if (dialog == null) {
            dialog = super.onCreateDialog(id);
        }
        if (dialog != null) {
            dialog.setOnDismissListener(mDismissListener);
        }
        return dialog;
    }

    private AlertDialog buildInfoDialog() {
        return new AlertDialog.Builder(this).setIcon(R.drawable.ic_launcher).setTitle(RD.string.app_name).setMessage(RD.string.info_text).setPositiveButton(RD.string.info_launch, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {
                launchApplication();
            }
        }).create();
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch(id) {
            case DIALOG_INFO:
                break;
        }
    }

    OnDismissListener mDismissListener = new OnDismissListener() {

        public void onDismiss(DialogInterface dialoginterface) {
            if (debug) Log.d(TAG, "Dialog dismissed. Pausing: " + mIsPausing);
            if (!mIsPausing) {
                if (debug) Log.d(TAG, "finish");
                InfoActivity.this.finish();
            } else {
            }
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (debug) Log.d(TAG, "onSaveInstanceState");
        mIsPausing = true;
        if (debug) Log.d(TAG, "onSaveInstanceState. Pausing: " + mIsPausing);
    }

    @Override
    protected void onResume() {
        if (debug) Log.d(TAG, "onResume");
        super.onResume();
        mIsPausing = false;
    }
}
