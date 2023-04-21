package net.androidseminar.reservation;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ReservationSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;

    public static final String TABLE_NAME = "RESERVATION";

    static final String[] COLUMNS = { "_id", "fahrzeug", "filiale", "bis", "von" };

    private static final String CREATE_STATEMENT = "CREATE TABLE " + TABLE_NAME + " " + "(_id INTEGER PRIMARY KEY, " + "fahrzeug TEXT, " + "filiale TEXT, " + "bis INTEGER, " + "von INTEGER);";

    private static final String DROP_STATEMENT = "DROP TABLE IF EXISTS RESERVATION;";

    public ReservationSQLiteOpenHelper(Context context) {
        super(context, TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTableReservation(db);
    }

    private void createTableReservation(SQLiteDatabase db) {
        db.execSQL(CREATE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTable(db);
        createTableReservation(db);
    }

    private void dropTable(SQLiteDatabase db) {
        db.execSQL(DROP_STATEMENT);
    }

    public List<Reservation> getAllReservations() {
        SQLiteDatabase readableDatabase = super.getReadableDatabase();
        Cursor resultSet = readableDatabase.query(TABLE_NAME, COLUMNS, null, null, null, null, null);
        List<Reservation> toReturn = new LinkedList<Reservation>();
        while (resultSet.moveToNext()) {
            Reservation res = new Reservation();
            res.setId(resultSet.getInt(0));
            res.setFahrzeug(resultSet.getString(1));
            res.setFiliale(resultSet.getString(2));
            res.setBis(new Date(resultSet.getLong(3)));
            res.setVon(new Date(resultSet.getLong(4)));
            toReturn.add(res);
        }
        return toReturn;
    }

    public void insertReservation(Reservation toAdd) {
        ContentValues toInsert = new ContentValues();
        toInsert.put(COLUMNS[0], toAdd.getId());
        toInsert.put(COLUMNS[1], toAdd.getFahrzeug());
        toInsert.put(COLUMNS[2], toAdd.getFiliale());
        toInsert.put(COLUMNS[3], toAdd.getBis().getTime());
        toInsert.put(COLUMNS[4], toAdd.getVon().getTime());
        SQLiteDatabase database = getWritableDatabase();
        database.insert(TABLE_NAME, null, toInsert);
    }
}
