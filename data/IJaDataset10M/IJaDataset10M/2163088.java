package com.android.exchange;

import com.android.email.provider.EmailContent.Message;
import com.android.email.provider.EmailContent.MessageColumns;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import java.util.ArrayList;

/**
 * EmailSyncAlarmReceiver (USAR) is used by the SyncManager to start up-syncs of user-modified data
 * back to the Exchange server.
 *
 * Here's how this works for Email, for example:
 *
 * 1) User modifies or deletes an email from the UI.
 * 2) SyncManager, which has a ContentObserver watching the Message class, is alerted to a change
 * 3) SyncManager sets an alarm (to be received by USAR) for a few seconds in the
 * future (currently 15), the delay preventing excess syncing (think of it as a debounce mechanism).
 * 4) ESAR Receiver's onReceive method is called
 * 5) ESAR goes through all change and deletion records and compiles a list of mailboxes which have
 * changes to be uploaded.
 * 6) ESAR calls SyncManager to start syncs of those mailboxes
 *
 */
public class EmailSyncAlarmReceiver extends BroadcastReceiver {

    final String[] MAILBOX_DATA_PROJECTION = { MessageColumns.MAILBOX_KEY };

    private static String TAG = "EmailSyncAlarm";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(TAG, "onReceive");
        ArrayList<Long> mailboxesToNotify = new ArrayList<Long>();
        ContentResolver cr = context.getContentResolver();
        int messageCount = 0;
        String selector = SyncManager.getEasAccountSelector();
        Cursor c = cr.query(Message.DELETED_CONTENT_URI, MAILBOX_DATA_PROJECTION, selector, null, null);
        try {
            while (c.moveToNext()) {
                messageCount++;
                long mailboxId = c.getLong(0);
                if (!mailboxesToNotify.contains(mailboxId)) {
                    mailboxesToNotify.add(mailboxId);
                }
            }
        } finally {
            c.close();
        }
        c = cr.query(Message.UPDATED_CONTENT_URI, MAILBOX_DATA_PROJECTION, selector, null, null);
        try {
            while (c.moveToNext()) {
                messageCount++;
                long mailboxId = c.getLong(0);
                if (!mailboxesToNotify.contains(mailboxId)) {
                    mailboxesToNotify.add(mailboxId);
                }
            }
        } finally {
            c.close();
        }
        for (Long mailboxId : mailboxesToNotify) {
            SyncManager.serviceRequest(mailboxId, SyncManager.SYNC_UPSYNC);
        }
        Log.v(TAG, "Changed/Deleted messages: " + messageCount + ", mailboxes: " + mailboxesToNotify.size());
    }
}
