package com.android.deskclock;

import java.util.Calendar;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Alarm Clock alarm alert: pops visible indicator and plays alarm
 * tone
 */
public class AlarmAlert extends Activity {

    private static final String DEFAULT_SNOOZE = "10";

    private static final String DEFAULT_VOLUME_BEHAVIOR = "2";

    private Alarm mAlarm;

    private int mVolumeBehavior;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Alarm alarm = intent.getParcelableExtra(Alarms.ALARM_INTENT_EXTRA);
            if (mAlarm.id == alarm.id) {
                dismiss(true);
            }
        }
    };

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        mAlarm = getIntent().getParcelableExtra(Alarms.ALARM_INTENT_EXTRA);
        final String vol = PreferenceManager.getDefaultSharedPreferences(this).getString(SettingsActivity.KEY_VOLUME_BEHAVIOR, DEFAULT_VOLUME_BEHAVIOR);
        mVolumeBehavior = Integer.parseInt(vol);
        final boolean requireUnlock = Settings.System.getInt(getContentResolver(), SettingsActivity.KEY_ALARM_REQUIRES_UNLOCK, 0) == 1;
        requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        if (!requireUnlock) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        }
        updateLayout();
        registerReceiver(mReceiver, new IntentFilter(Alarms.ALARM_KILLED));
    }

    private void setTitle() {
        String label = mAlarm.getLabelOrDefault(this);
        TextView title = (TextView) findViewById(R.id.alertTitle);
        title.setText(label);
    }

    protected View inflateView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.alarm_alert, null);
    }

    private void updateLayout() {
        LayoutInflater inflater = LayoutInflater.from(this);
        setContentView(inflateView(inflater));
        Button snooze = (Button) findViewById(R.id.snooze);
        snooze.requestFocus();
        snooze.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View v) {
                snooze();
            }
        });
        findViewById(R.id.dismiss).setOnClickListener(new Button.OnClickListener() {

            public void onClick(View v) {
                dismiss(false);
            }
        });
        setTitle();
    }

    private void snooze() {
        final String snooze = PreferenceManager.getDefaultSharedPreferences(this).getString(SettingsActivity.KEY_ALARM_SNOOZE, DEFAULT_SNOOZE);
        int snoozeMinutes = Integer.parseInt(snooze);
        final long snoozeTime = System.currentTimeMillis() + (1000 * 60 * snoozeMinutes);
        Alarms.saveSnoozeAlert(AlarmAlert.this, mAlarm.id, snoozeTime);
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(snoozeTime);
        String label = mAlarm.getLabelOrDefault(this);
        label = getString(R.string.alarm_notify_snooze_label, label);
        Intent cancelSnooze = new Intent(this, AlarmReceiver.class);
        cancelSnooze.setAction(Alarms.CANCEL_SNOOZE);
        cancelSnooze.putExtra(Alarms.ALARM_ID, mAlarm.id);
        PendingIntent broadcast = PendingIntent.getBroadcast(this, mAlarm.id, cancelSnooze, 0);
        NotificationManager nm = getNotificationManager();
        Notification n = new Notification(R.drawable.stat_notify_alarm, label, 0);
        n.setLatestEventInfo(this, label, getString(R.string.alarm_notify_snooze_text, Alarms.formatTime(this, c)), broadcast);
        n.flags |= Notification.FLAG_AUTO_CANCEL | Notification.FLAG_ONGOING_EVENT;
        nm.notify(mAlarm.id, n);
        String displayTime = getString(R.string.alarm_alert_snooze_set, snoozeMinutes);
        Log.v(displayTime);
        Toast.makeText(AlarmAlert.this, displayTime, Toast.LENGTH_LONG).show();
        stopService(new Intent(Alarms.ALARM_ALERT_ACTION));
        finish();
    }

    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    private void dismiss(boolean killed) {
        if (!killed) {
            NotificationManager nm = getNotificationManager();
            nm.cancel(mAlarm.id);
            stopService(new Intent(Alarms.ALARM_ALERT_ACTION));
        }
        finish();
    }

    /**
     * this is called when a second alarm is triggered while a
     * previous alert window is still active.
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (Log.LOGV) Log.v("AlarmAlert.OnNewIntent()");
        mAlarm = intent.getParcelableExtra(Alarms.ALARM_INTENT_EXTRA);
        setTitle();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Log.LOGV) Log.v("AlarmAlert.onDestroy()");
        unregisterReceiver(mReceiver);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean up = event.getAction() == KeyEvent.ACTION_UP;
        switch(event.getKeyCode()) {
            case KeyEvent.KEYCODE_VOLUME_UP:
            case KeyEvent.KEYCODE_VOLUME_DOWN:
            case KeyEvent.KEYCODE_CAMERA:
            case KeyEvent.KEYCODE_FOCUS:
                if (up) {
                    switch(mVolumeBehavior) {
                        case 1:
                            snooze();
                            break;
                        case 2:
                            dismiss(false);
                            break;
                        default:
                            break;
                    }
                }
                return true;
            default:
                break;
        }
        return super.dispatchKeyEvent(event);
    }
}
