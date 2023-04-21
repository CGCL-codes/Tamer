package com.gdma.good2go.utils;

import java.util.Calendar;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Simple notes database access helper class. Defines the basic CRUD operations
 * for the volunteering events, and gives the ability to list all events as well as
 * retrieve  a specific event.
 * 
 * This has been improved from the first version of this tutorial through the
 * addition of better error handling and also using returning a Cursor instead
 * of using a collection of inner classes (which is less scalable and not
 * recommended).
 */
public class EventsDbAdapter {

    public static final String KEY_EVENT_KEY = "eventkey";

    public static final String KEY_EVENTNAME = "name";

    public static final String KEY_EVENT_SHORT_INFO = "info";

    public static final String KEY_EVENT_DETAILS = "details";

    public static final String KEY_EVENT_GP_LONG = "gplong";

    public static final String KEY_EVENT_GP_LAT = "gplat";

    public static final String KEY_EVENT_DISTANCE = "distance";

    public static final String KEY_EVENT_DURATION = "duration";

    public static final String KEY_EVENT_CITY = "city";

    public static final String KEY_EVENT_STREET = "street";

    public static final String KEY_EVENT_STREET_NUMBER = "streetnumber";

    public static final String KEY_EVENT_TYPE_ANIMAL = "type_animal";

    public static final String KEY_EVENT_TYPE_CHILDREN = "type_children";

    public static final String KEY_EVENT_TYPE_DISABLED = "type_disabled";

    public static final String KEY_EVENT_TYPE_ELDERLY = "type_elderly";

    public static final String KEY_EVENT_TYPE_ENVIRONMENT = "type_environment";

    public static final String KEY_EVENT_TYPE_SPECIAL = "type_special";

    public static final String KEY_EVENT_IMAGE = "image";

    public static final String KEY_EVENT_START_TIME = "starttime";

    public static final String KEY_EVENT_END_TIME = "endtime";

    public static final String KEY_EVENT_PRE_REQ = "prereq";

    public static final String KEY_EVENT_NPO_NAME = "npo_name";

    public static final String KEY_EVENT_IS_FOR_GROUPS = "for_groups";

    public static final String KEY_EVENT_IS_FOR_INDIVID = "for_individ";

    public static final String KEY_EVENT_IS_FOR_KIDS = "for_kids";

    public static final String KEY_EVENT_WORK_MENIAL = "menial";

    public static final String KEY_EVENT_WORK_MENTAL = "mental";

    public static final String KEY_EVENT_OCCURENCE_KEY = "occkey";

    public static final String KEY_EVENT_GROUP_HOW_MANY = "how_many";

    public static final String KEY_EVENT_DURATION_IN_MINUTES = "duration_in_minutes";

    public static final String KEY_EVENTID = "_id";

    public static final String EVENTS_ORDER = "ABS(" + KEY_EVENT_DISTANCE + ")" + " ASC ";

    private static final String TAG = "EventsDbAdapter";

    private DatabaseHelper mDbHelper;

    private SQLiteDatabase mDb;

    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE = "create table events (" + KEY_EVENTID + " integer primary key autoincrement, " + KEY_EVENTNAME + " text not null, " + KEY_EVENT_SHORT_INFO + " text not null, " + KEY_EVENT_DETAILS + " text not null, " + KEY_EVENT_DISTANCE + " text not null, " + KEY_EVENT_CITY + " text not null, " + KEY_EVENT_STREET + " text not null, " + KEY_EVENT_STREET_NUMBER + " text not null, " + KEY_EVENT_DURATION + " text not null, " + KEY_EVENT_GP_LONG + " text not null, " + KEY_EVENT_GP_LAT + " text not null, " + KEY_EVENT_TYPE_ANIMAL + " text not null, " + KEY_EVENT_TYPE_CHILDREN + " text not null, " + KEY_EVENT_TYPE_DISABLED + " text not null, " + KEY_EVENT_TYPE_ELDERLY + " text not null, " + KEY_EVENT_TYPE_ENVIRONMENT + " text not null, " + KEY_EVENT_TYPE_SPECIAL + " text not null, " + KEY_EVENT_KEY + " text not null, " + KEY_EVENT_IMAGE + " text not null, " + KEY_EVENT_START_TIME + " text not null, " + KEY_EVENT_END_TIME + " text not null, " + KEY_EVENT_PRE_REQ + " text not null, " + KEY_EVENT_NPO_NAME + " text not null, " + KEY_EVENT_IS_FOR_GROUPS + " text not null, " + KEY_EVENT_IS_FOR_INDIVID + " text not null, " + KEY_EVENT_IS_FOR_KIDS + " text not null, " + KEY_EVENT_WORK_MENIAL + " text not null, " + KEY_EVENT_WORK_MENTAL + " text not null, " + KEY_EVENT_OCCURENCE_KEY + " text not null, " + KEY_EVENT_GROUP_HOW_MANY + " text not null, " + KEY_EVENT_DURATION_IN_MINUTES + " text not null, " + "UNIQUE (" + KEY_EVENT_KEY + "));";

