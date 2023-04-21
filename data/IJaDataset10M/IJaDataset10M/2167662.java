package android.database.sqlite.cts;

import android.database.sqlite.SQLiteFullException;
import android.test.AndroidTestCase;
import dalvik.annotation.TestTargets;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;

@TestTargetClass(android.database.sqlite.SQLiteFullException.class)
public class SQLiteFullExceptionTest extends AndroidTestCase {

    @TestTargets({ @TestTargetNew(level = TestLevel.COMPLETE, notes = "Test constructor", method = "SQLiteFullException", args = {  }), @TestTargetNew(level = TestLevel.COMPLETE, notes = "Test constructor", method = "SQLiteFullException", args = { java.lang.String.class }) })
    public void testConstructor() {
        new SQLiteFullException();
        new SQLiteFullException("error");
    }
}
