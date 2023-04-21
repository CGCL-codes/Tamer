package com.royfj.contactgroups;

import com.royfj.provider.ContactGroup;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Groups;
import android.util.Log;

public class ContactGroupManager {

    private static final String TAG = "ContactGroupManager";

    private static ContactGroupManager instance = null;

    private static String DEFAULT_GROUP_SELECT = Groups.TITLE + "!='Starred in Android'";

    private Context context = null;

    private ContentResolver resorlver;

    private AccountManager accountManager = null;

    private ContactGroupManager(Context context) {
        this.context = context;
        accountManager = AccountManager.get(context);
        resorlver = context.getContentResolver();
    }

    public static synchronized ContactGroupManager getInstance(Context context) {
        if (instance == null) {
            instance = new ContactGroupManager(context);
        }
        return instance;
    }

    public Cursor getGroups() {
        String selection = DEFAULT_GROUP_SELECT + " AND " + Groups.DELETED + "=0";
        String sortOrder = Groups.TITLE + " COLLATE LOCALIZED ASC";
        return resorlver.query(Groups.CONTENT_URI, null, selection, null, sortOrder);
    }

    public Account[] getAccounts() {
        Account[] accounts = accountManager.getAccounts();
        for (Account account : accounts) {
            Log.d(TAG, "Account Name: " + account.name);
            Log.d(TAG, "Account Type: " + account.type);
        }
        return accounts;
    }

    public int getAccountCount() {
        Account[] accounts = getAccounts();
        return accounts != null ? accounts.length : 0;
    }

    public String[] getAccountNames() {
        Account[] accounts = getAccounts();
        if (accounts != null && accounts.length > 0) {
            String[] accountNames = new String[accounts.length];
            int i = 0;
            for (Account account : accounts) {
                accountNames[i++] = account.name;
            }
            return accountNames;
        }
        return null;
    }

    public Cursor getGroupsByType(String accountType) {
        String selection = DEFAULT_GROUP_SELECT + " AND " + Groups.ACCOUNT_TYPE + "=?" + Groups.DELETED + "=0";
        String[] selectionArgs = new String[] { accountType };
        return resorlver.query(Groups.CONTENT_URI, null, selection, selectionArgs, null);
    }

    public Cursor getGroupById(String id) {
        String selection = DEFAULT_GROUP_SELECT + " AND " + Groups._ID + "='" + id + "'";
        return resorlver.query(Groups.CONTENT_URI, null, selection, null, null);
    }

    public Cursor getGroupsByName(String accountName) {
        String selection = DEFAULT_GROUP_SELECT + " AND " + Groups.ACCOUNT_NAME + "=?" + " AND " + Groups.DELETED + "=?";
        String[] selectionArgs = new String[] { accountName, "0" };
        String sortOrder = Groups.TITLE + " COLLATE LOCALIZED ASC";
        return resorlver.query(Groups.CONTENT_URI, null, selection, selectionArgs, sortOrder);
    }

