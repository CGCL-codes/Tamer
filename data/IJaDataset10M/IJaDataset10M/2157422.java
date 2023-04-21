package com.android.dbadapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBPlayerAdapter {

    public static final String KEY_ID = "id";

    public static final String KEY_NAME = "name";

    public static final String KEY_SCORE = "score";

    private static final String TAG = "DBAdapter";

    private static final String DATABASE_NAME = "ScoreKipper.db";

    private static final String DATABASE_TABLE_PLAYER = "PLAYER";

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE_PLAYER = "create table PLAYER (id integer primary key autoincrement, " + "name varchar not null unique, score integer);";

    private final Context context;

    private DatabaseHelper DBHelper;

    private SQLiteDatabase db;

    public DBPlayerAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE_PLAYER);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS PLAYER");
            onCreate(db);
        }
    }

    public DBPlayerAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        DBHelper.close();
    }

    public long insertPlayer(String name) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_SCORE, 0);
        return db.insert(DATABASE_TABLE_PLAYER, null, initialValues);
    }

    public boolean deletePlayer(long id) {
        return db.delete(DATABASE_TABLE_PLAYER, KEY_ID + "=" + id, null) > 0;
    }

    public Cursor getAllPlayer() {
        return db.query(DATABASE_TABLE_PLAYER, new String[] { KEY_ID, KEY_NAME, KEY_SCORE }, null, null, null, null, null);
    }

    public Cursor getPlayer(long id) throws SQLException {
        Cursor mCursor = db.query(true, DATABASE_TABLE_PLAYER, new String[] { KEY_ID, KEY_NAME, KEY_SCORE }, KEY_ID + "=" + id, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public boolean updatePlayer(long id, String name, int score) {
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, name);
        args.put(KEY_SCORE, score);
        return db.update(DATABASE_TABLE_PLAYER, args, KEY_ID + "=" + id, null) > 0;
    }
}
