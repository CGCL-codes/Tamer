package com.angis.fx.db;

import android.content.ContentValues;
import android.content.Context;

public class GISZFJGDBHelper extends ObjectDBHelper {

    public static final String PK_ZFID = "zfid";

    public static final String COLUMN_USERLAYER = "userlayer";

    public static final String COLUMN_X = "x";

    public static final String COLUMN_Y = "y";

    public static final String TABLE_NAME = "GISZFJG";

    public GISZFJGDBHelper(Context context) {
        super(context);
    }

    public long insert(ContentValues values) {
        return super.insert(TABLE_NAME, values);
    }

    public long deleteAll() {
        if (null == writeDB) {
            writeDB = dbHelper.getWritableDatabase();
        }
        return writeDB.delete(TABLE_NAME, null, null);
    }
}
