package android.database.cts;

import android.database.CursorIndexOutOfBoundsException;
import android.test.AndroidTestCase;
import dalvik.annotation.TestTargets;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargetClass;

@TestTargetClass(android.database.CursorIndexOutOfBoundsException.class)
public class CursorIndexOutOfBoundsExceptionTest extends AndroidTestCase {

    @TestTargets({ @TestTargetNew(level = TestLevel.COMPLETE, notes = "Test constructors of CursorIndexOutOfBoundsException.", method = "CursorIndexOutOfBoundsException", args = { java.lang.String.class }), @TestTargetNew(level = TestLevel.COMPLETE, notes = "Test constructors of CursorIndexOutOfBoundsException.", method = "CursorIndexOutOfBoundsException", args = { int.class, int.class }) })
    public void testConstructors() {
        int INDEX = 100;
        int SIZE = 99;
        String expected1 = "Expected exception message";
        String expected2 = "Index " + INDEX + " requested, with a size of " + SIZE;
        try {
            throw new CursorIndexOutOfBoundsException(null);
        } catch (CursorIndexOutOfBoundsException e) {
            assertNull(e.getMessage());
        }
        try {
            throw new CursorIndexOutOfBoundsException(expected1);
        } catch (CursorIndexOutOfBoundsException e) {
            assertEquals(expected1, e.getMessage());
        }
        try {
            throw new CursorIndexOutOfBoundsException(INDEX, SIZE);
        } catch (CursorIndexOutOfBoundsException e) {
            assertEquals(expected2, e.getMessage());
        }
    }
}
