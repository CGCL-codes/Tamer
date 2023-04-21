package com.android.providers.contacts;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * This will be launched during system boot, after the core system has
 * been brought up but before any non-persistent processes have been
 * started.  It is launched in a special state, with no content provider
 * or custom application class associated with the process running.
 *
 * It's job is to prime the contacts database. Either create it
 * if it doesn't exist, or open it and force any necessary upgrades.
 * All of this heavy lifting happens before the boot animation ends.
 */
public class ContactsUpgradeReceiver extends BroadcastReceiver {

    static final String TAG = "ContactsUpgradeReceiver";

    static final String PREF_DB_VERSION = "db_version";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            long startTime = System.currentTimeMillis();
            SharedPreferences prefs = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
            int prefVersion = prefs.getInt(PREF_DB_VERSION, 0);
            if (prefVersion != ContactsDatabaseHelper.DATABASE_VERSION) {
                prefs.edit().putInt(PREF_DB_VERSION, ContactsDatabaseHelper.DATABASE_VERSION).commit();
                Log.i(TAG, "Creating or opening contacts database");
                ContactsDatabaseHelper helper = ContactsDatabaseHelper.getInstance(context);
                helper.getWritableDatabase();
                helper.close();
                EventLogTags.writeContactsUpgradeReceiver(System.currentTimeMillis() - startTime);
            }
        } catch (Throwable t) {
            Log.wtf(TAG, "Error during upgrade attempt. Disabling receiver.", t);
            context.getPackageManager().setComponentEnabledSetting(new ComponentName(context, getClass()), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        }
    }
}
