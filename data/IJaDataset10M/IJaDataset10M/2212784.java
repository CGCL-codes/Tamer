package org.wi.ui;

import org.wi.R;
import org.wi.db.DBFriend;
import org.wi.db.FriendProvider;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;

public class FriendPreferenceActivity extends PreferenceActivity {

    private static final String TAG = "FriendPreferenceActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_layout);
        loadDefaultPreference();
        setupPreference();
    }

    private void loadDefaultPreference() {
        ((CheckBoxPreference) findPreference("AUTO_SYNC")).setChecked(false);
        ((CheckBoxPreference) findPreference("SYNC_HEAD")).setChecked(true);
        ((ListPreference) findPreference("SYNC_FREQ")).setValue("1440");
        ((EditTextPreference) findPreference("NICK_NAME")).setText("");
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
    }

    private void setupPreference() {
        ContentResolver resolver = getContentResolver();
        String uid = this.getIntent().getStringExtra("uid");
        Log.i(TAG, "Preference Init: uid=" + uid);
        Cursor cursor = resolver.query(DBFriend.CONTENT_URI, null, null, null, null);
        if (cursor.isBeforeFirst() && cursor.moveToFirst()) {
            do {
                String allAccount = cursor.getString(cursor.getColumnIndex(DBFriend.ACCOUNT_ID));
                if (allAccount.contains("renren=" + uid)) {
                    String preference = cursor.getString(cursor.getColumnIndex(DBFriend.PREFERENCE));
                    Log.i(TAG, "All Preferences: " + preference);
                    Boolean autoSync = false;
                    Boolean syncHead = true;
                    String syncFreq = "1440";
                    String nickName = cursor.getString(cursor.getColumnIndex(DBFriend.ALIAS));
                    String[] preferences = preference.split(DBFriend.DEFAULT_DIVIDER);
                    for (String item : preferences) {
                        Log.i(TAG, "Preference Set: " + item);
                        if (item.startsWith(org.wi.entity.Preference.AUTO_SYNC + "=")) {
                            autoSync = Boolean.parseBoolean(item.substring(item.indexOf('=') + 1));
                        } else if (item.startsWith(org.wi.entity.Preference.SYNC_HEAD + "=")) {
                            syncHead = Boolean.parseBoolean(item.substring(item.indexOf('=') + 1));
                        } else if (item.startsWith(org.wi.entity.Preference.SYNC_FREQ + "=")) {
                            syncFreq = item.substring(item.indexOf('=') + 1);
                        }
                    }
                    Log.i(TAG, "Find compatible preference: nickName=" + nickName);
                    ((CheckBoxPreference) findPreference("AUTO_SYNC")).setChecked(autoSync);
                    ((CheckBoxPreference) findPreference("SYNC_HEAD")).setChecked(syncHead);
                    ((ListPreference) findPreference("SYNC_FREQ")).setValue(syncFreq);
                    ((EditTextPreference) findPreference("NICK_NAME")).setText(nickName);
                    break;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            saveSettings();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void saveSettings() {
        boolean autoSync = ((CheckBoxPreference) findPreference("AUTO_SYNC")).isChecked();
        boolean syncHead = ((CheckBoxPreference) findPreference("SYNC_HEAD")).isChecked();
        String syncFreq = ((ListPreference) findPreference("SYNC_FREQ")).getValue();
        String nickName = ((EditTextPreference) findPreference("NICK_NAME")).getText();
        ContentResolver resolver = getContentResolver();
        String uid = this.getIntent().getStringExtra("uid");
        Log.i(TAG, "save it. uid=" + uid + ", autoSync=" + autoSync + ", nickName=" + nickName);
        boolean existed = false;
        Cursor cursor = resolver.query(DBFriend.CONTENT_URI, null, null, null, null);
        if (cursor.isBeforeFirst()) {
            while (cursor.moveToNext()) {
                String allAccount = cursor.getString(cursor.getColumnIndex(DBFriend.ACCOUNT_ID));
                if (allAccount.contains("renren=" + uid)) {
                    existed = true;
                    break;
                }
            }
            ;
        }
        if (existed) {
            String id = cursor.getString(cursor.getColumnIndex(DBFriend._ID));
            String where = DBFriend._ID + "=" + id;
            ContentValues newValues = new ContentValues();
            newValues.put(DBFriend.PREFERENCE, org.wi.entity.Preference.AUTO_SYNC + "=" + autoSync + DBFriend.DEFAULT_DIVIDER + org.wi.entity.Preference.SYNC_HEAD + "=" + syncHead + DBFriend.DEFAULT_DIVIDER + org.wi.entity.Preference.SYNC_FREQ + "=" + syncFreq + DBFriend.DEFAULT_DIVIDER);
            newValues.put(DBFriend.ALIAS, nickName);
            int count = resolver.update(DBFriend.CONTENT_URI, newValues, where, null);
            Log.i(TAG, "update: " + count);
        } else {
            ContentValues newValues = new ContentValues();
            newValues.put(DBFriend.ACCOUNT_ID, "renren=" + uid + DBFriend.DEFAULT_DIVIDER);
            newValues.put(DBFriend.PREFERENCE, org.wi.entity.Preference.AUTO_SYNC + "=" + autoSync + DBFriend.DEFAULT_DIVIDER + org.wi.entity.Preference.SYNC_HEAD + "=" + syncHead + DBFriend.DEFAULT_DIVIDER + org.wi.entity.Preference.SYNC_FREQ + "=" + syncFreq + DBFriend.DEFAULT_DIVIDER);
            newValues.put(DBFriend.ALIAS, nickName);
            Uri myRowUri = resolver.insert(DBFriend.CONTENT_URI, newValues);
            Log.i(TAG, "insert: " + myRowUri);
        }
        cursor.close();
    }
}
