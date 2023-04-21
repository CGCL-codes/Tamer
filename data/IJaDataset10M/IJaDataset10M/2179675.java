package foo.mystats.model;

import java.util.Date;
import foo.mystats.DbOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Data {

    private static final int NO_ID_YET = -1;

    private long id;

    private long rowId;

    private Date dateTime;

    private Object value;

    private int dataType;

    public void setDataType(int _dataType) {
        this.dataType = _dataType;
    }

    public int getDataType() {
        return dataType;
    }

    public String toString() {
        return String.format("{id: '%d', row_id: '%d', datetime: '%s', value: '%s', value_type: '%d'}", getId(), getRowId(), getDateTime(), getValue(), getDataType());
    }

    public Data(long rowId, Date _dateTime, Object value) {
        setId(NO_ID_YET);
        setRowId(rowId);
        setDateTime(_dateTime);
        setValue(value);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRowId() {
        return rowId;
    }

    public void setRowId(long rowId) {
        this.rowId = rowId;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public Object getValue() {
        return value;
    }

    public String getValueAsString() {
        switch(getDataType()) {
            case DataType.INTEGER:
                return ((Integer) getValue()).toString();
            case DataType.REAL:
                return ((Double) getValue()).toString();
            case DataType.BOOLEAN:
                return ((Boolean) getValue()).booleanValue() ? "1" : "0";
            case DataType.STRING:
                return ((String) getValue());
            case DataType.TIME:
                return Long.toString(((Date) getValue()).getTime() / 1000);
            default:
                return null;
        }
    }

    public void setDateTime(Date _dateTime) {
        this.dateTime = _dateTime;
    }

    public void setValue(Object value) {
        if (value instanceof Integer) {
            setDataType(DataType.INTEGER);
        } else if (value instanceof String) {
            setDataType(DataType.STRING);
        } else if (value instanceof Boolean) {
            setDataType(DataType.BOOLEAN);
        } else if (value instanceof Double) {
            setDataType(DataType.REAL);
        } else if (value instanceof Date) {
            setDataType(DataType.TIME);
        } else {
            throw new IllegalArgumentException("Type is not supported. Should be one of these: Integer, String, Boolean, Double, Data.");
        }
        this.value = value;
    }

    public static long insertData(Context context, Data data) {
        DbOpenHelper dbOpenHelper = new DbOpenHelper(context);
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("row_id", data.getRowId());
        cv.put("datetime", data.getDateTime().getTime() / 1000);
        cv.put("value", data.getValueAsString());
        long rowid = db.insert("data", null, cv);
        db.close();
        return rowid;
    }

    public static Object getDataObjectFromCursor(Cursor c, int column, int dataType) {
        Object _value = null;
        switch(dataType) {
            case DataType.INTEGER:
                _value = Integer.valueOf(c.getInt(column));
                break;
            case DataType.REAL:
                _value = Double.valueOf(c.getDouble(column));
                break;
            case DataType.STRING:
                _value = c.getString(column);
                break;
            case DataType.BOOLEAN:
                _value = Boolean.valueOf(c.getInt(column) != 0);
                break;
            case DataType.TIME:
                _value = new Date(c.getLong(column) * 1000);
                break;
        }
        return _value;
    }

    public static Data getDataById(Context context, long dataId) {
        Data result = null;
        DbOpenHelper dbOpenHelper = new DbOpenHelper(context);
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        Cursor c = null;
        try {
            c = db.rawQuery("select\r\n" + "    row_id,\r\n" + "    r.data_type,\r\n" + "    datetime,\r\n" + "    value\r\n" + "from\r\n" + "    data d,\r\n" + "    row r\r\n" + "where\r\n" + "    r.id = d.row_id and\r\n" + "    d.id = ?", new String[] { Long.toString(dataId) });
            c.moveToFirst();
            long _rowId = c.getLong(0);
            int _dataType = c.getInt(1);
            Date _dateTime = new Date(c.getLong(2) * 1000);
            result = new Data(_rowId, _dateTime, getDataObjectFromCursor(c, 3, _dataType));
            result.setId(dataId);
        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return result;
    }

    public static void updateDataById(Context context, Data data) {
        final String updateSQL = "update\r\n" + "    data\r\n" + "set\r\n" + "    value = ?,\r\n" + "    datetime = ?\r\n" + "where\r\n" + "    id = ?\r\n" + "";
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null.");
        }
        if (data.getId() == -1) {
            throw new IllegalArgumentException("Data should have an ID");
        }
        Log.i("Data", data.toString());
        DbOpenHelper dbOpenHelper = new DbOpenHelper(context);
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        try {
            db.execSQL(updateSQL, new String[] { data.getValueAsString(), Long.toString(data.getDateTime().getTime() / 1000), Long.toString(data.getId()) });
        } finally {
            db.close();
        }
    }

    public static boolean deleteDataById(Context context, long rowid) {
        boolean result = false;
        DbOpenHelper dbOpenHelper = new DbOpenHelper(context);
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        try {
            db.execSQL("delete from data where id = ?", new String[] { Long.toString(rowid) });
            result = true;
        } finally {
            db.close();
        }
        return result;
    }
}
