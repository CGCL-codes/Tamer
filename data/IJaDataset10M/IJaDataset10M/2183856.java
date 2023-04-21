package tracker;

import java.util.*;
import junit.framework.TestCase;

public class WeekTest extends TestCase {

    String[] frenchWeekDays = { "dimanche", "lundi", "mardi", "mercredi", "jeudi", "vendredi", "samedi" };

    public void testGetLocalizedDayOfWeekFor() throws Exception {
        Locale originalLocale = Locale.getDefault();
        Locale.setDefault(Locale.FRANCE);
        for (int firstDayOfWeek = 1; firstDayOfWeek < 8; firstDayOfWeek++) {
            Week w = new Week(firstDayOfWeek);
            assertEquals("loop#" + firstDayOfWeek, frenchWeekDays[0], w.getLocalizedDayOfWeekFor(1));
            assertEquals("loop#" + firstDayOfWeek, frenchWeekDays[1], w.getLocalizedDayOfWeekFor(2));
            assertEquals("loop#" + firstDayOfWeek, frenchWeekDays[2], w.getLocalizedDayOfWeekFor(3));
            assertEquals("loop#" + firstDayOfWeek, frenchWeekDays[3], w.getLocalizedDayOfWeekFor(4));
            assertEquals("loop#" + firstDayOfWeek, frenchWeekDays[4], w.getLocalizedDayOfWeekFor(5));
            assertEquals("loop#" + firstDayOfWeek, frenchWeekDays[5], w.getLocalizedDayOfWeekFor(6));
            assertEquals("loop#" + firstDayOfWeek, frenchWeekDays[6], w.getLocalizedDayOfWeekFor(7));
            try {
                w.getLocalizedDayOfWeekFor(8);
                fail();
            } catch (Exception expected) {
            }
            try {
                w.getLocalizedDayOfWeekFor(0);
                fail();
            } catch (Exception expected) {
            }
        }
        Locale.setDefault(originalLocale);
    }

    public void getDaysSinceFirstDOW() {
        Week w = new Week(Calendar.WEDNESDAY);
        assertEquals(0, w.getDaysSinceFirstDOW(Calendar.WEDNESDAY));
        assertEquals(1, w.getDaysSinceFirstDOW(Calendar.THURSDAY));
        assertEquals(6, w.getDaysSinceFirstDOW(Calendar.TUESDAY));
        w = new Week(Calendar.SATURDAY);
        assertEquals(0, w.getDaysSinceFirstDOW(Calendar.SATURDAY));
        assertEquals(5, w.getDaysSinceFirstDOW(Calendar.THURSDAY));
        assertEquals(3, w.getDaysSinceFirstDOW(Calendar.TUESDAY));
        w = new Week(Calendar.SUNDAY);
        assertEquals(6, w.getDaysSinceFirstDOW(Calendar.SATURDAY));
        assertEquals(4, w.getDaysSinceFirstDOW(Calendar.THURSDAY));
        assertEquals(2, w.getDaysSinceFirstDOW(Calendar.TUESDAY));
    }

    public void testIterator() throws Exception {
        Locale originalLocale = Locale.getDefault();
        Locale.setDefault(Locale.FRANCE);
        for (int firstDayOfWeek = 1; firstDayOfWeek < 8; firstDayOfWeek++) {
            Week w = new Week(firstDayOfWeek);
            Iterator<String> iterator = w.getDayOfWeekIterator();
            int ordinal = 0;
            while (iterator.hasNext()) {
                String day = iterator.next();
                assertEquals("loop#" + firstDayOfWeek, getFrenchWeekDay(ordinal + firstDayOfWeek), day);
                ordinal++;
            }
        }
        Locale.setDefault(originalLocale);
    }

    private String getFrenchWeekDay(int dayOfWeek) {
        if (dayOfWeek > 7) dayOfWeek -= 7;
        return frenchWeekDays[dayOfWeek - 1];
    }
}
