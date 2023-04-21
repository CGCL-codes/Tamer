package de.drak.Profiles.plugins;

import de.drak.Profiles.R;
import de.drak.Profiles.TimedProfiles;
import de.drak.Profiles.OnlineHelp;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

/**Used to Edit a Battery Condition*/
public class BatteryPicker extends Activity {

    private static final int MENU_SAVE = 1;

    private static final int MENU_DONT_SAVE = 2;

    static final String SAVE_PROCESS = "bat.proc";

    static final String SAVE_TYPE = "bat.type";

    static final int TYPE_CHARGE = 0;

    static final int TYPE_LOWER = 1;

    static final int TYPE_GRATER = 2;

    /**Indicates if Activity should return RESULT_CANCELED*/
    private boolean isCancelled;

    private void setProc(int i) {
        proc = i;
        TextView t = (TextView) findViewById(R.id.TextView01);
        t.setText(proc + " %");
    }

    private int proc;

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bat_pick);
        TimedProfiles.createTitle(this, getString(R.string.plugin_batterypicker));
        SeekBar bar = (SeekBar) findViewById(R.id.SeekBar01);
        bar.setMax(100);
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setProc(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        if (savedInstanceState == null) {
            final Bundle forwardedBundle = getIntent().getBundleExtra(QueryReceiver.INTENT_BUNDLE);
            if (forwardedBundle != null) {
                switch(forwardedBundle.getInt(SAVE_TYPE)) {
                    case TYPE_CHARGE:
                        ((RadioButton) findViewById(R.id.RadioCharging)).setChecked(true);
                        break;
                    case TYPE_LOWER:
                        ((RadioButton) findViewById(R.id.RadioLT)).setChecked(true);
                        setProc(forwardedBundle.getInt(SAVE_PROCESS));
                        bar.setProgress(forwardedBundle.getInt(SAVE_PROCESS));
                        break;
                    case TYPE_GRATER:
                        ((RadioButton) findViewById(R.id.RadioMT)).setChecked(true);
                        setProc(forwardedBundle.getInt(SAVE_PROCESS));
                        bar.setProgress(forwardedBundle.getInt(SAVE_PROCESS));
                        break;
                }
            }
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void finish() {
        if (isCancelled) setResult(RESULT_CANCELED); else {
            final Intent returnIntent = new Intent();
            final Bundle storeAndForwardExtras = new Bundle();
            if (((RadioButton) findViewById(R.id.RadioCharging)).isChecked()) {
                returnIntent.putExtra(TimedProfiles.INTENT_INFO_STR, getString(R.string.plugin_batterypicker_c));
                storeAndForwardExtras.putInt(SAVE_TYPE, TYPE_CHARGE);
            } else if (((RadioButton) findViewById(R.id.RadioLT)).isChecked()) {
                returnIntent.putExtra(TimedProfiles.INTENT_INFO_STR, "<=" + proc + "%");
                storeAndForwardExtras.putInt(SAVE_PROCESS, proc);
                storeAndForwardExtras.putInt(SAVE_TYPE, TYPE_LOWER);
            } else if (((RadioButton) findViewById(R.id.RadioMT)).isChecked()) {
                returnIntent.putExtra(TimedProfiles.INTENT_INFO_STR, ">=" + proc + "%");
                storeAndForwardExtras.putInt(SAVE_PROCESS, proc);
                storeAndForwardExtras.putInt(SAVE_TYPE, TYPE_GRATER);
            } else {
                setResult(TimedProfiles.RESULT_REMOVE);
                super.finish();
                return;
            }
            storeAndForwardExtras.putInt(QueryReceiver.TYPE_FIELD, QueryReceiver.IS_BATTERY);
            returnIntent.putExtra(QueryReceiver.INTENT_BUNDLE, storeAndForwardExtras);
            setResult(RESULT_OK, returnIntent);
        }
        super.finish();
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);
        final Intent helpIntent = new Intent(this, OnlineHelp.class);
        helpIntent.putExtra("com.twofortyfouram.locale.intent.extra.HELP_URL", OnlineHelp.HELP_URL_PRE + "plugin/BatteryPicker");
        helpIntent.putExtra(TimedProfiles.INTENT_TITLE, getTitle());
        menu.add(R.string.onlinehelp).setIcon(android.R.drawable.ic_menu_help).setIntent(helpIntent);
        menu.add(0, MENU_DONT_SAVE, 0, android.R.string.cancel).setIcon(android.R.drawable.ic_menu_close_clear_cancel).getItemId();
        menu.add(0, MENU_SAVE, 0, R.string.plugin_save).setIcon(android.R.drawable.ic_menu_save).getItemId();
        return true;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public boolean onMenuItemSelected(final int featureId, final MenuItem item) {
        switch(item.getItemId()) {
            case MENU_SAVE:
                {
                    finish();
                    return true;
                }
            case MENU_DONT_SAVE:
                {
                    isCancelled = true;
                    finish();
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }
}
