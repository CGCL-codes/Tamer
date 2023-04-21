package at.dasz.KolabDroid.Settings;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings {

    public static final String SETTINGS = "SETTINGS";

    private SharedPreferences pref;

    private SharedPreferences.Editor edit;

    public Settings(Context ctx) {
        pref = ctx.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
    }

    public void edit() {
        edit = pref.edit();
    }

    public void save() {
        edit.commit();
    }

    public void cancel() {
        edit = null;
    }

    public String getHost() {
        return pref.getString("HOST", "");
    }

    public void setHost(String value) {
        edit.putString("HOST", value);
    }

    public int getPort() {
        return pref.getInt("PORT", 993);
    }

    public void setPort(int value) {
        edit.putInt("PORT", value);
    }

    public boolean getUseSSL() {
        return pref.getBoolean("USESSL", true);
    }

    public void setUseSSL(boolean value) {
        edit.putBoolean("USESSL", value);
    }

    public String getUsername() {
        return pref.getString("USERNAME", "");
    }

    public void setUsername(String value) {
        edit.putString("USERNAME", value);
    }

    public String getPassword() {
        return pref.getString("PASSWORD", "");
    }

    public void setPassword(String value) {
        edit.putString("PASSWORD", value);
    }

    public String getIMAPNamespace() {
        return pref.getString("IMAP_NAMESPACE", "");
    }

    public void setIMAPNamespace(String value) {
        edit.putString("IMAP_NAMESPACE", value);
    }

    public String getContactsFolder() {
        return pref.getString("FOLDER_CONTACTS", "");
    }

    public void setContactsFolder(String value) {
        edit.putString("FOLDER_CONTACTS", value);
    }

    public String getCalendarFolder() {
        return pref.getString("FOLDER_CALENDAR", "");
    }

    public void setCalendarFolder(String value) {
        edit.putString("FOLDER_CALENDAR", value);
    }

    public boolean getCreateRemoteHash() {
        return pref.getBoolean("CREATE_REMOTE_HASH", false);
    }

    public void setCreateRemoteHash(boolean value) {
        edit.putBoolean("CREATE_REMOTE_HASH", value);
    }

    public boolean getSyncCalendar() {
        return pref.getBoolean("SYNC_CALENDAR", true);
    }

    public void setSyncCalendar(boolean value) {
        edit.putBoolean("SYNC_CALENDAR", value);
    }

    public boolean getSyncContacts() {
        return pref.getBoolean("SYNC_CONTACTS", true);
    }

    public void setSyncContacts(boolean value) {
        edit.putBoolean("SYNC_CONTACTS", value);
    }

    public boolean getMergeContactsByName() {
        return pref.getBoolean("MERGE_CONTACTS_BY_NAME", false);
    }

    public void setMergeContactsByName(boolean value) {
        edit.putBoolean("MERGE_CONTACTS_BY_NAME", value);
    }

    public String getAccountType() {
        return pref.getString("ACCOUNT_TYPE", "");
    }

    public void setAccountType(String type) {
        edit.putString("ACCOUNT_TYPE", type);
    }

    public String getAccountName() {
        return pref.getString("ACCOUNT_NAME", "");
    }

    public void setAccountName(String name) {
        edit.putString("ACCOUNT_NAME", name);
    }
}
