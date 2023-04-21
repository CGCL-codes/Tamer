package net.sf.oneWayCrypto;

import java.io.File;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

class DecryptionNotifier {

    private final Notification statusNotification;

    private final Notification completeNotification;

    private final NotificationManager nm;

    private final Context context = AppUtils.getContext();

    private static int NEXT_ID = 1;

    private final int taskID = NEXT_ID++;

    public DecryptionNotifier() {
        this.nm = (NotificationManager) AppUtils.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        completeNotification = new Notification(R.drawable.icon, context.getString(R.string.decryption_started), System.currentTimeMillis());
        statusNotification = new Notification(R.drawable.icon, context.getString(R.string.decryption_started), System.currentTimeMillis());
        statusNotification.flags = Notification.FLAG_ONGOING_EVENT;
    }

    public void notifyProgress(int progress) {
        String status = context.getString(R.string.decryption_progress_status, progress);
        statusNotification.setLatestEventInfo(context, context.getString(R.string.decryption_started), status, PendingIntent.getActivity(context, 0, new Intent(context, AlertDialog.class), 0));
        nm.notify(taskID, statusNotification);
    }

    public void notifyComplete(File file, int total) {
        nm.cancel(taskID);
        Intent i = new Intent(Intent.ACTION_DEFAULT);
        i.setData(Uri.fromFile(file));
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, i, 0);
        String status = context.getResources().getQuantityString(R.plurals.decryption_completed_details, total, total);
        completeNotification.setLatestEventInfo(context, context.getString(R.string.decryption_completed, file.getName()), status, contentIntent);
        nm.notify(taskID, completeNotification);
    }

    public void notifyFailed(File file) {
        nm.cancel(taskID);
        Intent i = new Intent(Intent.ACTION_DEFAULT);
        i.addCategory(Intent.CATEGORY_BROWSABLE);
        i.setData(Uri.fromFile(file));
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, i, 0);
        String status = context.getString(R.string.decryption_error_details, file.getName());
        completeNotification.setLatestEventInfo(context, context.getString(R.string.decryption_error), status, contentIntent);
        nm.notify(taskID, completeNotification);
    }

    public void notifyStarted() {
        Toast.makeText(context, R.string.decryption_started, Toast.LENGTH_SHORT).show();
    }
}
