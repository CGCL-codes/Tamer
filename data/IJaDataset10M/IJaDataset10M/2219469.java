package foo.mystats.test;

import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import foo.mystats.DbOpenHelper;
import foo.mystats.model.DataType;
import foo.mystats.model.Row;

public class RowTest extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        DbOpenHelper dbOpenHelper = new DbOpenHelper(getContext());
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.dropAllTables(db);
        dbOpenHelper.createAllTables(db);
        db.close();
    }

    public void testGetRowsWhenEmpty() {
        assertEquals(0, Row.getRowsCount(getContext()));
    }

    public void testGetRowsCountWhenNotEmpty() {
        int rowsCountBefore = Row.getRowsCount(getContext());
        Row.insertRow(getContext(), new Row("Test row", "Description", false, new DataType(DataType.INTEGER)));
        assertEquals(rowsCountBefore + 1, Row.getRowsCount(getContext()));
    }

    public void testSetNameNull() {
        boolean exceptionHappened = false;
        try {
            new Row(null, "Description", false, new DataType(DataType.INTEGER));
        } catch (IllegalArgumentException ex) {
            exceptionHappened = true;
        }
        assertTrue(exceptionHappened);
    }

    public void testSetNameEmpty() {
        boolean exceptionHappened = false;
        try {
            new Row("", "Description", false, new DataType(DataType.INTEGER));
        } catch (IllegalArgumentException ex) {
            exceptionHappened = true;
        }
        assertTrue(exceptionHappened);
    }

    public void testInsertWithNotUniqueName() {
        Row first = new Row("Unique", "Description", false, new DataType(DataType.INTEGER));
        Row second = new Row("Unique", "Description", false, new DataType(DataType.INTEGER));
        Row.insertRow(getContext(), first);
        boolean exception = false;
        try {
            Row.insertRow(getContext(), second);
        } catch (SQLiteConstraintException ex) {
            exception = true;
        }
        assertTrue(exception);
    }

    public void testInsertWithNotUniqueNameAfterTrim() {
        Row first = new Row("Unique", "Description", false, new DataType(DataType.INTEGER));
        Row second = new Row(" Unique	", "Description", false, new DataType(DataType.INTEGER));
        Row.insertRow(getContext(), first);
        boolean exception = false;
        try {
            Row.insertRow(getContext(), second);
        } catch (SQLiteConstraintException ex) {
            exception = true;
        }
        assertTrue(exception);
    }

    public void testSetBadDataType() {
        boolean exception = false;
        try {
            new Row("name", "description", true, new DataType(100500));
        } catch (IllegalArgumentException ex) {
            exception = true;
        }
        assertTrue(exception);
    }

    public void testInsertAllData() {
        Row row = new Row("name", "description", true, new DataType(DataType.INTEGER));
        long rowid = Row.insertRow(getContext(), row);
        Row selectedRow = Row.getRowById(getContext(), rowid);
        assertTrue(row.equalsTo(selectedRow));
    }

    public void testUpdateRowById() {
        Row row = new Row("name", "description", true, new DataType(DataType.INTEGER));
        long rowid = Row.insertRow(getContext(), row);
        row.setName("anotherName");
        row.setDescription("anotherDescription");
        row.setId(rowid);
        Row.updateRow(getContext(), row);
        Row selectedRow = Row.getRowById(getContext(), rowid);
        assertTrue(row.equalsTo(selectedRow));
    }
}
