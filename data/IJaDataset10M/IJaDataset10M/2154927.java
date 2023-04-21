package tgreiner.amy.common.timer;

import tgreiner.amy.common.timer.QuotaTimeControl;
import junit.framework.TestCase;

public class QuotaTimeControlTest extends TestCase {

    public void testGetSoftLimit() {
        QuotaTimeControl tc = new QuotaTimeControl(60, 300);
        assertEquals(5000, tc.getSoftLimit());
    }

    public void testGetHardLimit() {
        QuotaTimeControl tc = new QuotaTimeControl(60, 300);
        assertEquals(4 * 5000, tc.getHardLimit());
    }

    public void testLimitsDontExceedRemainingTime() {
        QuotaTimeControl tc = new QuotaTimeControl(60, 300);
        final int remaining = 500;
        tc.setRemainingTime(remaining);
        assertTrue(tc.getSoftLimit() <= remaining);
        assertTrue(tc.getHardLimit() <= remaining);
    }
}
