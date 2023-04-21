package com.google.code.jholidays.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.Calendar;
import java.util.Date;
import org.junit.Test;
import com.google.code.jholidays.events.parameters.DayMonthPair;

public class DayMonthPairTest {

    @Test
    public void testValidDays() {
        Calendar c = Calendar.getInstance();
        int maxDay = c.getActualMaximum(Calendar.DAY_OF_YEAR);
        int minDay = c.getActualMinimum(Calendar.DAY_OF_YEAR);
        c.set(Calendar.DAY_OF_YEAR, minDay);
        int currentDay = c.get(Calendar.DAY_OF_YEAR);
        while (currentDay <= maxDay) {
            DayMonthPair pair = new DayMonthPair(c.get(Calendar.DATE), c.get(Calendar.MONTH) + 1);
            assertNotNull(pair);
            c.set(Calendar.DAY_OF_YEAR, currentDay + 1);
            currentDay = c.get(Calendar.DAY_OF_YEAR);
        }
    }

    @Test
    public void testFebruary() {
        int month = 2;
        int day = 28;
        DayMonthPair pair = new DayMonthPair(day, month);
        assertNotNull(pair);
        ++day;
        pair = new DayMonthPair(day, month);
        assertNotNull(pair);
        ++day;
        try {
            pair = new DayMonthPair(day, month);
            assertNull("Exception must be thrown", pair);
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void testIllegalDatesHi() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, Calendar.JANUARY);
        int currentMonth = c.get(Calendar.MONTH);
        int currentDay = -1;
        while (currentMonth <= Calendar.DECEMBER) {
            if (currentMonth == Calendar.FEBRUARY) {
                currentDay = 30;
            } else {
                currentDay = c.getActualMaximum(Calendar.DATE);
                ++currentDay;
            }
            try {
                DayMonthPair pair = new DayMonthPair(currentDay, currentMonth + 1);
                assertNull(pair);
            } catch (Exception e) {
                assertTrue(e instanceof IllegalArgumentException);
            }
            ++currentMonth;
            c.set(Calendar.MONTH, currentMonth);
        }
    }

    @Test
    public void testIllegalDatesLow() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, Calendar.JANUARY);
        int currentMonth = c.get(Calendar.MONTH);
        int currentDay = -1;
        while (currentMonth <= Calendar.DECEMBER) {
            currentDay = 0;
            try {
                DayMonthPair pair = new DayMonthPair(currentDay, currentMonth + 1);
                assertNull(pair);
            } catch (Exception e) {
                assertTrue(e instanceof IllegalArgumentException);
            }
            ++currentMonth;
            c.set(Calendar.MONTH, currentMonth);
        }
    }

    @Test
    public void testIllegalDatesNegativeDay() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, Calendar.JANUARY);
        int currentMonth = c.get(Calendar.MONTH);
        int currentDay = -1;
        while (currentMonth <= Calendar.DECEMBER) {
            for (int i = 1; i < 50; ++i) {
                currentDay = -i;
                try {
                    DayMonthPair pair = new DayMonthPair(currentDay, currentMonth + 1);
                    assertNull(pair);
                } catch (Exception e) {
                    assertTrue(e instanceof IllegalArgumentException);
                }
            }
            ++currentMonth;
            c.set(Calendar.MONTH, currentMonth);
        }
    }

    @Test
    public void testGetDate() {
        final int year = 2000;
        final int month = Calendar.JANUARY;
        final int date = 1;
        Calendar c = Calendar.getInstance();
        c.clear();
        c.set(year, month, date);
        Date calendarDate = c.getTime();
        DayMonthPair pair = new DayMonthPair(date, month + 1);
        assertEquals(pair.getDate(year), calendarDate);
    }
}
