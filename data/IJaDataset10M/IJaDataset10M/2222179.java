package org.nomadpim.core.util.date;

import junit.framework.TestCase;
import org.joda.time.DateTime;

public class TimeUnitEqualDatesTest extends TestCase {

    private void assertEquals(boolean expected, TimeUnit timeUnit, DateTime date1, DateTime date2) {
        assertEquals(timeUnit.toString() + ".areDatesEqual(" + date1 + "," + date2 + ")", expected, timeUnit.areDatesEqual(date1, date2));
    }

    public void testEqualDate() {
        for (TimeUnit timeUnit : TimeUnit.values()) {
            testEqualDate(timeUnit);
        }
    }

    private void testEqualDate(TimeUnit timeUnit) {
        DateTime date = new DateTime();
        DateTime date2 = new DateTime(date.getMillis());
        assertEquals(true, timeUnit, date, date2);
    }

    public void testEqualDateInTimeUnit() {
        for (TimeUnit timeUnit : TimeUnit.values()) {
            testEqualDateInTimeUnit(timeUnit);
        }
    }

    private void testEqualDateInTimeUnit(TimeUnit timeUnit) {
        DateTime date = new DateTime(2000, 2, 2, 2, 2, 2, 2);
        TimeInterval interval = timeUnit.getIntervalContaining(date);
        DateTime intervalStart = interval.getStart();
        DateTime intervalEnd = interval.getEnd();
        DateTime endTime = intervalEnd.minusMillis(1);
        DateTime beforeTime = intervalStart.minusMillis(1);
        assertEquals(true, timeUnit, date, intervalStart);
        assertEquals(true, timeUnit, date, endTime);
        assertEquals(true, timeUnit, intervalStart, endTime);
        assertEquals(false, timeUnit, intervalEnd, date);
        assertEquals(false, timeUnit, intervalEnd, intervalStart);
        assertEquals(false, timeUnit, intervalEnd, endTime);
        assertEquals(false, timeUnit, beforeTime, date);
        assertEquals(false, timeUnit, beforeTime, intervalStart);
        assertEquals(false, timeUnit, beforeTime, endTime);
    }

    public void testSameDate() {
        for (TimeUnit timeUnit : TimeUnit.values()) {
            testSameDate(timeUnit);
        }
    }

    private void testSameDate(TimeUnit timeUnit) {
        DateTime date = new DateTime();
        assertEquals(true, timeUnit, date, date);
    }
}
