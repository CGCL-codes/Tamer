package org.srsly;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Interface to SQLite backend for storing card data. 
 * 
 * @author Kevin B. Weaver
 *
 */
public class CardsDbAdapter {

    public static final String CARD_TABLE = "Cards";

    public static final String KEY_ROWID = "_id";

    public static final String KEY_CATEGORY = "category";

    public static final String KEY_BOX = "box";

    public static final String KEY_FRONT = "front";

    public static final String KEY_BACK = "back";

    private static final String TAG = "CardsDbAdapter";

    private DatabaseHelper mDbHelper;

    private SQLiteDatabase mDb;

    private static final String DATABASE_CREATE = "CREATE TABLE " + CARD_TABLE + " (" + KEY_ROWID + " integer primary key autoincrement, " + KEY_CATEGORY + " text not null, " + KEY_BOX + " integer not null," + KEY_FRONT + " text not null, " + KEY_BACK + " text not null);";

    private static final String DATABASE_NAME = "SRSly_data";

    private static final int DATABASE_VERSION = 2;

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
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
            onCreate(db);
        }
    }

    public CardsDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Opens the database for storing cards.
     * 
     * @return frontend to the opened database
     * @throws SQLException
     */
    public CardsDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    /**
     * Closes the cards database.
     * 
     */
    public void close() {
        mDbHelper.close();
    }

    /**
     * Creates a card.
     * 
     * @param front
     * @param back
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */
    public long createCard(String category, String front, String back) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_CATEGORY, category);
        initialValues.put(KEY_BOX, 0);
        initialValues.put(KEY_FRONT, front);
        initialValues.put(KEY_BACK, back);
        return mDb.insert(CARD_TABLE, null, initialValues);
    }

    /**
     * Updates a card.
     * 
     * @param rowId
     * @param front
     * @param back
     * @return the number of rows affected (this should only be 1 or 0 since rowId is unique)
     */
    public long updateCard(long rowId, String category, String front, String back) {
        ContentValues updateValues = new ContentValues();
        updateValues.put(KEY_CATEGORY, category);
        updateValues.put(KEY_FRONT, front);
        updateValues.put(KEY_BACK, back);
        return mDb.update(CARD_TABLE, updateValues, KEY_ROWID + " = " + rowId, null);
    }

    /**
     * Updates the box number of a card.
     * 
     * @param rowId
     * @param box
     * @return the number of rows affected (this should only be 1 or 0 since rowId is unique)
     */
    public long setBox(long rowId, int box) {
        ContentValues updateValues = new ContentValues();
        updateValues.put(KEY_BOX, box);
        return mDb.update(CARD_TABLE, updateValues, KEY_ROWID + " = " + rowId, null);
    }

    /**
     * Deletes a card.
     * 
     * @param rowId
     * @return the number of rows affected (this should only be 1 or 0 since rowId is unique)
     */
    public long deleteCard(long rowId) {
        return mDb.delete(CARD_TABLE, KEY_ROWID + " = " + rowId, null);
    }

    /**
     * Returns a card with a given rowId. 
     * 
     * @param rowId
     * @return Cursor to a table containing a card with a given rowId
     * @throws SQLException
     */
    public Cursor fetchCard(long rowId) throws SQLException {
        Cursor mCursor = mDb.query(CARD_TABLE, new String[] { KEY_ROWID, KEY_CATEGORY, KEY_BOX, KEY_FRONT, KEY_BACK }, KEY_ROWID + "=" + rowId, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /**
     * Returns all cards.
     * 
     * @return Cursor to a table containing all cards, ordered alphabetically by the front of the card.
     */
    public Cursor fetchAllCards() {
        return mDb.query(CARD_TABLE, new String[] { KEY_ROWID, KEY_CATEGORY, KEY_BOX, KEY_FRONT, KEY_BACK }, null, null, null, null, KEY_FRONT);
    }

    /**
     * Deletes all cards.
     * 
     * @return the number of rows affected
     */
    public long deleteAllCards() {
        return mDb.delete(CARD_TABLE, null, null);
    }

    /**
     * Returns all categories used in the table.
     * 
     * @return table containing all distinct categories in the database
     */
    public Cursor getAllCategories() {
        return mDb.query(true, CARD_TABLE, new String[] { KEY_ROWID, KEY_CATEGORY }, null, null, null, null, KEY_CATEGORY, null);
    }
}
