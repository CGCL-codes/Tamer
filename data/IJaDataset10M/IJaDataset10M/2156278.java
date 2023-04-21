package org.karatasi.android.playground.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String name = "test.db";

    private static final int version = 1;

    public DBHelper(Context context) {
        super(context, name, null, version);
    }

    public void onCreate(SQLiteDatabase db) {
        System.out.println(name + ".onCreate");
        db.execSQL(Table.SQL_CREATE);
    }

    /** Called when the database has been opened.
	 * The implementation should check isReadOnly() before updating the database.
	 */
    public void onOpen(SQLiteDatabase db) {
        System.out.println(name + ".onOpen");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println(name + ".onUpgrade from " + oldVersion + " to " + newVersion);
        db.execSQL(Table.SQL_DROP);
        onCreate(db);
    }
}
