package tests.api.java.math;

import dalvik.annotation.TestTargets;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargetClass;
import java.math.BigDecimal;
import java.math.RoundingMode;

@TestTargetClass(RoundingMode.class)
public class RoundingModeTest extends junit.framework.TestCase {

    /**
     * @tests java.math.RoundingMode#valueOf(int)
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "valueOf", args = { int.class })
    public void test_valueOfI() {
        assertEquals("valueOf failed for ROUND_CEILING", RoundingMode.valueOf(BigDecimal.ROUND_CEILING), RoundingMode.CEILING);
        assertEquals("valueOf failed for ROUND_DOWN", RoundingMode.valueOf(BigDecimal.ROUND_DOWN), RoundingMode.DOWN);
        assertEquals("valueOf failed for ROUND_FLOOR", RoundingMode.valueOf(BigDecimal.ROUND_FLOOR), RoundingMode.FLOOR);
        assertEquals("valueOf failed for ROUND_HALF_DOWN", RoundingMode.valueOf(BigDecimal.ROUND_HALF_DOWN), RoundingMode.HALF_DOWN);
        assertEquals("valueOf failed for ROUND_HALF_EVEN", RoundingMode.valueOf(BigDecimal.ROUND_HALF_EVEN), RoundingMode.HALF_EVEN);
        assertEquals("valueOf failed for ROUND_HALF_UP", RoundingMode.valueOf(BigDecimal.ROUND_HALF_UP), RoundingMode.HALF_UP);
        assertEquals("valueOf failed for ROUND_UNNECESSARY", RoundingMode.valueOf(BigDecimal.ROUND_UNNECESSARY), RoundingMode.UNNECESSARY);
        assertEquals("valueOf failed for ROUND_UP", RoundingMode.valueOf(BigDecimal.ROUND_UP), RoundingMode.UP);
        try {
            RoundingMode.valueOf(13);
            fail("IllegalArgumentException expected for RoundingMode(13)");
        } catch (IllegalArgumentException e) {
        }
        try {
            RoundingMode.valueOf(-1);
            fail("IllegalArgumentException expected for RoundingMode(-1)");
        } catch (IllegalArgumentException e) {
        }
    }
}
