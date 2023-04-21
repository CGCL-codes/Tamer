package org.kecher.scheduler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class EventsDbAdapter {

    public static final String KEY_ROWID = "_id";

    public static final String KEY_TITLE = "title";

    public static final String KEY_RUN_TIME_HOUR = "run_time_hour";

    public static final String KEY_RUN_TIME_MINUTE = "run_time_minute";

    public static final String KEY_SUN = "sun";

    public static final String KEY_MON = "mon";

    public static final String KEY_TUES = "tues";

    public static final String KEY_WED = "wed";

    public static final String KEY_THUR = "thur";

    public static final String KEY_FRI = "fri";

    public static final String KEY_SAT = "sat";

    public static final String KEY_MODE = "mode";

    public static final String KEY_VOL = "vol";

    public static final String KEY_VIBRATE = "vibrate";

    public static final String KEY_LAST_RUN = "last_run";

    public static final String KEY_NEXT_RUN = "next_run";

    private static final String TAG = "EventsDbAdapter";

    private DatabaseHelper mDbHelper;

    private SQLiteDatabase mDb;

    private static final String DATABASE_CREATE = "CREATE TABLE events (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + "title TEXT NOT NULL, run_time_hour INTEGER NOT NULL, " + "run_time_minute INTEGER NOT NULL, sun INTEGER NOT NULL, " + "mon INTEGER NOT NULL, tues INTEGER NOT NULL, " + "wed INTEGER NOT NULL, thur INTEGER NOT NULL, " + "fri INTEGER NOT NULL, sat INTEGER NOT NULL, " + "mode TEXT NOT NULL, vol INTEGER NOT NULL, " + "vibrate INTEGER NOT NULL, last_run INTEGER NOT NULL, " + "next_run INTEGER NOT NULL);";

    private static final String DATABASE_NAME = "data";

    private static final String DATABASE_TABLE = "events";

    private static final int DATABASE_VERSION = 1;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS events");
            onCreate(db);
        }
    }

    public EventsDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public EventsDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public long createEvent(String title, int runTimeHour, int runTimeMin, int sun, int mon, int tues, int wed, int thur, int fri, int sat, String mode, int vol, int vibe, long nextRun, long lastRun) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_RUN_TIME_HOUR, runTimeHour);
        initialValues.put(KEY_RUN_TIME_MINUTE, runTimeMin);
        initialValues.put(KEY_SUN, sun);
        initialValues.put(KEY_MON, mon);
        initialValues.put(KEY_TUES, tues);
        initialValues.put(KEY_WED, wed);
        initialValues.put(KEY_THUR, thur);
        initialValues.put(KEY_FRI, fri);
        initialValues.put(KEY_SAT, sat);
        initialValues.put(KEY_MODE, mode);
        initialValues.put(KEY_VOL, vol);
        initialValues.put(KEY_VIBRATE, vibe);
        initialValues.put(KEY_NEXT_RUN, nextRun);
        initialValues.put(KEY_LAST_RUN, lastRun);
        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    public boolean deleteEvent(long rowId) {
        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public Cursor fetchAllEvents() {
        return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_TITLE, KEY_RUN_TIME_HOUR, KEY_RUN_TIME_MINUTE, KEY_SUN, KEY_MON, KEY_TUES, KEY_WED, KEY_THUR, KEY_FRI, KEY_SAT, KEY_MODE, KEY_VOL, KEY_VIBRATE, KEY_LAST_RUN, KEY_NEXT_RUN }, null, null, null, null, null);
    }

    public Cursor fetchEvent(long rowId) throws SQLException {
        Cursor mCursor = mDb.query(true, DATABASE_TABLE, new String[] { KEY_ROWID, KEY_TITLE, KEY_RUN_TIME_HOUR, KEY_RUN_TIME_MINUTE, KEY_SUN, KEY_MON, KEY_TUES, KEY_WED, KEY_THUR, KEY_FRI, KEY_SAT, KEY_MODE, KEY_VOL, KEY_VIBRATE, KEY_LAST_RUN, KEY_NEXT_RUN }, KEY_ROWID + "=" + rowId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        Log.v(TAG, "Cursor size is " + mCursor.getCount() + " Requested Row = " + rowId);
        return mCursor;
    }

    public boolean updateEvent(long rowId, String title, int runTimeHour, int runTimeMin, int sun, int mon, int tues, int wed, int thur, int fri, int sat, String mode, int vol, int vibe, long lastRun, long nextRun) {
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE, title);
        args.put(KEY_RUN_TIME_HOUR, runTimeHour);
        args.put(KEY_RUN_TIME_MINUTE, runTimeMin);
        args.put(KEY_SUN, sun);
        args.put(KEY_MON, mon);
        args.put(KEY_TUES, tues);
        args.put(KEY_WED, wed);
        args.put(KEY_THUR, thur);
        args.put(KEY_FRI, fri);
        args.put(KEY_SAT, sat);
        args.put(KEY_MODE, mode);
        args.put(KEY_VOL, vol);
        args.put(KEY_VIBRATE, vibe);
        args.put(KEY_LAST_RUN, lastRun);
        args.put(KEY_NEXT_RUN, nextRun);
        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public Cursor updateEventRunTimes(Long rowId, Long lastRun, Long nextRun) {
        return mDb.rawQuery(("UPDATE " + DATABASE_TABLE + " SET " + KEY_LAST_RUN + "=?, " + KEY_NEXT_RUN + "=? WHERE " + KEY_ROWID + " =?"), new String[] { lastRun.toString(), nextRun.toString(), rowId.toString() });
    }
}
