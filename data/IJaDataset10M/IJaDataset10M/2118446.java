package org.opennms.netmgt.statsd;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import org.opennms.core.utils.TimeKeeper;
import org.opennms.netmgt.statsd.RelativeTime;
import junit.framework.TestCase;

/**
 * @author <a href="mailto:dj@opennms.org">DJ Gregor</a>
 */
public class RelativeTimeTest extends TestCase {

    public void testYesterdayBeginningDST() {
        final TimeZone usEastern = TimeZone.getTimeZone("US/Eastern");
        RelativeTime yesterday = RelativeTime.YESTERDAY;
        yesterday.setTimeKeeper(new TimeKeeper() {

            public Date getCurrentDate() {
                Calendar cal = new GregorianCalendar(usEastern);
                cal.set(2006, Calendar.APRIL, 3, 10, 0, 0);
                return cal.getTime();
            }

            public long getCurrentTime() {
                return getCurrentDate().getTime();
            }
        });
        Date start = yesterday.getStart();
        Date end = yesterday.getEnd();
        assertEquals("start date", "Sun Apr 02 00:00:00 EST 2006", start.toString());
        assertEquals("end date", "Mon Apr 03 00:00:00 EDT 2006", end.toString());
        assertEquals("end date - start date", 82800000, end.getTime() - start.getTime());
    }

    public void testYesterdayEndingDST() {
        final TimeZone usEastern = TimeZone.getTimeZone("US/Eastern");
        RelativeTime yesterday = RelativeTime.YESTERDAY;
        yesterday.setTimeKeeper(new TimeKeeper() {

            public Date getCurrentDate() {
                Calendar cal = new GregorianCalendar(usEastern);
                cal.set(2006, Calendar.OCTOBER, 30, 10, 0, 0);
                return cal.getTime();
            }

            public long getCurrentTime() {
                return getCurrentDate().getTime();
            }
        });
        Date start = yesterday.getStart();
        Date end = yesterday.getEnd();
        assertEquals("start date", "Sun Oct 29 00:00:00 EDT 2006", start.toString());
        assertEquals("end date", "Mon Oct 30 00:00:00 EST 2006", end.toString());
        assertEquals("end date - start date", 90000000, end.getTime() - start.getTime());
    }
}
