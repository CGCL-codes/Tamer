package org.timepedia.chronoscope.nongwt;

import junit.framework.TestCase;
import org.timepedia.chronoscope.client.util.date.DateFields;
import org.timepedia.chronoscope.client.util.date.DayOfWeek;
import org.timepedia.chronoscope.client.util.date.EraCalc;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author chad takahashi
 */
public abstract class EraCalcTest extends TestCase {

    protected abstract EraCalc getEraCalc();

    protected abstract boolean isLeapYear(int year);

    private TimeZone origTimeZone;

    /**
   * Verify that the year timestamp calculated by this EraCalc object exactly matches
   * the corresponding calculation by java.util.Date.
   */
    static final void testCalcYearTimestamp(EraCalc eraCalc, int minYear, int maxYear) {
        for (int y = minYear; y <= maxYear; y++) {
            double eraCalcTime = eraCalc.calcYearTimestamp(y);
            assertEquals("y=" + y, javaDate(y).getTime(), (long) eraCalcTime);
        }
    }

    static void testCalcDayOfWeek(EraCalc eraCalc, int minYear, int maxYear) {
        Date javaDate = new Date(0L);
        for (int y = minYear; y <= maxYear; y++) {
            boolean isLeapYear = eraCalc.isLeapYear(y);
            for (int m = 0; m < 12; m++) {
                for (int d = 1; d <= eraCalc.getDaysInMonth(m, isLeapYear); d++) {
                    DayOfWeek dow = eraCalc.calcDayOfWeek(y, m, d);
                    javaDate.setYear(y - 1900);
                    javaDate.setMonth(m);
                    javaDate.setDate(d);
                    DayOfWeek expectedDOW = DayOfWeek.values()[javaDate.getDay()];
                    assertEquals("y=" + y + ";m=" + m + ";d=" + d + ": ", expectedDOW, dow);
                }
            }
        }
    }

    static void testCalcWeekOfYear(EraCalc eraCalc) {
        assertEquals(53, eraCalc.calcWeekOfYear(2005, 0, 1));
        assertEquals(53, eraCalc.calcWeekOfYear(2005, 0, 2));
        assertEquals(52, eraCalc.calcWeekOfYear(2005, 11, 31));
        assertEquals(1, eraCalc.calcWeekOfYear(2007, 0, 1));
        assertEquals(1, eraCalc.calcWeekOfYear(2008, 11, 31));
        assertEquals(53, eraCalc.calcWeekOfYear(2009, 11, 31));
        assertEquals(53, eraCalc.calcWeekOfYear(2010, 0, 3));
        assertEquals(1, eraCalc.calcWeekOfYear(2010, 0, 4));
        assertEquals(52, eraCalc.calcWeekOfYear(2008, 11, 28));
        assertEquals(1, eraCalc.calcWeekOfYear(2008, 11, 29));
        assertEquals(31, eraCalc.calcWeekOfYear(1953, 7, 2));
        assertEquals(6, eraCalc.calcWeekOfYear(2010, 1, 11));
    }

    static void testAssignYearField(EraCalc eraCalc, int minYear, int maxYear) {
        DateFields dateFields = new DateFields();
        for (int y = minYear; y <= maxYear; y++) {
            double ts = javaDate(y, 0, 1).getTime();
            double yearOffsetInMs = eraCalc.calcYearField(ts, dateFields);
            long expectedYearOffset = 0L;
            assertEquals("y=" + y, y, dateFields.year);
            assertEquals("y=" + y, expectedYearOffset, (long) yearOffsetInMs);
            ts = javaDate(y, 0, 1, 0, 3, 41).getTime();
            yearOffsetInMs = eraCalc.calcYearField(ts, dateFields);
            expectedYearOffset = (long) ((60 * 3 + 41) * 1000);
            assertEquals("y=" + y, y, dateFields.year);
            assertEquals("y=" + y, expectedYearOffset, (long) yearOffsetInMs);
            ts = javaDate(y, 0, 1, 4, 3, 41).getTime();
            yearOffsetInMs = eraCalc.calcYearField(ts, dateFields);
            expectedYearOffset = (long) (((60 * 60 * 4) + (60 * 3) + 41) * 1000);
            assertEquals("y=" + y, y, dateFields.year);
            assertEquals("y=" + y, expectedYearOffset, (long) yearOffsetInMs);
        }
    }

    static Date javaDate(int year) {
        return javaDate(year, 0, 1, 0, 0, 0);
    }

    static Date javaDate(int year, int month, int day) {
        return javaDate(year, month, day, 0, 0, 0);
    }

    static Date javaDate(int yr, int mo, int day, int hr, int min, int sec) {
        return new Date(yr - 1900, mo, day, hr, min, sec);
    }

    public final void setUp() {
        origTimeZone = TimeZone.getDefault();
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
    }

    public final void tearDown() {
        TimeZone.setDefault(origTimeZone);
    }
}
