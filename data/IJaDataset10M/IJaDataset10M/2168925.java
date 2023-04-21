package org.openremote.android.console;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import org.openremote.android.console.net.SavedServersNetworkCheckTestAsyncTask;

public class DataHelper {

    private static final String DATABASE_NAME = "example.db";

    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "table1";

    private Context context;

    private SQLiteDatabase db;

    private SQLiteStatement insertStmt;

    private SQLiteStatement deleteStmt;

    private SQLiteStatement selectStmt;

    private static final String INSERT = "insert into " + TABLE_NAME + "(name,info, auto, up,selected, failoverFor) values (?,?,?,?,?,?)";

    private static final String DELETE = "delete from " + TABLE_NAME + " where name = ?";

    private static final String SELECT = "select * from " + TABLE_NAME;

    private static final String FIND = "select * from " + TABLE_NAME + " where name = ?";

    private static final String FIND_FAILOVER = "select * from " + TABLE_NAME + " where failoverFor = ?";

    OpenHelper openHelper;

    public DataHelper(Context context) {
        this.context = context;
        openHelper = new OpenHelper(this.context);
        this.db = openHelper.getWritableDatabase();
        this.insertStmt = this.db.compileStatement(INSERT);
        this.deleteStmt = this.db.compileStatement(DELETE);
        this.selectStmt = this.db.compileStatement(SELECT);
    }

    public long insert(String name, String info) {
        this.selectStmt.bindString(1, name);
        String s[] = new String[1];
        s[0] = name;
        Cursor c = db.rawQuery(FIND, s);
        if (c.getColumnCount() > 0) return 0;
        c.close();
        this.insertStmt.bindString(1, name);
        this.insertStmt.bindString(2, info);
        return this.insertStmt.executeInsert();
    }

    public long find(String name) {
        String s[] = new String[1];
        s[0] = name;
        Cursor c = db.rawQuery(FIND, s);
        if (c.getCount() > 0) return 1;
        return 0;
    }

    public ArrayList<ControllerObject> findFailoverControllers(String name) {
        String s[] = new String[1];
        s[0] = name;
        Cursor cursor = db.rawQuery(FIND_FAILOVER, s);
        ArrayList<ControllerObject> list = new ArrayList<ControllerObject>();
        if (cursor.moveToFirst()) {
            do {
                list.add(new ControllerObject(cursor.getString(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getString(5)));
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }

    public void closeConnection() {
        openHelper.close();
    }

    public long insert(String name, String info, int auto, int up, int selected, String failoverFor) {
        this.insertStmt.bindString(1, name);
        this.insertStmt.bindString(2, info);
        this.insertStmt.bindLong(3, auto);
        this.insertStmt.bindLong(4, up);
        this.insertStmt.bindLong(5, selected);
        this.insertStmt.bindString(6, failoverFor);
        return this.insertStmt.executeInsert();
    }

    public void delete(String name) {
        this.deleteStmt.bindString(1, name);
        this.deleteStmt.execute();
    }

    public void deleteAll() {
        this.db.delete(TABLE_NAME, null, null);
    }

    public ArrayList<ControllerObject> getControllerData() {
        ArrayList<ControllerObject> list = new ArrayList<ControllerObject>();
        Cursor cursor = this.db.query(TABLE_NAME, null, null, null, null, null, "name desc");
        if (cursor.moveToFirst()) {
            do {
                list.add(new ControllerObject(cursor.getString(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getString(5)));
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }

    public ArrayList<ControllerObject> getFailoverControllerData() {
        ArrayList<ControllerObject> list = new ArrayList<ControllerObject>();
        Cursor cursor = this.db.query(TABLE_NAME, null, null, null, null, null, "name desc");
        if (cursor.moveToFirst()) {
            do {
                final ControllerObject co = new ControllerObject(cursor.getString(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getString(5));
                list.add(co);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }

    public List<String> selectAll() {
        List<String> list = new ArrayList<String>();
        Cursor cursor = this.db.query(TABLE_NAME, new String[] { "name" }, null, null, null, null, "name desc");
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }

    private static class OpenHelper extends SQLiteOpenHelper {

        OpenHelper(Context ctx) {
            super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_NAME + "(name TEXT PRIMARY KEY, info TEXT, auto INTEGER, up INTEGER, selected INTEGER, failoverFor TEXT)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w("Example", "Upgrading database, this will drop tables and recreate.");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}
