package org.sgodden.echo.ext20;

import java.util.Calendar;
import junit.framework.TestCase;

/**
 * Unit tests for the DateField component
 * @author Lloyd Colling
 *
 */
public class DateFieldTest extends TestCase {

    /**
     * Tests that when the client clears out the value from a Date Field,
     * that it's value is null.
     * @throws Exception
     */
    public void testClientClearingDateField() throws Exception {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MINUTE, 60 - c.get(Calendar.MINUTE));
        DateField df = new DateField(c);
        df.setAllowBlank(true);
        assertNotNull("Date Field is not using the passed calendar", df.getCalendar());
        assertEquals("Date Field value is not the value we originally passed", df.getCalendar().getTimeInMillis(), c.getTimeInMillis());
        df.processInput(DateField.PROPERTY_DATE_CHANGED, null);
        assertNull("Date Field is not setting it's value to null when the client passes null as it's input", df.getCalendar());
    }
}
