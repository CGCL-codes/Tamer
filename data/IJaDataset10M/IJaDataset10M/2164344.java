package at.dasz.KolabDroid.Provider;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import at.dasz.KolabDroid.Utils;

/**
 * @author arthur
 *
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "KolabDroid.db";

    private static final int DATABASE_VERSION = 7;

    public static final String COL_ID = "_id";

    public static final int COL_IDX_ID = 0;

    public static final String[] ID_PROJECTION = new String[] { COL_ID };

    /**
	 * @param context
	 */
    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createDb(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            Log.i("Database", "Upgrading Database from " + oldVersion + " to " + newVersion);
            if (oldVersion < 4) {
                dropDb(db);
                createDb(db);
            }
            if (oldVersion == 4) {
                db.execSQL("ALTER TABLE " + StatusProvider.STATUS_TABLE_NAME + " ADD " + StatusProvider.COL_fatalErrorMsg + " TEXT");
                oldVersion = 6;
            }
            if (oldVersion == 5) {
                try {
                    db.execSQL("ALTER TABLE " + StatusProvider.STATUS_TABLE_NAME + " ADD " + StatusProvider.COL_fatalErrorMsg + " TEXT");
                } catch (SQLException ex) {
                }
                oldVersion = 6;
            }
            if (oldVersion == 6) {
                dropDb(db);
                createDb(db);
                oldVersion = 7;
            }
        } else {
            Log.i("Database", "No need to upgrade Database");
        }
    }

    private void createDb(SQLiteDatabase db) {
        final String columns = Utils.join(", ", new String[] { COL_ID + " INTEGER PRIMARY KEY", LocalCacheProvider.COL_LOCAL_ID + " INTEGER UNIQUE", LocalCacheProvider.COL_LOCAL_HASH + " TEXT", LocalCacheProvider.COL_REMOTE_ID + " TEXT UNIQUE", LocalCacheProvider.COL_REMOTE_IMAP_UID + " TEXT", LocalCacheProvider.COL_REMOTE_CHANGEDDATE + " INTEGER", LocalCacheProvider.COL_REMOTE_SIZE + " INTEGER", LocalCacheProvider.COL_REMOTE_HASH + " BLOB" });
        db.execSQL("CREATE TABLE " + LocalCacheProvider.CONTACT_TABLE_NAME + " (" + columns + ");");
        db.execSQL("CREATE TABLE " + LocalCacheProvider.CALENDAR_TABLE_NAME + " (" + columns + ");");
        final String stat_columns = Utils.join(", ", new String[] { COL_ID + " INTEGER PRIMARY KEY", StatusProvider.COL_time + " INTEGER", StatusProvider.COL_task + " TEXT", StatusProvider.COL_items + " INTEGER", StatusProvider.COL_localChanged + " INTEGER", StatusProvider.COL_remoteChanged + " INTEGER", StatusProvider.COL_localNew + " INTEGER", StatusProvider.COL_remoteNew + " INTEGER", StatusProvider.COL_localDeleted + " INTEGER", StatusProvider.COL_remoteDeleted + " INTEGER", StatusProvider.COL_conflicted + " INTEGER", StatusProvider.COL_errors + " INTEGER", StatusProvider.COL_fatalErrorMsg + " TEXT" });
        db.execSQL("CREATE TABLE " + StatusProvider.STATUS_TABLE_NAME + " (" + stat_columns + ");");
    }

    private void dropDb(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + LocalCacheProvider.CONTACT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + LocalCacheProvider.CALENDAR_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + StatusProvider.STATUS_TABLE_NAME);
    }

    public void cleanDb(SQLiteDatabase db) {
        db.execSQL("DELETE FROM " + LocalCacheProvider.CONTACT_TABLE_NAME);
        db.execSQL("DELETE FROM " + LocalCacheProvider.CALENDAR_TABLE_NAME);
        db.execSQL("DELETE FROM " + StatusProvider.STATUS_TABLE_NAME);
    }
}
