package de.bea.domingo.groupware.repeat;

import java.util.Calendar;
import java.util.Locale;
import java.util.SortedSet;
import de.bea.domingo.groupware.Appointment;
import de.bea.domingo.groupware.CalendarEntry;

/**
 * Tests for class {@link MonthlyByDate}.
 *
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
 */
public final class MonthlyByDateTest extends AbstractRepeatIntervalTest {

    /**
     * Test setting and getting weekdays.
     */
    public void testWeekdayRepeats() {
        MonthlyByDate options = new MonthlyByDate();
        options.setRepeats(1, true);
        options.setRepeats(2, true);
        options.setRepeats(3, true);
        options.setRepeats(4, true);
        assertTrue(options.isRepeats(1));
        assertTrue(options.isRepeats(2));
        assertTrue(options.isRepeats(3));
        assertTrue(options.isRepeats(4));
        for (int i = 5; i < 31; i++) {
            assertFalse(options.isRepeats(i));
        }
    }

    /**
     * Test setting and getting weekdays.
     */
    public void testEvents() {
        System.out.println("--------");
        System.out.println("Test events");
        MonthlyByDate options = new MonthlyByDate();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2008, Calendar.JANUARY, 1);
        options.setStartDate(calendar);
        options.setContinuesforMonths(3);
        options.setInterval(1);
        options.setRepeats(1, true);
        options.setRepeats(2, true);
        options.setRepeats(28, true);
        options.setRepeats(29, true);
        options.setRepeats(30, true);
        options.setRepeats(31, true);
        SortedSet events = options.getEvents();
        logEvents(events);
        assertContainsEvent(events, calendar, 2008, Calendar.JANUARY, 1);
        assertContainsEvent(events, calendar, 2008, Calendar.JANUARY, 2);
        assertContainsEvent(events, calendar, 2008, Calendar.JANUARY, 28);
        assertContainsEvent(events, calendar, 2008, Calendar.JANUARY, 29);
        assertContainsEvent(events, calendar, 2008, Calendar.JANUARY, 30);
        assertContainsEvent(events, calendar, 2008, Calendar.JANUARY, 31);
        assertContainsEvent(events, calendar, 2008, Calendar.FEBRUARY, 1);
        assertContainsEvent(events, calendar, 2008, Calendar.FEBRUARY, 2);
        assertContainsEvent(events, calendar, 2008, Calendar.FEBRUARY, 28);
        assertContainsEvent(events, calendar, 2008, Calendar.FEBRUARY, 29);
        assertContainsEvent(events, calendar, 2008, Calendar.MARCH, 1);
        assertContainsEvent(events, calendar, 2008, Calendar.MARCH, 2);
        assertContainsEvent(events, calendar, 2008, Calendar.MARCH, 28);
        assertContainsEvent(events, calendar, 2008, Calendar.MARCH, 29);
        assertContainsEvent(events, calendar, 2008, Calendar.MARCH, 30);
        assertContainsEvent(events, calendar, 2008, Calendar.MARCH, 31);
    }

    /**
     * Test with US locale.
     */
    public void testEventsUS() {
        runTestEvents(Locale.US);
    }

    /**
     * Test with german locale.
     */
    public void testEventsGerman() {
        runTestEvents(Locale.GERMAN);
    }

    private void runTestEvents(Locale locale) {
        System.out.println("--------");
        System.out.println("Test events with locale " + locale.getDisplayName());
        Locale.setDefault(locale);
        MonthlyByDate options = new MonthlyByDate();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2008, Calendar.JANUARY, 1);
        options.setStartDate(calendar);
        options.setContinuesforMonths(7);
        options.setInterval(2);
        options.setRepeats(1, true);
        options.setRepeats(2, true);
        options.setRepeats(3, true);
        options.setRepeats(4, true);
        System.out.println(options);
        SortedSet events = options.getEvents();
        logEvents(events);
        assertEquals("number of events", 16, events.size());
        assertContainsEvent(events, calendar, 2008, Calendar.JANUARY, 1);
        assertContainsEvent(events, calendar, 2008, Calendar.JANUARY, 2);
        assertContainsEvent(events, calendar, 2008, Calendar.JANUARY, 3);
        assertContainsEvent(events, calendar, 2008, Calendar.JANUARY, 4);
        assertContainsEvent(events, calendar, 2008, Calendar.MARCH, 1);
        assertContainsEvent(events, calendar, 2008, Calendar.MARCH, 2);
        assertContainsEvent(events, calendar, 2008, Calendar.MARCH, 3);
        assertContainsEvent(events, calendar, 2008, Calendar.MARCH, 4);
        assertContainsEvent(events, calendar, 2008, Calendar.MAY, 1);
        assertContainsEvent(events, calendar, 2008, Calendar.MAY, 2);
        assertContainsEvent(events, calendar, 2008, Calendar.MAY, 3);
        assertContainsEvent(events, calendar, 2008, Calendar.MAY, 4);
        assertContainsEvent(events, calendar, 2008, Calendar.JULY, 1);
        assertContainsEvent(events, calendar, 2008, Calendar.JULY, 2);
        assertContainsEvent(events, calendar, 2008, Calendar.JULY, 3);
        assertContainsEvent(events, calendar, 2008, Calendar.JULY, 4);
        assertNotContainsEvent(events, calendar, 2008, Calendar.AUGUST, 1);
        assertNotContainsEvent(events, calendar, 2008, Calendar.AUGUST, 2);
        assertNotContainsEvent(events, calendar, 2008, Calendar.AUGUST, 3);
        assertNotContainsEvent(events, calendar, 2008, Calendar.AUGUST, 4);
        assertNotContainsEvent(events, calendar, 2008, Calendar.SEPTEMBER, 1);
        assertNotContainsEvent(events, calendar, 2008, Calendar.SEPTEMBER, 2);
        assertNotContainsEvent(events, calendar, 2008, Calendar.SEPTEMBER, 3);
        assertNotContainsEvent(events, calendar, 2008, Calendar.SEPTEMBER, 4);
    }

    /**
     * Test mapping a repeating appointment.
     */
    public void testMapping() {
        CalendarEntry entry = new Appointment();
        entry.setStartDate(2008, Calendar.JANUARY, 1);
        entry.setStartTime(15, 59, 58);
        entry.setLocation("location");
        entry.addCategory("Category");
        entry.setTitle("Title");
        MonthlyByDate options = new MonthlyByDate();
        entry.setRepeatOptions(options);
        options.setStartDate(entry.getStartDate());
    }
}
