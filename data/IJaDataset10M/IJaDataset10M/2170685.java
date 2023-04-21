package junit.wdc.utils;

import junit.framework.*;
import java.util.*;
import wdc.utils.WDCDay;

public class WDCDayTest extends TestCase {

    /**
	 * 
	 * The Java Calendar class uses 0-basd indexing, so need to ensure
	 * the WDCDay class plays nice. The goal of this test is to monitor 
	 * consistency over time to ensure the two different time/date code bases
	 * don't deviate down the road, since a number of pieces of code make
	 * assumptions around current specifications.
	 * 
	 */
    public void testCalendarWDCDayInteraction() {
        WDCDay wNorm = null;
        WDCDay wLeap = null;
        try {
            Calendar norm = Calendar.getInstance();
            norm.set(Calendar.YEAR, 2001);
            norm.set(Calendar.MONTH, 1);
            norm.set(Calendar.DATE, 1);
            wNorm = new WDCDay(norm.get(Calendar.YEAR), norm.get(Calendar.MONTH) - 1, norm.getActualMaximum(Calendar.DAY_OF_MONTH));
            Calendar leap = Calendar.getInstance();
            leap.set(Calendar.YEAR, 2000);
            leap.set(Calendar.MONTH, 1);
            leap.set(Calendar.DATE, 1);
            wLeap = new WDCDay(leap.get(Calendar.YEAR), leap.get(Calendar.MONTH) - 1, leap.getActualMaximum(Calendar.DAY_OF_MONTH));
        } catch (Exception x) {
            x.printStackTrace();
        } finally {
            assertTrue("FAIL: normal year Calendar inconsitency", ((wNorm != null) && wNorm.getDay() == 28));
            assertTrue("FAIL: leap year Calendar inconsitency", ((wLeap != null) && wLeap.getDay() == 29));
        }
    }
}
