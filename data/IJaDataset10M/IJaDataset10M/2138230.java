package fi.tuska.jalkametri.db;

import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import fi.tuska.jalkametri.db.upgrade.DBCreator;
import fi.tuska.jalkametri.db.upgrade.DBUpgrader;
import fi.tuska.jalkametri.db.upgrade.DropAndCreate;
import fi.tuska.jalkametri.db.upgrade.RecalculatePortions;
import fi.tuska.jalkametri.db.upgrade.RunMultipleCommands;
import fi.tuska.jalkametri.db.upgrade.RunUpgradeCommand;
import fi.tuska.jalkametri.util.LogUtil;

public class DBAdapter {

    private static final String DATABASE_NAME = "jalkametri.db";

    private static final int DATABASE_VERSION = 36;

    public static final String KEY_ID = "id";

    public static final String KEY_NAME = "name";

    public static final String KEY_ICON = "icon";

    public static final String KEY_ORDER = "pos";

    public static final String KEY_PORTIONS = "portions";

    public static final String KEY_CATEGORY_ID = "cat_id";

    public static final String KEY_STRENGTH = "strength";

    public static final String KEY_VOLUME = "volume";

    public static final String KEY_SIZE_ID = "size_id";

    public static final String KEY_SIZE_NAME = "size_name";

    public static final String KEY_TIME = "time";

    public static final String KEY_COMMENT = "comment";

    public static final String TAG = "DBAdapter";

    public static final String ID_WHERE_CLAUSE = KEY_ID + " = ?";

    private SQLiteDatabase db;

    private final Context context;

    private DBHelper helper;

    private static DBUpgrader dbCreator = new DBCreator();

    private static DBUpgrader dbReCreator = new DropAndCreate();

    private static TreeMap<Integer, DBUpgrader> upgraders = new TreeMap<Integer, DBUpgrader>();

    static {
        upgraders.put(0, new DropAndCreate());
        upgraders.put(32, new RunUpgradeCommand(FavouritesDB.SQL_CREATE_TABLE_FAVOURITES_1));
        upgraders.put(33, new RunUpgradeCommand("ALTER TABLE drinks ADD COLUMN comment TEXT NOT NULL DEFAULT ''", "ALTER TABLE history ADD COLUMN comment TEXT NOT NULL DEFAULT ''", "ALTER TABLE favourites ADD COLUMN comment TEXT NOT NULL DEFAULT ''"));
        upgraders.put(34, new RunMultipleCommands(new RunUpgradeCommand("ALTER TABLE history ADD COLUMN portions FLOAT NOT NULL DEFAULT 0"), new RecalculatePortions("history")));
        upgraders.put(35, new RunUpgradeCommand(HistoryDB.SQL_CREATE_HISTORY_INDEX));
    }

    public DBAdapter(Activity activity) {
        this.context = activity.getApplicationContext();
        this.helper = new DBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DBAdapter(Context context) {
        this.context = context.getApplicationContext();
        this.helper = new DBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void open() {
        if (db != null) {
            return;
        }
        db = helper.getWritableDatabase();
    }

    public void close() {
        if (db != null) {
            db.close();
            db = null;
        }
    }

    protected SQLiteDatabase getDatabase() {
        if (db == null) open();
        assert db != null;
        return db;
    }

    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            LogUtil.w(TAG, "Creating the database tables from scratch");
            dbCreator.updateDB(context, db, 0, DATABASE_VERSION);
        }

        private int getStartOfUpgrade(int oldVersion) {
            SortedMap<Integer, DBUpgrader> head = upgraders.headMap(oldVersion + 1);
            if (!head.isEmpty()) {
                return head.lastKey();
            }
            return 0;
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            int curV = oldVersion;
            if (newVersion < oldVersion) {
                LogUtil.w(TAG, "Reverse-upgrading DB from version %s to %s", oldVersion, newVersion);
                dbReCreator.updateDB(context, db, oldVersion, newVersion);
                return;
            }
            LogUtil.w(TAG, "Upgrading DB from version %d to %d", oldVersion, newVersion);
            int startVersion = getStartOfUpgrade(oldVersion);
            SortedMap<Integer, DBUpgrader> runUpdates = upgraders.tailMap(startVersion);
            for (Entry<Integer, DBUpgrader> e : runUpdates.entrySet()) {
                DBUpgrader upgrader = e.getValue();
                int newV = e.getKey();
                if (newV >= newVersion) break;
                LogUtil.w(TAG, "Running DB upgrader for version %d: %s", newV, upgrader);
                upgrader.updateDB(context, db, curV, newV);
                curV = newV;
            }
        }
    }
}
