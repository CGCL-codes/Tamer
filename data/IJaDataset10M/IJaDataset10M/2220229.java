package com.googlecode.cbrates;

import com.googlecode.cbrates.transport.DayValute;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Alex
 */
public class DbAdapter {

    private static final String DATABASE_NAME = "cbrates.db";

    private static final int DATABASE_VERSION = 9;

    private static final String DAY_VALUTE_TABLE_NAME = "day";

    private static final String DAY_VALUTE_ID = "vid";

    private static final String DAY_VALUTE_NAME = "name";

    private static final String DAY_VALUTE_NUM_CODE = "num_code";

    private static final String DAY_VALUTE_CHAR_CODE = "char_code";

    private static final String DAY_VALUTE_NOMINAL = "nominal";

    private static final String DAY_VALUTE_VALUE = "value";

    private static final String DAY_VALUTE_DIFF = "diff";

    private static final String[] DAY_VALUTE_COLLUMNS = new String[] { DAY_VALUTE_ID, DAY_VALUTE_NAME, DAY_VALUTE_NUM_CODE, DAY_VALUTE_CHAR_CODE, DAY_VALUTE_NOMINAL, DAY_VALUTE_VALUE, DAY_VALUTE_DIFF };

    private DatabaseHelper dbHelper;

    private SQLiteDatabase db;

    private final Context context;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = "CREATE TABLE " + DAY_VALUTE_TABLE_NAME + " (" + DAY_VALUTE_ID + " VARCHAR(10) PRIMARY KEY, " + DAY_VALUTE_NAME + " TEXT NOT NULL, " + DAY_VALUTE_NUM_CODE + " INTEGER NOT NULL, " + DAY_VALUTE_CHAR_CODE + " VARCHAR(5) NOT NULL, " + DAY_VALUTE_NOMINAL + " INTEGER NOT NULL, " + DAY_VALUTE_VALUE + " DOUBLE NOT NULL, " + DAY_VALUTE_DIFF + " DOUBLE NOT NULL);";
            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
            db.execSQL("DROP TABLE IF EXISTS " + DAY_VALUTE_TABLE_NAME);
            onCreate(db);
        }
    }

    public DbAdapter(Context context) {
        this.context = context;
    }

    public void open() {
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    private void addDayValute(DayValute dayValute) {
        db.insert(DAY_VALUTE_TABLE_NAME, null, initValues(dayValute));
    }

    private void updateDayValute(DayValute dayValute) {
        db.update(DAY_VALUTE_TABLE_NAME, initValues(dayValute), DAY_VALUTE_ID + "='" + dayValute.getId() + "'", null);
    }

    private DayValute fillDayValute(Cursor dayValuteCur) {
        DayValute dv = new DayValute();
        dv.setId(dayValuteCur.getString(0));
        dv.setName(dayValuteCur.getString(1));
        dv.setNumCode(dayValuteCur.getString(2));
        dv.setCharCode(dayValuteCur.getString(3));
        dv.setNominal(dayValuteCur.getInt(4));
        dv.setValue(dayValuteCur.getDouble(5));
        dv.setDiffWithPrev(dayValuteCur.getDouble(6));
        return dv;
    }

    private ContentValues initValues(DayValute dayValute) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(DAY_VALUTE_ID, dayValute.getId());
        initialValues.put(DAY_VALUTE_NAME, dayValute.getName());
        initialValues.put(DAY_VALUTE_CHAR_CODE, dayValute.getCharCode());
        initialValues.put(DAY_VALUTE_NUM_CODE, dayValute.getNumCode());
        initialValues.put(DAY_VALUTE_NOMINAL, dayValute.getNominal());
        initialValues.put(DAY_VALUTE_VALUE, dayValute.getValue());
        initialValues.put(DAY_VALUTE_DIFF, dayValute.getDiffWithPrev());
        return initialValues;
    }

    private void addDayValuteList(List<DayValute> dayValuteList) {
        for (DayValute dayValute : dayValuteList) {
            addDayValute(dayValute);
        }
    }

    private void setDayValuteList(List<DayValute> dayValuteList) {
        for (DayValute dayValute : dayValuteList) {
            updateDayValute(dayValute);
        }
    }

    public void updateDayValuteList(List<DayValute> dayValuteList) {
        if (dayValuteTableIsEmty()) {
            addDayValuteList(dayValuteList);
        } else {
            setDayValuteList(dayValuteList);
        }
    }

    public DayValute getDayValute(String id) {
        Cursor dayValuteCur = db.query(true, DAY_VALUTE_TABLE_NAME, DAY_VALUTE_COLLUMNS, DAY_VALUTE_ID + "='" + id + "'", null, null, null, null, null);
        dayValuteCur.moveToFirst();
        return fillDayValute(dayValuteCur);
    }

    public LinkedList<DayValute> getDayValuteList() {
        LinkedList<DayValute> dayValList = new LinkedList<DayValute>();
        Cursor dayValuteCur = db.query(true, DAY_VALUTE_TABLE_NAME, DAY_VALUTE_COLLUMNS, null, null, null, null, null, null);
        dayValuteCur.move(-1);
        while (dayValuteCur.moveToNext()) {
            dayValList.add(fillDayValute(dayValuteCur));
        }
        return dayValList;
    }

    private boolean dayValuteTableIsEmty() {
        return (db.compileStatement("SELECT COUNT(*) FROM " + DAY_VALUTE_TABLE_NAME).simpleQueryForLong() == 0);
    }
}
