package android.graphics.cts;

import android.graphics.Path;
import android.graphics.Path.Direction;
import android.test.AndroidTestCase;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;

@TestTargetClass(Path.Direction.class)
public class Path_DirectionTest extends AndroidTestCase {

    @TestTargetNew(level = TestLevel.COMPLETE, method = "valueOf", args = { java.lang.String.class })
    public void testValueOf() {
        assertEquals(Direction.CW, Direction.valueOf("CW"));
        assertEquals(Direction.CCW, Direction.valueOf("CCW"));
    }

    @TestTargetNew(level = TestLevel.COMPLETE, method = "values", args = {  })
    public void testValues() {
        Direction[] expected = { Direction.CW, Direction.CCW };
        Direction[] actual = Direction.values();
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < actual.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }
}
