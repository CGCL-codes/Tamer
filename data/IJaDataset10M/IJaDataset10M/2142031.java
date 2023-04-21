package com.emelsoft;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbAdapter {

    private DatabaseHelper dbHelper;

    private SQLiteDatabase db;

    public static final String KEY_TITLE = "title";

    public static final String KEY_PHONE_NUM = "phone_num";

    public static final String KEY_MSG = "msg";

    public static final String KEY_ROWID = "_id";

    public static final int COL_ROWID = 0;

    public static final int COL_TITLE = 1;

    public static final int COL_PHONE_NUM = 2;

    public static final int COL_MSG = 3;

    private static final String TAG = "QuickText";

    private static final String DATABASE_CREATE = "create table msgs (_id integer primary key autoincrement, " + "title text not null, phone_num text not null, msg text not null);";

    private static final String DATABASE_NAME = "data";

    private static final String DATABASE_TABLE = "msgs";

    private static final int DATABASE_VERSION = 1;

    private final Context ctx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS msgs");
            onCreate(db);
        }
    }

    public DbAdapter(Context ctx) {
        Log.d(TAG, "DbAdapter.<ctor>");
        this.ctx = ctx;
    }

    public DbAdapter open() throws SQLException {
        Log.d(TAG, "<> open()");
        dbHelper = new DatabaseHelper(ctx);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        Log.d(TAG, "<> close()");
        dbHelper.close();
    }

    public long addMsg(String title, String phoneNum, String msg) {
        Log.d(TAG, "-> addMsg(" + title + "," + phoneNum + "," + msg + ")");
        ContentValues vals = new ContentValues();
        vals.put(KEY_TITLE, title);
        vals.put(KEY_PHONE_NUM, phoneNum);
        vals.put(KEY_MSG, msg);
        return db.insert(DATABASE_TABLE, null, vals);
    }

    public void updateMsg(long rowId, String title, String phoneNum, String msg) {
        ContentValues vals = new ContentValues();
        vals.put(KEY_TITLE, title);
        vals.put(KEY_PHONE_NUM, phoneNum);
        vals.put(KEY_MSG, msg);
        db.update(DATABASE_TABLE, vals, KEY_ROWID + "=" + rowId, null);
    }

    public boolean deleteMsg(long rowId) {
        Log.d(TAG, "<> deleteMsg(" + rowId + ")");
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public Cursor fetchAllMsgs() {
        Log.d(TAG, "<> fetchAllMsgs()");
        Cursor cursor = db.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_TITLE, KEY_PHONE_NUM, KEY_MSG }, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchMsg(long rowId) throws SQLException {
        Log.d(TAG, "<> fetchMsg(" + rowId + ")");
        Cursor cursor = db.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_TITLE, KEY_PHONE_NUM, KEY_MSG }, KEY_ROWID + "=" + rowId, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public void clearMsgs() {
        Log.d(TAG, "<> clearMsgs()");
        db.execSQL("DROP TABLE IF EXISTS msgs");
        db.execSQL(DATABASE_CREATE);
    }
}
