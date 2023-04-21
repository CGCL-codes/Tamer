package se.erikofsweden.findmyphone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

public class FindMyPhoneEmailReceiver extends BroadcastReceiver {

    public static final String EXTRA_SUBJECT = "com.fsck.k9.intent.extra.SUBJECT";

    public static final String ACTION_EMAIL_RECEIVED = "com.fsck.k9.intent.action.EMAIL_RECEIVED";

    public static final String EXTRA_FROM = "com.fsck.k9.intent.extra.FROM";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_EMAIL_RECEIVED)) {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
            boolean active = pref.getBoolean("service_active", false);
            boolean emailActive = pref.getBoolean("email_active", false);
            String secret = pref.getString("secret_text", "").toLowerCase();
            String emailPrefix = pref.getString("email_text", "").toLowerCase();
            Bundle bundle = intent.getExtras();
            if (bundle != null && emailActive && active && secret.length() > 0) {
                if (bundle.containsKey(EXTRA_SUBJECT) && bundle.containsKey(EXTRA_FROM)) {
                    String subject = bundle.getString(EXTRA_SUBJECT);
                    String from = bundle.getString(EXTRA_FROM);
                    boolean match = false;
                    if (emailPrefix.length() > 0 && subject.toLowerCase().startsWith(emailPrefix)) {
                        match = true;
                        subject = subject.substring(emailPrefix.length());
                    } else if (emailPrefix.length() <= 0) {
                        match = true;
                    } else {
                        match = false;
                    }
                    if (match && subject.toLowerCase().contains(secret)) {
                        match = true;
                    } else {
                        match = false;
                    }
                    if (match) {
                        Log.d(FindMyPhoneHelper.LOG_TAG, "Got Email with secret text " + from);
                        Intent locationIntent = new Intent(context, LocationMessageService.class);
                        locationIntent.setData(Uri.parse("?destinationAddress=" + from));
                        context.startService(locationIntent);
                    }
                }
            }
        }
    }
}
