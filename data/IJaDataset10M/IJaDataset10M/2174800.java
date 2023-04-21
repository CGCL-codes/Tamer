package edu.kit.pse.ass.gui.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.junit.Test;

/**
 * The Class BookingInfoModelTest.
 */
public class BookingInfoModelTest {

    /**
	 * Test is duration valid.
	 */
    @Test
    public void testIsDurationValid() {
        BookingInfoModel mod = new BookingInfoModel() {
        };
        mod.setDuration("12:12");
        assertTrue(mod.isDurationValid());
    }

    /**
	 * Test is duration valid error.
	 */
    @Test
    public void testIsDurationValidError() {
        BookingInfoModel mod = new BookingInfoModel() {
        };
        mod.setDuration("12:1d2");
        assertFalse(mod.isDurationValid());
    }

    /**
	 * Test get end.
	 */
    @Test
    public void testGetEnd() {
        BookingInfoModel mod = new BookingInfoModel() {
        };
        Calendar start = new GregorianCalendar(2012, 5, 5, 10, 00);
        mod.setStart(start.getTime());
        mod.setDuration("2:15");
        Date end = mod.getEnd();
        Calendar endCal = new GregorianCalendar();
        endCal.setTime(end);
        start.add(Calendar.MINUTE, 135);
        assertEquals(start, endCal);
    }

    /**
	 * Test set start to now.
	 */
    @Test
    public void testSetStartToNow() {
        BookingInfoModel mod = new BookingInfoModel() {
        };
        Calendar now = Calendar.getInstance();
        mod.setStartToNow();
        Date start = mod.getStart();
        Calendar startCal = new GregorianCalendar();
        startCal.setTime(start);
        assertTrue(now.before(startCal));
        now.add(Calendar.MINUTE, 15 - (now.get(Calendar.MINUTE) % 15));
        assertEquals(0, now.compareTo(startCal));
    }
}
