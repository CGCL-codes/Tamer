package consciouscode.util;

import java.util.Calendar;
import junit.framework.TestCase;

public class DatesTest extends TestCase {

    public static final String AUTO_SUITES = "common";

    /**
       Document some poorly-documented behavior of the Calendar class.
    */
    public void testCalendar() {
        myCalendar.clear();
        assertEquals(0, myCalendar.get(Calendar.MONTH));
        assertEquals(1, myCalendar.get(Calendar.DAY_OF_MONTH));
    }

    public void testDateIsValid() {
        assertTrue(Dates.dateIsValid(1999, 12, 12, myCalendar));
        assertTrue(Dates.dateIsValid(2002, 12, 12, myCalendar));
        assertTrue(!Dates.dateIsValid(2002, -1, 12, myCalendar));
        assertTrue(!Dates.dateIsValid(2002, 0, 12, myCalendar));
        assertTrue(!Dates.dateIsValid(2002, 13, 12, myCalendar));
        assertTrue(!Dates.dateIsValid(2002, 1, -1, myCalendar));
        assertTrue(!Dates.dateIsValid(2002, 1, 0, myCalendar));
        assertTrue(!Dates.dateIsValid(2002, 1, 32, myCalendar));
        assertTrue(Dates.dateIsValid(2000, 2, 28, myCalendar));
        assertTrue(Dates.dateIsValid(2000, 2, 29, myCalendar));
        assertTrue(!Dates.dateIsValid(2000, 2, 30, myCalendar));
        assertTrue(Dates.dateIsValid(2001, 2, 28, myCalendar));
        assertTrue(!Dates.dateIsValid(2001, 2, 29, myCalendar));
        assertTrue(!Dates.dateIsValid(2001, 2, 30, myCalendar));
    }

    public void testMakeValidDate() {
        assertNull(Dates.makeValidDate(2000, -1, 12, myCalendar));
        assertNotNull(Dates.makeValidDate(2000, 2, 29, myCalendar));
        assertNotNull(Dates.makeValidDate(2001, 2, 28, myCalendar));
        assertNull(Dates.makeValidDate(2000, 2, 30, myCalendar));
        assertNull(Dates.makeValidDate(2001, 2, 29, myCalendar));
    }

    @Override
    protected void setUp() {
        myCalendar = Calendar.getInstance();
    }

    private Calendar myCalendar;
}
