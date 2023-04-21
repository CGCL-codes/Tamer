package cz.krtinec.telka;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        WakeLock lock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.FULL_WAKE_LOCK, "AlarmReceiver");
        lock.acquire();
        Log.i("AlarmReceiver", "AlarmReceived...");
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification n = new Notification(R.drawable.telka, context.getString(R.string.run_to_TV), intent.getLongExtra(Constants.SHOW_START_TIME, System.currentTimeMillis()));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
        n.setLatestEventInfo(context, context.getString(R.string.run_to_TV), intent.getStringExtra(Constants.SHOW_NAME) + " " + context.getString(R.string.just_starts), pendingIntent);
        n.defaults = Notification.DEFAULT_ALL;
        manager.notify(intent.getIntExtra(Constants.SHOW_ID, 0), n);
        lock.release();
    }
}
