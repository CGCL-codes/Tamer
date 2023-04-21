package edu.unl.cse.activitygraph;

import static org.junit.Assert.*;
import java.util.Calendar;
import java.util.Date;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import edu.unl.cse.activitygraph.Interval;

public class IntervalTest {

    Interval intvl;

    Calendar cal;

    @Before
    public void setUp() throws Exception {
        cal = Calendar.getInstance();
        Date start, end;
        cal.set(2007, 6, 20, 12, 30, 15);
        start = cal.getTime();
        cal.set(2007, 6, 20, 14, 45, 30);
        end = cal.getTime();
        String note = "testIntervalTest";
        intvl = new Interval(start, end, note);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testIsInterval() {
        assertTrue(intvl.isInterval());
    }

    @Test
    public void testGetEndTime() {
        cal.set(2007, 6, 20, 14, 45, 30);
        assertEquals(cal.getTime(), intvl.getEndTime());
    }

    @Test
    public void testGetStartTime() {
        cal.set(2007, 6, 20, 12, 30, 15);
        assertEquals(cal.getTime(), intvl.getStartTime());
    }

    @Test
    public void testEquals() {
        Interval intvl2;
        Date start, end;
        cal.set(2007, 6, 20, 12, 30, 15);
        start = cal.getTime();
        cal.set(2007, 6, 20, 14, 45, 30);
        end = cal.getTime();
        String note = "testIntervalTest";
        intvl2 = new Interval(start, end, note);
        assertTrue(intvl.equals(intvl2));
    }

    @Test
    public void testNotEquals() {
        Interval intvl2;
        Date start, end;
        cal.set(2007, 6, 20, 12, 30, 15);
        start = cal.getTime();
        cal.set(2007, 6, 21, 12, 34, 30);
        end = cal.getTime();
        String note = "testIntervalTest";
        intvl2 = new Interval(start, end, note);
        assertFalse("These two intervals should not be equal", intvl.equals(intvl2));
    }

    @Test
    public void testGetLengthMin() {
        assertEquals(60 * 2 + 15 + .25, intvl.getLengthMin(), .005);
    }
}
