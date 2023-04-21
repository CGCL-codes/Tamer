package com.lonedev.androftpsync;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AndroSyncDB extends SQLiteOpenHelper {

    public static final String DB_NAME = "androsyncdb";

    public static final int DB_VERSION = 1;

    public static final String TAG = "AndroSyncDB";

    public AndroSyncDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createProfileTable(db);
        createFolderSynchTable(db);
        createScheduleTable(db);
    }

    private void createScheduleTable(SQLiteDatabase db) {
    }

    private void createFolderSynchTable(SQLiteDatabase db) {
    }

    public final String CREATE_PROFILE_TABLE_SQL = "create table profile (" + "profile_id INTEGER PRIMARY KEY, " + "profile_name TEXT, " + "ftp_hostname TEXT, " + "ftp_username TEXT, " + "ftp_password TEXT " + ")";

    private void createProfileTable(SQLiteDatabase db) {
        Log.i(TAG, "Creating 'profile' table");
        Log.d(TAG, CREATE_PROFILE_TABLE_SQL);
        db.execSQL(CREATE_PROFILE_TABLE_SQL);
        Log.d(TAG, "Table 'profile' created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
