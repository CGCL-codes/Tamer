package bruce.dai;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import org.apache.http.util.EncodingUtils;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.provider.OpenableColumns;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    private static String dbPath = "data/data/bruce.dai/databases/";

    private static String dbName = "dream.db";

    private static String dbTable = "dreaminfo";

    private SQLiteDatabase sdb = null;

    private Context myContext = null;

    public DBHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHelper(Context context) throws IOException {
        this(context, dbName, null, 1);
        this.myContext = context;
        sdb = generateSQLiteDatabase();
    }

    private SQLiteDatabase generateSQLiteDatabase() throws IOException {
        SQLiteDatabase sqlite = null;
        try {
            sqlite = SQLiteDatabase.openDatabase(dbPath + dbName, null, SQLiteDatabase.OPEN_READONLY);
        } catch (Exception e) {
            sqlite = null;
        }
        if (null == sqlite) {
            this.getReadableDatabase();
            InputStream is = myContext.getAssets().open(dbName);
            String target = dbPath + dbName;
            OutputStream os = new FileOutputStream(target);
            byte[] buffer = new byte[2048];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.flush();
            os.close();
            is.close();
        }
        sqlite = SQLiteDatabase.openDatabase(dbPath + dbName, null, SQLiteDatabase.OPEN_READONLY);
        return sqlite;
    }

    public Cursor selectCur(String name) throws UnsupportedEncodingException {
        Log.e("query by keyword", name);
        String sql = "";
        sql = "select * from " + dbTable + " where dreamcontent ='" + name + "'";
        return sdb.rawQuery(sql, null);
    }

    public Cursor selectAllCur() {
        return sdb.query(dbTable, null, null, null, null, null, null);
    }

    @Override
    public synchronized void close() {
        if (null != sdb) {
            sdb.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase arg0) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