    private static final String DATABASE_NAME = "data";

    private static final String DATABASE_TABLE = "events";

    private static final int DATABASE_VERSION = 23;

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
            db.execSQL("DROP TABLE IF EXISTS events");
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public EventsDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the events database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public EventsDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    /**
     * Create a new event using the details provided. If the event is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     * 
     * @param name the name of the event
     * @param info the short info of the event
     * @param details the long details of the event
     * @param gplat the geopoint latitude
     * @param gplong the geopoint longtitude
     * @param mental 
     * @param menial 
     * @param kids 
     * @param individ 
     * @param groups 
     * @param npoName 
     * @param preReq 
     * @return rowId or -1 if failed
     */
    public long createEvent(String eventkey, String name, String info, String details, String gplat, String gplong, String distance, String duration, String city, String street, String streetNumber, String typeAnimal, String typeChildren, String typeDisabled, String typeElderly, String typeEnvironment, String typeSpecial, String eventImage, String startTime, String endTime, String preReq, String npoName, String groups, String individ, String kids, String menial, String mental, String occurenceKey, String howMany, String duration_in_minutes) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_EVENTNAME, name);
        initialValues.put(KEY_EVENT_SHORT_INFO, info);
        initialValues.put(KEY_EVENT_DETAILS, details);
        initialValues.put(KEY_EVENT_DISTANCE, distance);
        initialValues.put(KEY_EVENT_CITY, city);
        initialValues.put(KEY_EVENT_STREET, street);
        initialValues.put(KEY_EVENT_STREET_NUMBER, streetNumber);
        initialValues.put(KEY_EVENT_DURATION, duration);
        initialValues.put(KEY_EVENT_GP_LONG, gplong);
        initialValues.put(KEY_EVENT_GP_LAT, gplat);
        initialValues.put(KEY_EVENT_KEY, eventkey);
        initialValues.put(KEY_EVENT_TYPE_ANIMAL, typeAnimal);
        initialValues.put(KEY_EVENT_TYPE_CHILDREN, typeChildren);
        initialValues.put(KEY_EVENT_TYPE_DISABLED, typeDisabled);
        initialValues.put(KEY_EVENT_TYPE_ELDERLY, typeElderly);
        initialValues.put(KEY_EVENT_TYPE_ENVIRONMENT, typeEnvironment);
        initialValues.put(KEY_EVENT_TYPE_SPECIAL, typeSpecial);
        initialValues.put(KEY_EVENT_IMAGE, eventImage);
        initialValues.put(KEY_EVENT_START_TIME, startTime);
        initialValues.put(KEY_EVENT_END_TIME, endTime);
        initialValues.put(KEY_EVENT_PRE_REQ, preReq);
        initialValues.put(KEY_EVENT_NPO_NAME, npoName);
        initialValues.put(KEY_EVENT_IS_FOR_GROUPS, groups);
        initialValues.put(KEY_EVENT_IS_FOR_INDIVID, individ);
        initialValues.put(KEY_EVENT_IS_FOR_KIDS, kids);
        initialValues.put(KEY_EVENT_WORK_MENIAL, menial);
        initialValues.put(KEY_EVENT_WORK_MENTAL, mental);
        initialValues.put(KEY_EVENT_OCCURENCE_KEY, occurenceKey);
        initialValues.put(KEY_EVENT_GROUP_HOW_MANY, howMany);
        initialValues.put(KEY_EVENT_DURATION_IN_MINUTES, duration_in_minutes);
        long result = mDb.insert(DATABASE_TABLE, null, initialValues);
        return result;
    }

    /**
     * Delete the event with the given rowId
     * 
     * @param rowId id of event to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteEvent(long eventId) {
        return mDb.delete(DATABASE_TABLE, KEY_EVENTID + "=" + eventId, null) > 0;
    }

    /**
     * Delete all records in events table
     * 
     * @return true if deleted, false otherwise
     */
    public boolean deleteAllEvents() {
        return mDb.delete(DATABASE_TABLE, "1", null) > 0;
    }

    /**
     * Return a Cursor over the list of all events in the database
     * 
     * @return Cursor over all events
     */
    public Cursor fetchAllEvents() {
        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY, 8);
        int hour = now.get(Calendar.HOUR_OF_DAY);
        String where = "CAST(" + KEY_EVENT_START_TIME + " as INT)" + " > " + Integer.toString(hour);
        return mDb.query(DATABASE_TABLE, null, where, null, null, null, EVENTS_ORDER, null);
    }

    /**
     * Return a Cursor positioned at the event that matches the given rowId
     * 
     * @param eventId id of event to retrieve
     * @return Cursor positioned to matching event, if found
     * @throws SQLException if event could not be found/retrieved
     */
    public Cursor fetchEvent(long eventId) throws SQLException {
        Cursor mCursor = mDb.query(true, DATABASE_TABLE, null, KEY_EVENTID + "=" + eventId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchEventByFilters(String[] types, int radius, int timeInMinutes) throws SQLException {
        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY, 8);
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int i = 0;
        String q = "SELECT * FROM `events` WHERE " + "( CAST(" + KEY_EVENT_START_TIME + " as INT)" + " > " + Integer.toString(hour) + ")";
        String arr[] = { "", "" };
        if (timeInMinutes > 0) {
            q = q + " AND (" + "CAST(" + KEY_EVENT_DURATION_IN_MINUTES + " as INT) <=" + timeInMinutes + ")";
            arr[i] = Integer.toString(timeInMinutes);
            i++;
        }
        if (radius > 0) {
            q = q + " AND (" + "CAST(" + KEY_EVENT_DISTANCE + " as INT) <=" + radius + ")";
            arr[i] = Integer.toString(radius);
            i++;
        }
        if (types != null) {
            int j = 0;
            boolean flag = false;
            while (j < types.length) {
                if (types[j] == "animals") {
                    if (!flag) {
                        q = q + " AND (" + KEY_EVENT_TYPE_ANIMAL + " = \'1\' ";
                        flag = true;
                    } else q = q + " OR " + KEY_EVENT_TYPE_ANIMAL + " = \'1\' ";
                }
                if (types[j] == "children") {
                    if (!flag) {
                        q = q + " AND (" + KEY_EVENT_TYPE_CHILDREN + " = \'1\'";
                        flag = true;
                    } else q = q + " OR " + KEY_EVENT_TYPE_CHILDREN + " = \'1\' ";
                }
                if (types[j] == "disabled") {
                    if (!flag) {
                        q = q + "AND (" + KEY_EVENT_TYPE_DISABLED + " = \'1\' ";
                        flag = true;
                    } else q = q + " OR " + KEY_EVENT_TYPE_DISABLED + " = \'1\' ";
                }
                if (types[j] == "elderly") {
                    if (!flag) {
                        q = q + " AND (" + KEY_EVENT_TYPE_ELDERLY + " = \'1\' ";
                        flag = true;
                    } else q = q + " OR " + KEY_EVENT_TYPE_ELDERLY + " = \'1\' ";
                }
                if (types[j] == "environment") {
                    if (!flag) {
                        q = q + " AND (" + KEY_EVENT_TYPE_ENVIRONMENT + " = \'1\' ";
                        flag = true;
                    } else q = q + " OR " + KEY_EVENT_TYPE_ENVIRONMENT + " = \'1\' ";
                }
                if (types[j] == "special") {
                    if (!flag) {
                        q = q + " AND (" + KEY_EVENT_TYPE_SPECIAL + " = \'1\' ";
                        flag = true;
                    } else q = q + " OR " + KEY_EVENT_TYPE_SPECIAL + " = \'1\'";
                }
                j++;
            }
            if (flag) q = q + ")";
        }
        q = q + " ORDER BY " + EVENTS_ORDER;
        Cursor mCursor = mDb.rawQuery(q, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /**
     * Update the event using the details provided. The event to be updated is
     * specified using the eventId, and it is altered to use the details
     * values passed in
     * 
     * @param eventId id of event to update
     * @param distance value to set distance to
     * @return true if the event was successfully updated, false otherwise
     */
    public boolean updateEvent(long eventId, String distance) {
        ContentValues args = new ContentValues();
        args.put(KEY_EVENT_DISTANCE, distance);
        return mDb.update(DATABASE_TABLE, args, KEY_EVENTID + " = " + eventId, null) > 0;
    }

    public boolean isEventsEmpty() {
        Cursor c = fetchAllEvents();
        if (!c.isBeforeFirst()) return true;
        return false;
    }
}
