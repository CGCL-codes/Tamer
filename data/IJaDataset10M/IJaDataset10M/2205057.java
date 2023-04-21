package jaxlib.time.cron;

import jaxlib.junit.ObjectTestCase;
import jaxlib.time.Weekday;

/**
 * @author  <a href="mailto:joerg.wassmer@web.de">Joerg Wassmer</a>
 * @since   JaXLib 1.0
 * @version $Id: WeekdaysTest.java 2896 2011-04-10 16:24:47Z joerg_wassmer $
 */
public final class WeekdaysTest extends ObjectTestCase {

    public WeekdaysTest(String name) {
        super(name);
    }

    @Override
    protected Object createObject() {
        return Weekdays.NONE;
    }

    @Override
    public void testToString() {
        assertEquals("-", Weekdays.NONE.toString());
        assertEquals("*", Weekdays.EACH.toString());
        for (int i = 1; i <= 7; i++) {
            final Weekdays m = Weekdays.ofBits(1 << (i - 1));
            assertEquals(Integer.toString(Weekday.of(i).number), m.toString());
        }
    }
}
