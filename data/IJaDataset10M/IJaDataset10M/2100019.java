package org.nomadpim.core.util.date;

import java.util.Locale;
import org.joda.time.DateTime;
import org.junit.Test;

public class TimeIntervalFilterTest extends DateFilterTest {

    @Test
    public void simpleInterval() {
        filter = new TimeIntervalFilter(new TimeInterval(new DateTime(1000), new DateTime(10000)));
        assertReject(500);
        assertReject(999);
        assertAccept(1000);
        assertAccept(1001);
        assertAccept(5000);
        assertAccept(9999);
        assertReject(10000);
        assertReject(15000);
    }

    @Test
    public void testEvaluateOnDayInterval() {
        DateTime filterDate = new DateTime(2006, 2, 5, 12, 0, 0, 0);
        filter = new TimeIntervalFilter(filterDate, TimeUnit.DAY);
        assertReject(2006, 2, 4, 23, 59, 0, 0);
        assertAccept(2006, 2, 5, 0, 0, 0, 0);
        assertAccept(2006, 2, 5, 13, 0, 0, 0);
        assertAccept(2006, 2, 5, 23, 59, 59, 999);
        assertReject(2006, 2, 6, 0, 0, 0, 0);
    }

    @Test
    public void testEvaluateOnMonthInterval() {
        DateTime filterDate = new DateTime(2006, 2, 5, 12, 0, 0, 0);
        filter = new TimeIntervalFilter(filterDate, TimeUnit.MONTH);
        assertReject(2006, 1, 31, 23, 59, 0, 0);
        assertAccept(2006, 2, 1, 0, 0, 0, 0);
        assertAccept(2006, 2, 14, 0, 0, 0, 0);
        assertAccept(2006, 2, 28, 23, 59, 59, 999);
        assertReject(2006, 3, 1, 0, 0, 0, 0);
    }

    @Test
    public void testEvaluateOnWeekIntervalGermany() {
        DateTime filterDate = new DateTime(2006, 2, 5, 12, 12, 3, 12);
        filter = new TimeIntervalFilter(filterDate, new TimeUnit.Week() {

            @Override
            Locale getLocale() {
                return Locale.GERMANY;
            }
        });
        assertReject(2006, 1, 29, 23, 59, 0, 0);
        assertAccept(2006, 1, 30, 0, 0, 0, 0);
        assertAccept(2006, 2, 2, 0, 0, 0, 0);
        assertAccept(2006, 2, 5, 23, 59, 59, 999);
        assertReject(2006, 2, 6, 0, 0, 0, 0);
    }

    @Test
    public void testEvaluateOnWeekIntervalUS() {
        DateTime filterDate = new DateTime(2006, 2, 5, 12, 12, 3, 12);
        filter = new TimeIntervalFilter(filterDate, new TimeUnit.Week() {

            @Override
            Locale getLocale() {
                return Locale.US;
            }
        });
        assertReject(2006, 2, 4, 23, 59, 0, 0);
        assertAccept(2006, 2, 5, 0, 0, 0, 0);
        assertAccept(2006, 2, 8, 0, 0, 0, 0);
        assertAccept(2006, 2, 11, 23, 59, 59, 999);
        assertReject(2006, 2, 12, 0, 0, 0, 0);
    }
}