    public String getAccountByGroupId(String id) {
        Cursor cursor = getGroupById(id);
        String accountName = null;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            accountName = cursor.getString(cursor.getColumnIndex(Groups.ACCOUNT_NAME));
        }
        return accountName;
    }

    /**
   * Retrieves the account by account name
   * 
   * @param accountName
   *          account name
   * @return Account object
   */
    public Account getAccount(String accountName) {
        if (accountName != null) {
            Account[] accounts = getAccounts();
            for (Account account : accounts) {
                if (accountName.equals(account.name)) {
                    return account;
                }
            }
        }
        return null;
    }

    /**
   * Create a group for an account
   * 
   * @param groupName
   * @param accountName
   */
    public void createGroup(String groupName, String accountName) {
        ContentValues groupValues = new ContentValues();
        groupValues.put(Groups.TITLE, groupName);
        Account account = getAccount(accountName);
        if (account != null) {
            groupValues.put(Groups.ACCOUNT_NAME, accountName);
            groupValues.put(Groups.ACCOUNT_TYPE, account.type);
        }
        groupValues.put(Groups.GROUP_VISIBLE, 1);
        ContentResolver resolver = context.getContentResolver();
        Uri uri = resolver.insert(Groups.CONTENT_URI, groupValues);
        Log.i(TAG, "result: " + uri.getPath());
        resolver.notifyChange(Groups.CONTENT_URI, null);
    }

    public boolean deleteGroup(int groupId) {
        String[] as = new String[1];
        as[0] = String.valueOf(groupId);
        int result = context.getContentResolver().delete(Groups.CONTENT_URI, Groups._ID + "=?", as);
        context.getContentResolver().notifyChange(Groups.CONTENT_URI, null);
        return result > 0;
    }

    public boolean updateGroup(int groupId, String s) {
        ContentValues contentvalues = new ContentValues();
        contentvalues.put(Groups.TITLE, s);
        int r = context.getContentResolver().update(Groups.CONTENT_URI, contentvalues, Groups._ID + "=" + groupId, null);
        return r > 0;
    }

    public boolean addToGroup(String rawContactId, String groupId) {
        removeContactFromGroup(rawContactId, groupId);
        Log.d(TAG, "[addToGroup]rawContactId:" + rawContactId + " - groupId: " + groupId);
        ContentValues values = new ContentValues();
        values.put(ContactsContract.CommonDataKinds.GroupMembership.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID, groupId);
        values.put(ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE, ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE);
        Uri uri = context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
        context.getContentResolver().notifyChange(ContactsContract.Data.CONTENT_URI, null);
        return uri != null;
    }

    /**
   * Delete the contact from the group
   * 
   * @param rawContactId
   *          Raw contact id in raw_contact table
   * @param groupId
   * @return
   */
    public boolean removeContactFromGroup(String rawContactId, String groupId) {
        String where = ContactsContract.CommonDataKinds.GroupMembership.RAW_CONTACT_ID + "=? AND " + ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID + "=? AND " + ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE + "=?";
        String[] selectionArgs = new String[] { rawContactId, groupId, ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE };
        int result = context.getContentResolver().delete(ContactsContract.Data.CONTENT_URI, where, selectionArgs);
        context.getContentResolver().notifyChange(ContactsContract.Data.CONTENT_URI, null);
        return result > 0;
    }

    /**
   * Check if the ringtone is set for the group
   * @param groupId group id
   * @return
   */
    public boolean isSetGroupRingtone(String groupId) {
        return getGroupRingtone(groupId) != null;
    }

    public void addGroupRingtone(String groupId, String ringtone) {
        ContentValues values = new ContentValues();
        values.put(ContactGroup.COLUMN_GROUP_ID, groupId);
        values.put(ContactGroup.COLUMN_GROUP_RINGTONE, ringtone);
        context.getContentResolver().insert(ContactGroup.CONTENT_URI, values);
    }

    public void updateGroupRingtone(String ringtone, String groupId) {
        String selection = ContactGroup.COLUMN_GROUP_ID + "=?";
        String[] selectionArgs = new String[] { groupId };
        ContentValues values = new ContentValues();
        values.put(ContactGroup.COLUMN_GROUP_RINGTONE, ringtone);
        context.getContentResolver().update(ContactGroup.CONTENT_URI, values, selection, selectionArgs);
    }

    public String getGroupRingtone(String groupId) {
        String selection = ContactGroup.COLUMN_GROUP_ID + "=?";
        String[] selectionArgs = new String[] { groupId };
        Cursor cursor = context.getContentResolver().query(ContactGroup.CONTENT_URI, null, selection, selectionArgs, null);
        String ringtone = null;
        if (cursor != null && cursor.getCount() > 0) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                ringtone = cursor.getString(cursor.getColumnIndex(ContactGroup.COLUMN_GROUP_RINGTONE));
            }
        }
        return ringtone;
    }
}
