package android.core;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.content.ContentValues;
import android.content.Context;
import org.apache.commons.codec.binary.Base64;
import org.apache.harmony.xnet.provider.jsse.SSLClientSessionCache;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.net.ssl.SSLSession;

/**
 * Hook into harmony SSL cache to persist the SSL sessions.
 *
 * Current implementation is suitable for saving a small number of hosts -
 * like google services. It can be extended with expiration and more features
 * to support more hosts.
 *
 * {@hide}
 */
public class DatabaseSessionCache implements SSLClientSessionCache {

    private static final String TAG = "SslSessionCache";

    static DatabaseHelper sDefaultDatabaseHelper;

    private DatabaseHelper mDatabaseHelper;

    /**
     * Table where sessions are stored.
     */
    public static final String SSL_CACHE_TABLE = "ssl_sessions";

    private static final String SSL_CACHE_ID = "_id";

    /**
     * Key is host:port - port is not optional.
     */
    private static final String SSL_CACHE_HOSTPORT = "hostport";

    /**
     * Base64-encoded DER value of the session.
     */
    private static final String SSL_CACHE_SESSION = "session";

    /**
     * Time when the record was added - should be close to the time
     * of the initial session negotiation.
     */
    private static final String SSL_CACHE_TIME_SEC = "time_sec";

    public static final String DATABASE_NAME = "ssl_sessions.db";

    public static final int DATABASE_VERSION = 1;

    /** public for testing
     */
    public static final int SSL_CACHE_ID_COL = 0;

    public static final int SSL_CACHE_HOSTPORT_COL = 1;

    public static final int SSL_CACHE_SESSION_COL = 2;

    public static final int SSL_CACHE_TIME_SEC_COL = 3;

    private static final String SAVE_ON_ADD = "save_on_add";

    static boolean sHookInitializationDone = false;

    public static final int MAX_CACHE_SIZE = 256;

    private static final Map<String, byte[]> mExternalCache = new LinkedHashMap<String, byte[]>(MAX_CACHE_SIZE, 0.75f, true) {

        @Override
        public boolean removeEldestEntry(Map.Entry<String, byte[]> eldest) {
            boolean shouldDelete = this.size() > MAX_CACHE_SIZE;
            return shouldDelete;
        }
    };

    static boolean mNeedsCacheLoad = true;

    public static final String[] PROJECTION = new String[] { SSL_CACHE_ID, SSL_CACHE_HOSTPORT, SSL_CACHE_SESSION, SSL_CACHE_TIME_SEC };

    /**
     * This class needs to be installed as a hook, if the security property
     * is set. Getting the right classloader may be fun since we don't use
     * Provider to get its classloader, but in android this is in same
     * loader with AndroidHttpClient.
     *
     * This constructor will use the default database. You must
     * call init() before to specify the context used for the database and
     * check settings.
     */
    public DatabaseSessionCache() {
        Log.v(TAG, "Instance created.");
        this.mDatabaseHelper = sDefaultDatabaseHelper;
    }

    /**
     * Create a SslSessionCache instance, using the specified context to
     * initialize the database.
     *
     * This constructor will use the default database - created the first
     * time.
     *
     * @param activityContext
     */
    public DatabaseSessionCache(Context activityContext) {
        init(activityContext);
        this.mDatabaseHelper = sDefaultDatabaseHelper;
    }

    /**
     * Create a SslSessionCache that uses a specific database.
     *
     * @param database
     */
    public DatabaseSessionCache(DatabaseHelper database) {
        this.mDatabaseHelper = database;
    }

    /**
     * You must call this method to enable SSL session caching for an app.
     */
    public static synchronized void init(Context activityContext) {
        if (sHookInitializationDone) {
            return;
        }
        Context appContext = activityContext.getApplicationContext();
        sDefaultDatabaseHelper = new DatabaseHelper(appContext);
        sHookInitializationDone = true;
    }

    public void putSessionData(SSLSession session, byte[] der) {
        if (mDatabaseHelper == null) {
            return;
        }
        if (mExternalCache.size() > MAX_CACHE_SIZE) {
            Cursor byTime = mDatabaseHelper.getWritableDatabase().query(SSL_CACHE_TABLE, PROJECTION, null, null, null, null, SSL_CACHE_TIME_SEC);
            byTime.moveToFirst();
            String hostPort = byTime.getString(SSL_CACHE_HOSTPORT_COL);
            mDatabaseHelper.getWritableDatabase().delete(SSL_CACHE_TABLE, SSL_CACHE_HOSTPORT + "= ?", new String[] { hostPort });
        }
        long t0 = System.currentTimeMillis();
        String b64 = new String(Base64.encodeBase64(der));
        String key = session.getPeerHost() + ":" + session.getPeerPort();
        ContentValues values = new ContentValues();
        values.put(SSL_CACHE_HOSTPORT, key);
        values.put(SSL_CACHE_SESSION, b64);
        values.put(SSL_CACHE_TIME_SEC, System.currentTimeMillis() / 1000);
        synchronized (this.getClass()) {
            mExternalCache.put(key, der);
            try {
                mDatabaseHelper.getWritableDatabase().insert(SSL_CACHE_TABLE, null, values);
            } catch (SQLException ex) {
                Log.w(TAG, "Ignoring SQL exception when caching session", ex);
            }
        }
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            long t1 = System.currentTimeMillis();
            Log.d(TAG, "New SSL session " + session.getPeerHost() + " DER len: " + der.length + " " + (t1 - t0));
        }
    }

    public byte[] getSessionData(String host, int port) {
        if (mDatabaseHelper == null) {
            return null;
        }
        synchronized (this.getClass()) {
            if (mNeedsCacheLoad) {
                mNeedsCacheLoad = false;
                long t0 = System.currentTimeMillis();
                Cursor cur = null;
                try {
                    cur = mDatabaseHelper.getReadableDatabase().query(SSL_CACHE_TABLE, PROJECTION, null, null, null, null, null);
                    if (cur.moveToFirst()) {
                        do {
                            String hostPort = cur.getString(SSL_CACHE_HOSTPORT_COL);
                            String value = cur.getString(SSL_CACHE_SESSION_COL);
                            if (hostPort == null || value == null) {
                                continue;
                            }
                            byte[] der = Base64.decodeBase64(value.getBytes());
                            mExternalCache.put(hostPort, der);
                        } while (cur.moveToNext());
                    }
                } catch (SQLException ex) {
                    Log.d(TAG, "Error loading SSL cached entries ", ex);
                } finally {
                    if (cur != null) {
                        cur.close();
                    }
                    if (Log.isLoggable(TAG, Log.DEBUG)) {
                        long t1 = System.currentTimeMillis();
                        Log.d(TAG, "LOADED CACHED SSL " + (t1 - t0) + " ms");
                    }
                }
            }
            String key = host + ":" + port;
            return mExternalCache.get(key);
        }
    }

    public byte[] getSessionData(byte[] id) {
        return null;
    }

    /** Visible for testing.
     */
    public static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + SSL_CACHE_TABLE + " (" + SSL_CACHE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + SSL_CACHE_HOSTPORT + " TEXT UNIQUE ON CONFLICT REPLACE," + SSL_CACHE_SESSION + " TEXT," + SSL_CACHE_TIME_SEC + " INTEGER" + ");");
            db.execSQL("CREATE INDEX ssl_sessions_idx1 ON ssl_sessions (" + SSL_CACHE_HOSTPORT + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + SSL_CACHE_TABLE);
            onCreate(db);
        }
    }
}
