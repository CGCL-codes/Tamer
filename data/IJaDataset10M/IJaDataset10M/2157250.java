package org.jlib.core.calendar;

import java.util.Calendar;
import java.util.GregorianCalendar;
import org.junit.Test;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * JUnit test suite for TimeInterval. 
 *
 * @author Igor Akkerman
 */
public class TimeIntervalTest {

    private static final Calendar CALENDAR20070101 = new GregorianCalendar(2007, Calendar.JANUARY, 01);

    private static final Calendar CALENDAR20070301 = new GregorianCalendar(2007, Calendar.MARCH, 01);

    private static final Calendar CALENDAR20080101 = new GregorianCalendar(2008, Calendar.JANUARY, 01);

    private static final Calendar CALENDAR20080301 = new GregorianCalendar(2008, Calendar.MARCH, 01);

    private static final Calendar CALENDAR20090101 = new GregorianCalendar(2009, Calendar.JANUARY, 01);

    private static final Calendar CALENDAR20090301 = new GregorianCalendar(2009, Calendar.MARCH, 01);

    private static final TimeInterval ONE_DAY = new TimeInterval(1, 0, 0, 0);

    private static final TimeInterval THREESIXTYFIVE_DAYS = new TimeInterval(365, 0, 0, 0);

    private static final TimeInterval THREESIXTYSIX_DAYS = new TimeInterval(366, 0, 0, 0);

    @Test
    public void test365Days1() {
        assertThat(CALENDAR20080101, is(equalTo(THREESIXTYFIVE_DAYS.addTo(CALENDAR20070101))));
    }

    @Test
    public void test366Days1() {
        assertThat(CALENDAR20080301, is(equalTo(THREESIXTYSIX_DAYS.addTo(CALENDAR20070301))));
    }
}
