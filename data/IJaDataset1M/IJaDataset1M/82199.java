package com.agileprojectassistant;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

/**
 * Display a message as a notification, with an accompanying sound.
 */
public class MessageDisplay {

    private MessageDisplay() {
    }

    public static void displayMessage(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String sender = (String) extras.get("sender");
            String message = (String) extras.get("message");
            Util.generateNotification(context, "Message from " + sender + ": " + message);
            playNotificationSound(context);
        }
    }

    private static void playNotificationSound(Context context) {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (uri != null) {
            Ringtone rt = RingtoneManager.getRingtone(context, uri);
            if (rt != null) {
                rt.setStreamType(AudioManager.STREAM_NOTIFICATION);
                rt.play();
            }
        }
    }
}
