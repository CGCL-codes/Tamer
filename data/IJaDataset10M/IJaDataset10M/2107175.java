package android.graphics.cts;

import android.graphics.Path;
import android.graphics.Path.FillType;
import android.test.AndroidTestCase;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;

@TestTargetClass(Path.FillType.class)
public class Path_FillTypeTest extends AndroidTestCase {

    @TestTargetNew(level = TestLevel.COMPLETE, method = "valueOf", args = { java.lang.String.class })
    public void testValueOf() {
        assertEquals(FillType.WINDING, FillType.valueOf("WINDING"));
        assertEquals(FillType.EVEN_ODD, FillType.valueOf("EVEN_ODD"));
        assertEquals(FillType.INVERSE_WINDING, FillType.valueOf("INVERSE_WINDING"));
        assertEquals(FillType.INVERSE_EVEN_ODD, FillType.valueOf("INVERSE_EVEN_ODD"));
    }

    @TestTargetNew(level = TestLevel.COMPLETE, method = "values", args = {  })
    public void testValues() {
        FillType[] expected = { FillType.WINDING, FillType.EVEN_ODD, FillType.INVERSE_WINDING, FillType.INVERSE_EVEN_ODD };
        FillType[] actual = FillType.values();
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < actual.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }
}
