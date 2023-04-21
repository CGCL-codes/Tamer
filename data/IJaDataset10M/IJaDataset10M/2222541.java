package com.android.unit_tests;

import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.CharArrayBuffer;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Handler;
import android.os.Parcel;
import android.test.PerformanceTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.Collator;
import java.util.Arrays;
import junit.framework.Assert;
import junit.framework.TestCase;
import static android.database.DatabaseUtils.InsertHelper.TABLE_INFO_PRAGMA_COLUMNNAME_INDEX;
import static android.database.DatabaseUtils.InsertHelper.TABLE_INFO_PRAGMA_DEFAULT_INDEX;

public class DatabaseGeneralTest extends TestCase implements PerformanceTestCase {

    private static final String sString1 = "this is a test";

    private static final String sString2 = "and yet another test";

    private static final String sString3 = "this string is a little longer, but still a test";

    private static final String PHONE_NUMBER = "16175551212";

    private static final int CURRENT_DATABASE_VERSION = 42;

    private SQLiteDatabase mDatabase;

    private File mDatabaseFile;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mDatabaseFile = new File("/sqlite_stmt_journals", "database_test.db");
        if (mDatabaseFile.exists()) {
            mDatabaseFile.delete();
        }
        mDatabase = SQLiteDatabase.openOrCreateDatabase(mDatabaseFile.getPath(), null);
        assertNotNull(mDatabase);
        mDatabase.setVersion(CURRENT_DATABASE_VERSION);
    }

    @Override
    protected void tearDown() throws Exception {
        mDatabase.close();
        mDatabaseFile.delete();
        super.tearDown();
    }

    public boolean isPerformanceOnly() {
        return false;
    }

    public int startPerformance(Intermediates intermediates) {
        return 1;
    }

    private void populateDefaultTable() {
        mDatabase.execSQL("CREATE TABLE test (_id INTEGER PRIMARY KEY, data TEXT);");
        mDatabase.execSQL("INSERT INTO test (data) VALUES ('" + sString1 + "');");
        mDatabase.execSQL("INSERT INTO test (data) VALUES ('" + sString2 + "');");
        mDatabase.execSQL("INSERT INTO test (data) VALUES ('" + sString3 + "');");
    }

    @MediumTest
    public void testVersion() throws Exception {
        assertEquals(CURRENT_DATABASE_VERSION, mDatabase.getVersion());
        mDatabase.setVersion(11);
        assertEquals(11, mDatabase.getVersion());
    }

    @MediumTest
    public void testUpdate() throws Exception {
        populateDefaultTable();
        ContentValues values = new ContentValues(1);
        values.put("data", "this is an updated test");
        assertEquals(1, mDatabase.update("test", values, "_id=1", null));
        Cursor c = mDatabase.query("test", null, "_id=1", null, null, null, null);
        assertNotNull(c);
        assertEquals(1, c.getCount());
        c.moveToFirst();
        String value = c.getString(c.getColumnIndexOrThrow("data"));
        assertEquals("this is an updated test", value);
    }

    @MediumTest
    public void testPhoneNumbersEqual() throws Exception {
        mDatabase.execSQL("CREATE TABLE phones (num TEXT);");
        mDatabase.execSQL("INSERT INTO phones (num) VALUES ('911');");
        mDatabase.execSQL("INSERT INTO phones (num) VALUES ('5555');");
        mDatabase.execSQL("INSERT INTO phones (num) VALUES ('+" + PHONE_NUMBER + "');");
        String number;
        Cursor c;
        c = mDatabase.query("phones", null, "PHONE_NUMBERS_EQUAL(num, '504-555-7683')", null, null, null, null);
        assertTrue(c == null || c.getCount() == 0);
        c.close();
        c = mDatabase.query("phones", null, "PHONE_NUMBERS_EQUAL(num, '911')", null, null, null, null);
        assertNotNull(c);
        assertEquals(1, c.getCount());
        c.moveToFirst();
        number = c.getString(c.getColumnIndexOrThrow("num"));
        assertEquals("911", number);
        c.close();
        c = mDatabase.query("phones", null, "PHONE_NUMBERS_EQUAL(num, '5555')", null, null, null, null);
        assertNotNull(c);
        assertEquals(1, c.getCount());
        c.moveToFirst();
        number = c.getString(c.getColumnIndexOrThrow("num"));
        assertEquals("5555", number);
        c.close();
        c = mDatabase.query("phones", null, "PHONE_NUMBERS_EQUAL(num, '180055555555')", null, null, null, null);
        assertTrue(c == null || c.getCount() == 0);
        c.close();
        c = mDatabase.query("phones", null, "PHONE_NUMBERS_EQUAL(num, '+" + PHONE_NUMBER + "')", null, null, null, null);
        assertNotNull(c);
        assertEquals(1, c.getCount());
        c.moveToFirst();
        number = c.getString(c.getColumnIndexOrThrow("num"));
        assertEquals("+" + PHONE_NUMBER, number);
        c.close();
        c = mDatabase.query("phones", null, "PHONE_NUMBERS_EQUAL(num, '+1 (617).555-1212')", null, null, null, null);
        assertNotNull(c);
        assertEquals(1, c.getCount());
        c.moveToFirst();
        number = c.getString(c.getColumnIndexOrThrow("num"));
        assertEquals("+" + PHONE_NUMBER, number);
        c.close();
        c = mDatabase.query("phones", null, "PHONE_NUMBERS_EQUAL(num, '" + PHONE_NUMBER + "')", null, null, null, null);
        assertNotNull(c);
        assertEquals(1, c.getCount());
        c.moveToFirst();
        number = c.getString(c.getColumnIndexOrThrow("num"));
        assertEquals("+" + PHONE_NUMBER, number);
        c.close();
        c = mDatabase.query("phones", null, "PHONE_NUMBERS_EQUAL(num, '011" + PHONE_NUMBER + "')", null, null, null, null);
        assertNotNull(c);
        assertEquals(1, c.getCount());
        c.moveToFirst();
        number = c.getString(c.getColumnIndexOrThrow("num"));
        assertEquals("+" + PHONE_NUMBER, number);
        c.close();
        c = mDatabase.query("phones", null, "PHONE_NUMBERS_EQUAL(num, '00" + PHONE_NUMBER + "')", null, null, null, null);
        assertNotNull(c);
        assertEquals(1, c.getCount());
        c.moveToFirst();
        number = c.getString(c.getColumnIndexOrThrow("num"));
        assertEquals("+" + PHONE_NUMBER, number);
        c.close();
    }

    private void phoneNumberCompare(String phone1, String phone2, boolean equal, boolean useStrictComparation) {
        String[] temporalPhoneNumbers = new String[2];
        temporalPhoneNumbers[0] = phone1;
        temporalPhoneNumbers[1] = phone2;
        Cursor cursor = mDatabase.rawQuery(String.format("SELECT CASE WHEN PHONE_NUMBERS_EQUAL(?, ?, %d) " + "THEN 'equal' ELSE 'not equal' END", (useStrictComparation ? 1 : 0)), temporalPhoneNumbers);
        try {
            assertNotNull(cursor);
            assertTrue(cursor.moveToFirst());
            if (equal) {
                assertEquals(String.format("Unexpectedly, \"%s != %s\".", phone1, phone2), "equal", cursor.getString(0));
            } else {
                assertEquals(String.format("Unexpectedly, \"%s\" == \"%s\".", phone1, phone2), "not equal", cursor.getString(0));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void assertPhoneNumberEqual(String phone1, String phone2) throws Exception {
        assertPhoneNumberEqual(phone1, phone2, true);
        assertPhoneNumberEqual(phone1, phone2, false);
    }

    private void assertPhoneNumberEqual(String phone1, String phone2, boolean useStrict) throws Exception {
        phoneNumberCompare(phone1, phone2, true, useStrict);
    }

    private void assertPhoneNumberNotEqual(String phone1, String phone2) throws Exception {
        assertPhoneNumberNotEqual(phone1, phone2, true);
        assertPhoneNumberNotEqual(phone1, phone2, false);
    }

    private void assertPhoneNumberNotEqual(String phone1, String phone2, boolean useStrict) throws Exception {
        phoneNumberCompare(phone1, phone2, false, useStrict);
    }

    /**
     * Tests international matching issues for the PHONE_NUMBERS_EQUAL function.
     * 
     * @throws Exception
     */
    @SmallTest
    public void testPhoneNumbersEqualInternationl() throws Exception {
        assertPhoneNumberEqual("1", "1");
        assertPhoneNumberEqual("123123", "123123");
        assertPhoneNumberNotEqual("123123", "923123");
        assertPhoneNumberNotEqual("123123", "123129");
        assertPhoneNumberNotEqual("123123", "1231234");
        assertPhoneNumberNotEqual("123123", "0123123", false);
        assertPhoneNumberNotEqual("123123", "0123123", true);
        assertPhoneNumberEqual("650-253-0000", "6502530000");
        assertPhoneNumberEqual("650-253-0000", "650 253 0000");
        assertPhoneNumberEqual("650 253 0000", "6502530000");
        assertPhoneNumberEqual("+1 650-253-0000", "6502530000");
        assertPhoneNumberEqual("001 650-253-0000", "6502530000");
        assertPhoneNumberEqual("0111 650-253-0000", "6502530000");
        assertPhoneNumberEqual("+79161234567", "89161234567");
        assertPhoneNumberEqual("+33123456789", "0123456789");
        assertPhoneNumberEqual("+31771234567", "0771234567");
        assertPhoneNumberEqual("+66811234567", "166811234567");
        assertPhoneNumberNotEqual("+33123456789", "+1123456789");
        assertPhoneNumberEqual("5125551212", "+15125551212");
        assertPhoneNumberNotEqual("5125551212", "6505551212");
        assertPhoneNumberEqual("090-1234-5678", "+819012345678");
        assertPhoneNumberEqual("090(1234)5678", "+819012345678");
        assertPhoneNumberEqual("090-1234-5678", "+81-90-1234-5678");
        assertPhoneNumberEqual("+593(800)123-1234", "8001231234");
        assertPhoneNumberEqual("+593-2-1234-123", "21234123");
        assertPhoneNumberEqual("008001231234", "8001231234", false);
        assertPhoneNumberNotEqual("008001231234", "8001231234", true);
        assertPhoneNumberEqual("080-1234-5678", "+819012345678", false);
        assertPhoneNumberNotEqual("080-1234-5678", "+819012345678", true);
    }

    @MediumTest
    public void testCopyString() throws Exception {
        mDatabase.execSQL("CREATE TABLE guess (numi INTEGER, numf FLOAT, str TEXT);");
        mDatabase.execSQL("INSERT INTO guess (numi,numf,str) VALUES (0,0.0,'ZoomZoomZoomZoom');");
        mDatabase.execSQL("INSERT INTO guess (numi,numf,str) VALUES (2000000000,3.1415926535,'');");
        String chinese = "京仅 尽径惊";
        String[] arr = new String[1];
        arr[0] = chinese;
        mDatabase.execSQL("INSERT INTO guess (numi,numf,str) VALUES (-32768,-1.0,?)", arr);
        Cursor c;
        c = mDatabase.rawQuery("SELECT * FROM guess", null);
        c.moveToFirst();
        CharArrayBuffer buf = new CharArrayBuffer(14);
        String compareTo = c.getString(c.getColumnIndexOrThrow("numi"));
        int numiIdx = c.getColumnIndexOrThrow("numi");
        int numfIdx = c.getColumnIndexOrThrow("numf");
        int strIdx = c.getColumnIndexOrThrow("str");
        c.copyStringToBuffer(numiIdx, buf);
        assertEquals(1, buf.sizeCopied);
        assertEquals(compareTo, new String(buf.data, 0, buf.sizeCopied));
        c.copyStringToBuffer(strIdx, buf);
        assertEquals("ZoomZoomZoomZoom", new String(buf.data, 0, buf.sizeCopied));
        c.moveToNext();
        compareTo = c.getString(numfIdx);
        c.copyStringToBuffer(numfIdx, buf);
        assertEquals(compareTo, new String(buf.data, 0, buf.sizeCopied));
        c.copyStringToBuffer(strIdx, buf);
        assertEquals(0, buf.sizeCopied);
        c.moveToNext();
        c.copyStringToBuffer(numfIdx, buf);
        assertEquals(-1.0, Double.valueOf(new String(buf.data, 0, buf.sizeCopied)).doubleValue());
        c.copyStringToBuffer(strIdx, buf);
        compareTo = c.getString(strIdx);
        assertEquals(chinese, compareTo);
        assertEquals(chinese, new String(buf.data, 0, buf.sizeCopied));
        c.close();
    }

    @MediumTest
    public void testSchemaChange1() throws Exception {
        SQLiteDatabase db1 = mDatabase;
        Cursor cursor;
        db1.execSQL("CREATE TABLE db1 (_id INTEGER PRIMARY KEY, data TEXT);");
        cursor = db1.query("db1", null, null, null, null, null, null);
        assertNotNull("Cursor is null", cursor);
        db1.execSQL("CREATE TABLE db2 (_id INTEGER PRIMARY KEY, data TEXT);");
        assertEquals(0, cursor.getCount());
        cursor.deactivate();
    }

    @MediumTest
    public void testSchemaChange2() throws Exception {
        SQLiteDatabase db1 = mDatabase;
        SQLiteDatabase db2 = SQLiteDatabase.openOrCreateDatabase(mDatabaseFile, null);
        Cursor cursor;
        db1.execSQL("CREATE TABLE db1 (_id INTEGER PRIMARY KEY, data TEXT);");
        cursor = db1.query("db1", null, null, null, null, null, null);
        assertNotNull("Cursor is null", cursor);
        assertEquals(0, cursor.getCount());
        cursor.deactivate();
    }

    @MediumTest
    public void testSchemaChange3() throws Exception {
        SQLiteDatabase db1 = mDatabase;
        SQLiteDatabase db2 = SQLiteDatabase.openOrCreateDatabase(mDatabaseFile, null);
        Cursor cursor;
        db1.execSQL("CREATE TABLE db1 (_id INTEGER PRIMARY KEY, data TEXT);");
        db1.execSQL("INSERT INTO db1 (data) VALUES ('test');");
        cursor = db1.query("db1", null, null, null, null, null, null);
    }

    private class ChangeObserver extends ContentObserver {

        private int mCursorNotificationCount = 0;

        private int mNotificationCount = 0;

        public int getCursorNotificationCount() {
            return mCursorNotificationCount;
        }

        public int getNotificationCount() {
            return mNotificationCount;
        }

        public ChangeObserver(boolean cursor) {
            super(new Handler());
            mCursor = cursor;
        }

        @Override
        public boolean deliverSelfNotifications() {
            return true;
        }

        @Override
        public void onChange(boolean selfChange) {
            if (mCursor) {
                mCursorNotificationCount++;
            } else {
                mNotificationCount++;
            }
        }

        boolean mCursor;
    }

    @MediumTest
    public void testNotificationTest1() throws Exception {
    }

    @MediumTest
    public void testSelectionArgs() throws Exception {
        mDatabase.execSQL("CREATE TABLE test (_id INTEGER PRIMARY KEY, data TEXT);");
        ContentValues values = new ContentValues(1);
        values.put("data", "don't forget to handled 's");
        mDatabase.insert("test", "data", values);
        values.clear();
        values.put("data", "no apostrophes here");
        mDatabase.insert("test", "data", values);
        Cursor c = mDatabase.query("test", null, "data GLOB ?", new String[] { "*'*" }, null, null, null);
        assertEquals(1, c.getCount());
        assertTrue(c.moveToFirst());
        assertEquals("don't forget to handled 's", c.getString(1));
        c.deactivate();
        try {
            mDatabase.query("test", new String[] { "_id" }, "_id=?", new String[] { null }, null, null, null);
            fail("expected exception not thrown");
        } catch (IllegalArgumentException e) {
        }
    }

    @MediumTest
    public void testTokenize() throws Exception {
        Cursor c;
        mDatabase.execSQL("CREATE TABLE tokens (" + "token TEXT COLLATE unicode," + "source INTEGER," + "token_index INTEGER," + "tag TEXT" + ");");
        mDatabase.execSQL("CREATE TABLE tokens_no_index (" + "token TEXT COLLATE unicode," + "source INTEGER" + ");");
        Assert.assertEquals(0, DatabaseUtils.longForQuery(mDatabase, "SELECT _TOKENIZE(NULL, NULL, NULL, NULL)", null));
        Assert.assertEquals(0, DatabaseUtils.longForQuery(mDatabase, "SELECT _TOKENIZE('tokens', NULL, NULL, NULL)", null));
        Assert.assertEquals(0, DatabaseUtils.longForQuery(mDatabase, "SELECT _TOKENIZE('tokens', 10, NULL, NULL)", null));
        Assert.assertEquals(0, DatabaseUtils.longForQuery(mDatabase, "SELECT _TOKENIZE('tokens', 10, 'some string', NULL)", null));
        Assert.assertEquals(3, DatabaseUtils.longForQuery(mDatabase, "SELECT _TOKENIZE('tokens', 11, 'some string ok', ' ', 1, 'foo')", null));
        Assert.assertEquals(2, DatabaseUtils.longForQuery(mDatabase, "SELECT _TOKENIZE('tokens', 11, 'second field', ' ', 1, 'bar')", null));
        Assert.assertEquals(3, DatabaseUtils.longForQuery(mDatabase, "SELECT _TOKENIZE('tokens_no_index', 20, 'some string ok', ' ')", null));
        Assert.assertEquals(3, DatabaseUtils.longForQuery(mDatabase, "SELECT _TOKENIZE('tokens_no_index', 21, 'foo bar baz', ' ', 0)", null));
        String chinese = new String("京仅 尽径惊");
        Assert.assertEquals(2, DatabaseUtils.longForQuery(mDatabase, "SELECT _TOKENIZE('tokens', 12,'" + chinese + "', ' ', 1)", null));
        String icustr = new String("Frédéric Hjønnevåg");
        Assert.assertEquals(2, DatabaseUtils.longForQuery(mDatabase, "SELECT _TOKENIZE('tokens', 13, '" + icustr + "', ' ', 1)", null));
        Assert.assertEquals(9, DatabaseUtils.longForQuery(mDatabase, "SELECT count(*) from tokens;", null));
        String key = DatabaseUtils.getHexCollationKey("Frederic Hjonneva");
        Assert.assertEquals(1, DatabaseUtils.longForQuery(mDatabase, "SELECT count(*) from tokens where token GLOB '" + key + "*'", null));
        Assert.assertEquals(13, DatabaseUtils.longForQuery(mDatabase, "SELECT source from tokens where token GLOB '" + key + "*'", null));
        Assert.assertEquals(0, DatabaseUtils.longForQuery(mDatabase, "SELECT token_index from tokens where token GLOB '" + key + "*'", null));
        key = DatabaseUtils.getHexCollationKey("Hjonneva");
        Assert.assertEquals(1, DatabaseUtils.longForQuery(mDatabase, "SELECT count(*) from tokens where token GLOB '" + key + "*'", null));
        Assert.assertEquals(13, DatabaseUtils.longForQuery(mDatabase, "SELECT source from tokens where token GLOB '" + key + "*'", null));
        Assert.assertEquals(1, DatabaseUtils.longForQuery(mDatabase, "SELECT token_index from tokens where token GLOB '" + key + "*'", null));
        key = DatabaseUtils.getHexCollationKey("some string ok");
        Assert.assertEquals(1, DatabaseUtils.longForQuery(mDatabase, "SELECT count(*) from tokens where token GLOB '" + key + "*'", null));
        Assert.assertEquals(11, DatabaseUtils.longForQuery(mDatabase, "SELECT source from tokens where token GLOB '" + key + "*'", null));
        Assert.assertEquals(0, DatabaseUtils.longForQuery(mDatabase, "SELECT token_index from tokens where token GLOB '" + key + "*'", null));
        Assert.assertEquals("foo", DatabaseUtils.stringForQuery(mDatabase, "SELECT tag from tokens where token GLOB '" + key + "*'", null));
        key = DatabaseUtils.getHexCollationKey("string");
        Assert.assertEquals(1, DatabaseUtils.longForQuery(mDatabase, "SELECT count(*) from tokens where token GLOB '" + key + "*'", null));
        Assert.assertEquals(11, DatabaseUtils.longForQuery(mDatabase, "SELECT source from tokens where token GLOB '" + key + "*'", null));
        Assert.assertEquals(1, DatabaseUtils.longForQuery(mDatabase, "SELECT token_index from tokens where token GLOB '" + key + "*'", null));
        Assert.assertEquals("foo", DatabaseUtils.stringForQuery(mDatabase, "SELECT tag from tokens where token GLOB '" + key + "*'", null));
        key = DatabaseUtils.getHexCollationKey("ok");
        Assert.assertEquals(1, DatabaseUtils.longForQuery(mDatabase, "SELECT count(*) from tokens where token GLOB '" + key + "*'", null));
        Assert.assertEquals(11, DatabaseUtils.longForQuery(mDatabase, "SELECT source from tokens where token GLOB '" + key + "*'", null));
        Assert.assertEquals(2, DatabaseUtils.longForQuery(mDatabase, "SELECT token_index from tokens where token GLOB '" + key + "*'", null));
        Assert.assertEquals("foo", DatabaseUtils.stringForQuery(mDatabase, "SELECT tag from tokens where token GLOB '" + key + "*'", null));
        key = DatabaseUtils.getHexCollationKey("second field");
        Assert.assertEquals(1, DatabaseUtils.longForQuery(mDatabase, "SELECT count(*) from tokens where token GLOB '" + key + "*'", null));
        Assert.assertEquals(11, DatabaseUtils.longForQuery(mDatabase, "SELECT source from tokens where token GLOB '" + key + "*'", null));
        Assert.assertEquals(0, DatabaseUtils.longForQuery(mDatabase, "SELECT token_index from tokens where token GLOB '" + key + "*'", null));
        Assert.assertEquals("bar", DatabaseUtils.stringForQuery(mDatabase, "SELECT tag from tokens where token GLOB '" + key + "*'", null));
        key = DatabaseUtils.getHexCollationKey("field");
        Assert.assertEquals(1, DatabaseUtils.longForQuery(mDatabase, "SELECT count(*) from tokens where token GLOB '" + key + "*'", null));
        Assert.assertEquals(11, DatabaseUtils.longForQuery(mDatabase, "SELECT source from tokens where token GLOB '" + key + "*'", null));
        Assert.assertEquals(1, DatabaseUtils.longForQuery(mDatabase, "SELECT token_index from tokens where token GLOB '" + key + "*'", null));
        Assert.assertEquals("bar", DatabaseUtils.stringForQuery(mDatabase, "SELECT tag from tokens where token GLOB '" + key + "*'", null));
        key = DatabaseUtils.getHexCollationKey(chinese);
        String[] a = new String[1];
        a[0] = key;
        Assert.assertEquals(1, DatabaseUtils.longForQuery(mDatabase, "SELECT count(*) from tokens where token= ?", a));
        Assert.assertEquals(12, DatabaseUtils.longForQuery(mDatabase, "SELECT source from tokens where token= ?", a));
        Assert.assertEquals(0, DatabaseUtils.longForQuery(mDatabase, "SELECT token_index from tokens where token= ?", a));
        a[0] += "*";
        Assert.assertEquals(1, DatabaseUtils.longForQuery(mDatabase, "SELECT count(*) from tokens where token GLOB ?", a));
        Assert.assertEquals(12, DatabaseUtils.longForQuery(mDatabase, "SELECT source from tokens where token GLOB ?", a));
        Assert.assertEquals(0, DatabaseUtils.longForQuery(mDatabase, "SELECT token_index from tokens where token GLOB ?", a));
        Assert.assertEquals(1, DatabaseUtils.longForQuery(mDatabase, "SELECT count(*) from tokens where token= '" + key + "'", null));
        Assert.assertEquals(12, DatabaseUtils.longForQuery(mDatabase, "SELECT source from tokens where token= '" + key + "'", null));
        Assert.assertEquals(0, DatabaseUtils.longForQuery(mDatabase, "SELECT token_index from tokens where token= '" + key + "'", null));
        Assert.assertEquals(1, DatabaseUtils.longForQuery(mDatabase, "SELECT count(*) from tokens where token GLOB '" + key + "*'", null));
        Assert.assertEquals(12, DatabaseUtils.longForQuery(mDatabase, "SELECT source from tokens where token GLOB '" + key + "*'", null));
        Assert.assertEquals(0, DatabaseUtils.longForQuery(mDatabase, "SELECT token_index from tokens where token GLOB '" + key + "*'", null));
        key = DatabaseUtils.getHexCollationKey("京仅");
        Assert.assertEquals(1, DatabaseUtils.longForQuery(mDatabase, "SELECT count(*) from tokens where token GLOB '" + key + "*'", null));
        Assert.assertEquals(12, DatabaseUtils.longForQuery(mDatabase, "SELECT source from tokens where token GLOB '" + key + "*'", null));
        Assert.assertEquals(0, DatabaseUtils.longForQuery(mDatabase, "SELECT token_index from tokens where token GLOB '" + key + "*'", null));
        key = DatabaseUtils.getHexCollationKey("尽径惊");
        Log.d("DatabaseGeneralTest", "key = " + key);
        Assert.assertEquals(1, DatabaseUtils.longForQuery(mDatabase, "SELECT count(*) from tokens where token GLOB '" + key + "*'", null));
        Assert.assertEquals(12, DatabaseUtils.longForQuery(mDatabase, "SELECT source from tokens where token GLOB '" + key + "*'", null));
        Assert.assertEquals(1, DatabaseUtils.longForQuery(mDatabase, "SELECT token_index from tokens where token GLOB '" + key + "*'", null));
        Assert.assertEquals(0, DatabaseUtils.longForQuery(mDatabase, "SELECT count(*) from tokens where token GLOB 'ab*'", null));
        key = DatabaseUtils.getHexCollationKey("some string ok");
        Assert.assertEquals(1, DatabaseUtils.longForQuery(mDatabase, "SELECT count(*) from tokens_no_index where token GLOB '" + key + "*'", null));
        Assert.assertEquals(20, DatabaseUtils.longForQuery(mDatabase, "SELECT source from tokens_no_index where token GLOB '" + key + "*'", null));
        key = DatabaseUtils.getHexCollationKey("bar");
        Assert.assertEquals(1, DatabaseUtils.longForQuery(mDatabase, "SELECT count(*) from tokens_no_index where token GLOB '" + key + "*'", null));
        Assert.assertEquals(21, DatabaseUtils.longForQuery(mDatabase, "SELECT source from tokens_no_index where token GLOB '" + key + "*'", null));
    }

    @MediumTest
    public void testTransactions() throws Exception {
        mDatabase.execSQL("CREATE TABLE test (num INTEGER);");
        mDatabase.execSQL("INSERT INTO test (num) VALUES (0)");
        setNum(1);
        checkNum(1);
        setNum(0);
        mDatabase.beginTransaction();
        setNum(1);
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
        checkNum(1);
        Assert.assertFalse(mDatabase.isDbLockedByCurrentThread());
        setNum(0);
        mDatabase.beginTransaction();
        setNum(1);
        mDatabase.endTransaction();
        checkNum(0);
        Assert.assertFalse(mDatabase.isDbLockedByCurrentThread());
        assertThrowsIllegalState(new Runnable() {

            public void run() {
                mDatabase.endTransaction();
            }
        });
        assertThrowsIllegalState(new Runnable() {

            public void run() {
                mDatabase.setTransactionSuccessful();
            }
        });
        mDatabase.beginTransaction();
        mDatabase.setTransactionSuccessful();
        assertThrowsIllegalState(new Runnable() {

            public void run() {
                mDatabase.setTransactionSuccessful();
            }
        });
        assertThrowsIllegalState(new Runnable() {

            public void run() {
                mDatabase.beginTransaction();
            }
        });
        mDatabase.endTransaction();
        Assert.assertFalse(mDatabase.isDbLockedByCurrentThread());
        setNum(0);
        mDatabase.beginTransaction();
        mDatabase.beginTransaction();
        setNum(1);
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
        checkNum(1);
        Assert.assertFalse(mDatabase.isDbLockedByCurrentThread());
        setNum(0);
        mDatabase.beginTransaction();
        mDatabase.beginTransaction();
        setNum(1);
        mDatabase.endTransaction();
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
        checkNum(0);
        Assert.assertFalse(mDatabase.isDbLockedByCurrentThread());
        setNum(0);
        mDatabase.beginTransaction();
        mDatabase.beginTransaction();
        setNum(1);
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
        mDatabase.endTransaction();
        checkNum(0);
        Assert.assertFalse(mDatabase.isDbLockedByCurrentThread());
    }

    private void setNum(int num) {
        mDatabase.execSQL("UPDATE test SET num = " + num);
    }

    private void checkNum(int num) {
        Assert.assertEquals(num, DatabaseUtils.longForQuery(mDatabase, "SELECT num FROM test", null));
    }

    private void assertThrowsIllegalState(Runnable r) {
        boolean ok = false;
        try {
            r.run();
        } catch (IllegalStateException e) {
            ok = true;
        }
        Assert.assertTrue(ok);
    }

    public void xxtestMem1() throws Exception {
        populateDefaultTable();
        for (int i = 0; i < 50000; i++) {
            Cursor cursor = mDatabase.query("test", null, null, null, null, null, null);
            cursor.moveToFirst();
            cursor.close();
        }
    }

    public void xxtestMem2() throws Exception {
        populateDefaultTable();
        for (int i = 0; i < 50000; i++) {
            Cursor cursor = mDatabase.query("test", null, null, null, null, null, null);
            cursor.close();
        }
    }

    public void xxtestMem3() throws Exception {
        populateDefaultTable();
        for (int i = 0; i < 50000; i++) {
            Cursor cursor = mDatabase.query("test", null, null, null, null, null, null);
            cursor.deactivate();
        }
    }

    @MediumTest
    public void testContentValues() throws Exception {
        ContentValues values = new ContentValues();
        values.put("string", "value");
        assertEquals("value", values.getAsString("string"));
        byte[] bytes = new byte[42];
        Arrays.fill(bytes, (byte) 0x28);
        values.put("byteArray", bytes);
        assertTrue(Arrays.equals(bytes, values.getAsByteArray("byteArray")));
        Parcel p = Parcel.obtain();
        values.writeToParcel(p, 0);
        p.setDataPosition(0);
        values = ContentValues.CREATOR.createFromParcel(p);
        assertTrue(Arrays.equals(bytes, values.getAsByteArray("byteArray")));
        assertEquals("value", values.get("string"));
    }

    @MediumTest
    public void testTableInfoPragma() throws Exception {
        mDatabase.execSQL("CREATE TABLE pragma_test (" + "i INTEGER DEFAULT 1234, " + "j INTEGER, " + "s TEXT DEFAULT 'hello', " + "t TEXT, " + "'select' TEXT DEFAULT \"hello\")");
        try {
            Cursor cur = mDatabase.rawQuery("PRAGMA table_info(pragma_test)", null);
            Assert.assertEquals(5, cur.getCount());
            Assert.assertTrue(cur.moveToNext());
            Assert.assertEquals("i", cur.getString(TABLE_INFO_PRAGMA_COLUMNNAME_INDEX));
            Assert.assertEquals("1234", cur.getString(TABLE_INFO_PRAGMA_DEFAULT_INDEX));
            Assert.assertTrue(cur.moveToNext());
            Assert.assertEquals("j", cur.getString(TABLE_INFO_PRAGMA_COLUMNNAME_INDEX));
            Assert.assertNull(cur.getString(TABLE_INFO_PRAGMA_DEFAULT_INDEX));
            Assert.assertTrue(cur.moveToNext());
            Assert.assertEquals("s", cur.getString(TABLE_INFO_PRAGMA_COLUMNNAME_INDEX));
            Assert.assertEquals("'hello'", cur.getString(TABLE_INFO_PRAGMA_DEFAULT_INDEX));
            Assert.assertTrue(cur.moveToNext());
            Assert.assertEquals("t", cur.getString(TABLE_INFO_PRAGMA_COLUMNNAME_INDEX));
            Assert.assertNull(cur.getString(TABLE_INFO_PRAGMA_DEFAULT_INDEX));
            Assert.assertTrue(cur.moveToNext());
            Assert.assertEquals("select", cur.getString(TABLE_INFO_PRAGMA_COLUMNNAME_INDEX));
            Assert.assertEquals("\"hello\"", cur.getString(TABLE_INFO_PRAGMA_DEFAULT_INDEX));
            cur.close();
        } catch (Throwable t) {
            throw new RuntimeException("If you see this test fail, it's likely that something about " + "sqlite's PRAGMA table_info(...) command has changed.", t);
        }
    }

    @MediumTest
    public void testInsertHelper() throws Exception {
        Cursor cur;
        ContentValues cv;
        long row;
        mDatabase.execSQL("CREATE TABLE insert_test (" + "_id INTEGER PRIMARY KEY, " + "s TEXT NOT NULL UNIQUE, " + "t TEXT NOT NULL DEFAULT 'hello world', " + "i INTEGER, " + "j INTEGER NOT NULL DEFAULT 1234, " + "'select' TEXT)");
        DatabaseUtils.InsertHelper ih = new DatabaseUtils.InsertHelper(mDatabase, "insert_test");
        cv = new ContentValues();
        cv.put("s", "one");
        row = ih.insert(cv);
        cur = mDatabase.rawQuery("SELECT * FROM insert_test WHERE _id == " + row, null);
        Assert.assertTrue(cur.moveToFirst());
        Assert.assertEquals("one", cur.getString(1));
        Assert.assertEquals("hello world", cur.getString(2));
        Assert.assertNull(cur.getString(3));
        Assert.assertEquals(1234, cur.getLong(4));
        Assert.assertNull(cur.getString(5));
        cv = new ContentValues();
        cv.put("s", "two");
        cv.put("t", "goodbye world");
        row = ih.insert(cv);
        cur = mDatabase.rawQuery("SELECT * FROM insert_test WHERE _id == " + row, null);
        Assert.assertTrue(cur.moveToFirst());
        Assert.assertEquals("two", cur.getString(1));
        Assert.assertEquals("goodbye world", cur.getString(2));
        Assert.assertNull(cur.getString(3));
        Assert.assertEquals(1234, cur.getLong(4));
        Assert.assertNull(cur.getString(5));
        cv = new ContentValues();
        cv.put("t", "goodbye world");
        row = ih.insert(cv);
        Assert.assertEquals(-1, row);
        cv = new ContentValues();
        cv.put("s", "three");
        cv.put("i", 2345);
        cv.put("j", 3456);
        cv.put("select", "tricky");
        row = ih.insert(cv);
        cur = mDatabase.rawQuery("SELECT * FROM insert_test WHERE _id == " + row, null);
        Assert.assertTrue(cur.moveToFirst());
        Assert.assertEquals("three", cur.getString(1));
        Assert.assertEquals("hello world", cur.getString(2));
        Assert.assertEquals(2345, cur.getLong(3));
        Assert.assertEquals(3456, cur.getLong(4));
        Assert.assertEquals("tricky", cur.getString(5));
        cv = new ContentValues();
        cv.put("s", "three");
        cv.put("i", 6789);
        row = ih.insert(cv);
        Assert.assertEquals(-1, row);
        row = ih.replace(cv);
        cur = mDatabase.rawQuery("SELECT * FROM insert_test WHERE _id == " + row, null);
        Assert.assertTrue(cur.moveToFirst());
        Assert.assertEquals("three", cur.getString(1));
        Assert.assertEquals("hello world", cur.getString(2));
        Assert.assertEquals(6789, cur.getLong(3));
        ih.close();
    }
}
