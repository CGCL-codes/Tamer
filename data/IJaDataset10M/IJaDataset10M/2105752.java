package org.nomadpim.core.util.date;

import static org.junit.Assert.assertEquals;
import org.joda.time.DateTime;
import org.junit.Test;

public class DayTest {

    @Test
    public void shouldReturnCorrectDay() {
        assertEquals(Day.MONDAY, Day.getDayOf(new DateTime(2007, 5, 14, 0, 0, 0, 0)));
        assertEquals(Day.MONDAY, Day.getDayOf(new DateTime(2007, 5, 14, 23, 59, 59, 999)));
        assertEquals(Day.TUESDAY, Day.getDayOf(new DateTime(2007, 5, 15, 0, 0, 0, 0)));
        assertEquals(Day.TUESDAY, Day.getDayOf(new DateTime(2007, 5, 15, 23, 59, 59, 999)));
        assertEquals(Day.WEDNESDAY, Day.getDayOf(new DateTime(2007, 5, 16, 0, 0, 0, 0)));
        assertEquals(Day.WEDNESDAY, Day.getDayOf(new DateTime(2007, 5, 16, 23, 59, 59, 999)));
        assertEquals(Day.THURSDAY, Day.getDayOf(new DateTime(2007, 5, 17, 0, 0, 0, 0)));
        assertEquals(Day.THURSDAY, Day.getDayOf(new DateTime(2007, 5, 17, 23, 59, 59, 999)));
        assertEquals(Day.FRIDAY, Day.getDayOf(new DateTime(2007, 5, 18, 0, 0, 0, 0)));
        assertEquals(Day.FRIDAY, Day.getDayOf(new DateTime(2007, 5, 18, 23, 59, 59, 999)));
        assertEquals(Day.SATURDAY, Day.getDayOf(new DateTime(2007, 5, 19, 0, 0, 0, 0)));
        assertEquals(Day.SATURDAY, Day.getDayOf(new DateTime(2007, 5, 19, 23, 59, 59, 999)));
        assertEquals(Day.SUNDAY, Day.getDayOf(new DateTime(2007, 5, 20, 0, 0, 0, 0)));
        assertEquals(Day.SUNDAY, Day.getDayOf(new DateTime(2007, 5, 20, 23, 59, 59, 999)));
    }
}
