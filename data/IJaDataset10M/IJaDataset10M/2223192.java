package org.wwweeeportal.util;

import java.util.*;
import org.wwweeeportal.util.convert.*;
import static org.junit.Assert.*;
import org.junit.*;

/**
 * JUnit tests for {@link DateUtil}.
 */
public class DateUtilTest {

    private static final Calendar mkCal(final Integer year, final Integer month, final Integer dayOfMonth, final Integer hourOfDay, final Integer minute, final Integer second, final Integer millisecond, final TimeZone timeZone) {
        final Calendar cal1 = Calendar.getInstance(timeZone);
        cal1.clear();
        if (year != null) cal1.set(Calendar.YEAR, year.intValue());
        if (month != null) cal1.set(Calendar.MONTH, month.intValue());
        if (dayOfMonth != null) cal1.set(Calendar.DAY_OF_MONTH, dayOfMonth.intValue());
        if (hourOfDay != null) cal1.set(Calendar.HOUR_OF_DAY, hourOfDay.intValue());
        if (minute != null) cal1.set(Calendar.MINUTE, minute.intValue());
        if (second != null) cal1.set(Calendar.SECOND, second.intValue());
        if (millisecond != null) cal1.set(Calendar.MILLISECOND, millisecond.intValue());
        return cal1;
    }

    /**
   * Test {@link DateUtil#ISO_8601_STRING_CALENDAR_CONVERTER}.
   */
    @SuppressWarnings("boxing")
    @Test
    public void testISO8601StringCalendarConverter() {
        assertEquals(mkCal(1976, Calendar.MAY, 15, 3, 42, 12, 420, TimeZone.getTimeZone("GMT-7")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-15T03:42:12.420-07:00"));
        assertEquals(mkCal(1976, Calendar.MAY, 15, 3, 42, 12, 420, TimeZone.getTimeZone("GMT-7")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-15T03:42:12.420-7:00"));
        assertEquals(mkCal(1976, Calendar.MAY, 15, 3, 42, 12, 420, TimeZone.getTimeZone("GMT-7")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-15T03:42:12.420-07"));
        assertEquals(mkCal(1976, Calendar.MAY, 15, 3, 42, 12, 420, TimeZone.getTimeZone("GMT-7")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-15T03:42:12.420-7"));
        assertEquals(mkCal(1976, Calendar.MAY, 15, 3, 42, 12, 420, TimeZone.getTimeZone("GMT-7")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-15T03:42:12.42-07"));
        assertEquals(mkCal(1976, Calendar.MAY, 15, 3, 42, 12, 420, TimeZone.getTimeZone("GMT-7")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-15T03:42:12.42-7"));
        assertEquals(mkCal(1976, Calendar.MAY, 15, 3, 42, 12, 420, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-15T03:42:12.420Z"));
        assertEquals(mkCal(1976, Calendar.MAY, 15, 3, 42, 12, 420, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-15T03:42:12.42Z"));
        assertEquals(mkCal(1976, Calendar.MAY, 15, 3, 42, 12, 420, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-15T03:42:12.42"));
        assertEquals(mkCal(1976, Calendar.MAY, 15, 3, 42, 12, null, TimeZone.getTimeZone("GMT-7")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-15T03:42:12.000-07"));
        assertEquals(mkCal(1976, Calendar.MAY, 15, 3, 42, 12, null, TimeZone.getTimeZone("GMT-7")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-15T03:42:12.00-07"));
        assertEquals(mkCal(1976, Calendar.MAY, 15, 3, 42, 12, null, TimeZone.getTimeZone("GMT-7")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-15T03:42:12.0-07"));
        assertEquals(mkCal(1976, Calendar.MAY, 15, 3, 42, 12, null, TimeZone.getTimeZone("GMT-7")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-15T03:42:12-07"));
        assertEquals(mkCal(1976, Calendar.MAY, 15, 3, 42, 12, null, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-15T03:42:12.000Z"));
        assertEquals(mkCal(1976, Calendar.MAY, 15, 3, 42, 12, null, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-15T03:42:12.00Z"));
        assertEquals(mkCal(1976, Calendar.MAY, 15, 3, 42, 12, null, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-15T03:42:12.0Z"));
        assertEquals(mkCal(1976, Calendar.MAY, 15, 3, 42, 12, null, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-15T03:42:12Z"));
        assertEquals(mkCal(1976, Calendar.MAY, 15, 3, 42, 12, null, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-15T03:42:12"));
        assertEquals(mkCal(1976, Calendar.MAY, 15, 3, 42, null, null, TimeZone.getTimeZone("GMT-7")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-15T03:42:00-7"));
        assertEquals(mkCal(1976, Calendar.MAY, 15, 3, 42, null, null, TimeZone.getTimeZone("GMT-7")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-15T03:42:0-7"));
        assertEquals(mkCal(1976, Calendar.MAY, 15, 3, 42, null, null, TimeZone.getTimeZone("GMT-7")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-15T03:42-7"));
        assertEquals(mkCal(1976, Calendar.MAY, 15, 3, 42, null, null, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-15T03:42:00.0Z"));
        assertEquals(mkCal(1976, Calendar.MAY, 15, 3, 42, null, null, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-15T03:42:0.0Z"));
        assertEquals(mkCal(1976, Calendar.MAY, 15, 3, 42, null, null, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-15T03:42:00Z"));
        assertEquals(mkCal(1976, Calendar.MAY, 15, 3, 42, null, null, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-15T03:42:0Z"));
        assertEquals(mkCal(1976, Calendar.MAY, 15, 3, 42, null, null, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-15T03:42:00"));
        assertEquals(mkCal(1976, Calendar.MAY, 15, 3, 42, null, null, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-15T03:42:0"));
        assertEquals(mkCal(1976, Calendar.MAY, 15, 3, 42, null, null, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-15T03:42Z"));
        assertEquals(mkCal(1976, Calendar.MAY, 15, 3, 42, null, null, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-15T03:42"));
        assertEquals(mkCal(1976, Calendar.MAY, 15, 3, null, null, null, TimeZone.getTimeZone("GMT-7")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-15T03:00:00-7"));
        assertEquals(mkCal(1976, Calendar.MAY, 15, 3, null, null, null, TimeZone.getTimeZone("GMT-7")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-15T03:0:00-7"));
        assertEquals(mkCal(1976, Calendar.MAY, 15, 3, null, null, null, TimeZone.getTimeZone("GMT-7")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-15T03:00-7"));
        assertEquals(mkCal(1976, Calendar.MAY, 15, 3, null, null, null, TimeZone.getTimeZone("GMT-7")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-15T03:0-7"));
        assertEquals(mkCal(1976, Calendar.MAY, 15, 3, null, null, null, TimeZone.getTimeZone("GMT-7")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-15T3:0-7"));
        assertEquals(mkCal(1976, Calendar.MAY, 15, 3, null, null, null, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-15T03:00:00Z"));
        assertEquals(mkCal(1976, Calendar.MAY, 15, 3, null, null, null, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-15T03:0:00Z"));
        assertEquals(mkCal(1976, Calendar.MAY, 15, 3, null, null, null, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-15T03:00Z"));
        assertEquals(mkCal(1976, Calendar.MAY, 15, 3, null, null, null, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-15T03:0Z"));
        assertEquals(mkCal(1976, Calendar.MAY, 15, null, null, null, null, TimeZone.getTimeZone("GMT-7")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-15T00:00:00.0-7"));
        assertEquals(mkCal(1976, Calendar.MAY, 15, null, null, null, null, TimeZone.getTimeZone("GMT-7")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-15T00:00:00-7"));
        assertEquals(mkCal(1976, Calendar.MAY, 15, null, null, null, null, TimeZone.getTimeZone("GMT-7")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-15T00:00-7"));
        assertEquals(mkCal(1976, Calendar.MAY, 15, null, null, null, null, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-15T00:00:00.0Z"));
        assertEquals(mkCal(1976, Calendar.MAY, 15, null, null, null, null, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-15T00:00:00Z"));
        assertEquals(mkCal(1976, Calendar.MAY, 15, null, null, null, null, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-15T00:00Z"));
        assertEquals(mkCal(1976, Calendar.MAY, 15, null, null, null, null, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-15T"));
        assertEquals(mkCal(1976, Calendar.MAY, 15, null, null, null, null, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-15"));
        assertEquals(mkCal(1976, Calendar.MAY, null, null, null, null, null, TimeZone.getTimeZone("GMT-7")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-01T00:00:00.0-7"));
        assertEquals(mkCal(1976, Calendar.MAY, null, null, null, null, null, TimeZone.getTimeZone("GMT-7")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-01T00:00:00-7"));
        assertEquals(mkCal(1976, Calendar.MAY, null, null, null, null, null, TimeZone.getTimeZone("GMT-7")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-01T00:00-7"));
        assertEquals(mkCal(1976, Calendar.MAY, null, null, null, null, null, TimeZone.getTimeZone("GMT-7")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-1T00:00-7"));
        assertEquals(mkCal(1976, Calendar.MAY, null, null, null, null, null, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-01T00:00:00.0Z"));
        assertEquals(mkCal(1976, Calendar.MAY, null, null, null, null, null, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-01T00:00:00.0"));
        assertEquals(mkCal(1976, Calendar.MAY, null, null, null, null, null, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-01T00:00:00Z"));
        assertEquals(mkCal(1976, Calendar.MAY, null, null, null, null, null, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-01T00:00:00"));
        assertEquals(mkCal(1976, Calendar.MAY, null, null, null, null, null, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-01T00:00Z"));
        assertEquals(mkCal(1976, Calendar.MAY, null, null, null, null, null, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-01T00:00"));
        assertEquals(mkCal(1976, Calendar.MAY, null, null, null, null, null, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-01T00"));
        assertEquals(mkCal(1976, Calendar.MAY, null, null, null, null, null, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-01"));
        assertEquals(mkCal(1976, Calendar.MAY, null, null, null, null, null, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05-1"));
        assertEquals(mkCal(1976, Calendar.MAY, null, null, null, null, null, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-5-01"));
        assertEquals(mkCal(1976, Calendar.MAY, null, null, null, null, null, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-5-1"));
        assertEquals(mkCal(1976, Calendar.MAY, null, null, null, null, null, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-05"));
        assertEquals(mkCal(1976, Calendar.MAY, null, null, null, null, null, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-5"));
        assertEquals(mkCal(1976, null, null, null, null, null, null, TimeZone.getTimeZone("GMT-7")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-01-01T00:00:00.0-7"));
        assertEquals(mkCal(1976, null, null, null, null, null, null, TimeZone.getTimeZone("GMT-7")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-01-01T00:00:00-7"));
        assertEquals(mkCal(1976, null, null, null, null, null, null, TimeZone.getTimeZone("GMT-7")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-01-01T00:00-7"));
        assertEquals(mkCal(1976, null, null, null, null, null, null, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-01-01T00:00:00.0Z"));
        assertEquals(mkCal(1976, null, null, null, null, null, null, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-01-01T00:00:00.0"));
        assertEquals(mkCal(1976, null, null, null, null, null, null, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-01-01T00:00:00Z"));
        assertEquals(mkCal(1976, null, null, null, null, null, null, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-01-01T00:00:00"));
        assertEquals(mkCal(1976, null, null, null, null, null, null, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-01-01T00:00Z"));
        assertEquals(mkCal(1976, null, null, null, null, null, null, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-01-01T00:00"));
        assertEquals(mkCal(1976, null, null, null, null, null, null, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-01-01T00"));
        assertEquals(mkCal(1976, null, null, null, null, null, null, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-01-01"));
        assertEquals(mkCal(1976, null, null, null, null, null, null, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-01"));
        assertEquals(mkCal(1976, null, null, null, null, null, null, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976-1"));
        assertEquals(mkCal(1976, null, null, null, null, null, null, TimeZone.getTimeZone("UTC")), ConversionUtil.invokeConverter(DateUtil.ISO_8601_STRING_CALENDAR_CONVERTER, "1976"));
        return;
    }

    /**
   * Test {@link DateUtil#ISO_8601_CALENDAR_STRING_CONVERTER}.
   */
    @SuppressWarnings("boxing")
    @Test
    public void testISO8601CalendarStringConverter() {
        assertEquals("1976-05-15T03:42:12.42-07", ConversionUtil.invokeConverter(DateUtil.ISO_8601_CALENDAR_STRING_CONVERTER, mkCal(1976, Calendar.MAY, 15, 3, 42, 12, 420, TimeZone.getTimeZone("GMT-7"))));
        assertEquals("1976-05-15T03:42:12.42", ConversionUtil.invokeConverter(DateUtil.ISO_8601_CALENDAR_STRING_CONVERTER, mkCal(1976, Calendar.MAY, 15, 3, 42, 12, 420, TimeZone.getTimeZone("UTC"))));
        assertEquals("1976-05-15T03:42:12-07", ConversionUtil.invokeConverter(DateUtil.ISO_8601_CALENDAR_STRING_CONVERTER, mkCal(1976, Calendar.MAY, 15, 3, 42, 12, null, TimeZone.getTimeZone("GMT-7"))));
        assertEquals("1976-05-15T03:42:12", ConversionUtil.invokeConverter(DateUtil.ISO_8601_CALENDAR_STRING_CONVERTER, mkCal(1976, Calendar.MAY, 15, 3, 42, 12, null, TimeZone.getTimeZone("UTC"))));
        assertEquals("1976-05-15T03:42:00-07", ConversionUtil.invokeConverter(DateUtil.ISO_8601_CALENDAR_STRING_CONVERTER, mkCal(1976, Calendar.MAY, 15, 3, 42, null, null, TimeZone.getTimeZone("GMT-7"))));
        assertEquals("1976-05-15T03:42", ConversionUtil.invokeConverter(DateUtil.ISO_8601_CALENDAR_STRING_CONVERTER, mkCal(1976, Calendar.MAY, 15, 3, 42, null, null, TimeZone.getTimeZone("UTC"))));
        assertEquals("1976-05-15T03:00:00-07", ConversionUtil.invokeConverter(DateUtil.ISO_8601_CALENDAR_STRING_CONVERTER, mkCal(1976, Calendar.MAY, 15, 3, null, null, null, TimeZone.getTimeZone("GMT-7"))));
        assertEquals("1976-05-15T03", ConversionUtil.invokeConverter(DateUtil.ISO_8601_CALENDAR_STRING_CONVERTER, mkCal(1976, Calendar.MAY, 15, 3, null, null, null, TimeZone.getTimeZone("UTC"))));
        assertEquals("1976-05-15T00:00:00-07", ConversionUtil.invokeConverter(DateUtil.ISO_8601_CALENDAR_STRING_CONVERTER, mkCal(1976, Calendar.MAY, 15, null, null, null, null, TimeZone.getTimeZone("GMT-7"))));
        assertEquals("1976-05-15", ConversionUtil.invokeConverter(DateUtil.ISO_8601_CALENDAR_STRING_CONVERTER, mkCal(1976, Calendar.MAY, 15, null, null, null, null, TimeZone.getTimeZone("UTC"))));
        assertEquals("1976-05-01T00:00:00-07", ConversionUtil.invokeConverter(DateUtil.ISO_8601_CALENDAR_STRING_CONVERTER, mkCal(1976, Calendar.MAY, null, null, null, null, null, TimeZone.getTimeZone("GMT-7"))));
        assertEquals("1976-05", ConversionUtil.invokeConverter(DateUtil.ISO_8601_CALENDAR_STRING_CONVERTER, mkCal(1976, Calendar.MAY, null, null, null, null, null, TimeZone.getTimeZone("UTC"))));
        assertEquals("1976-01-01T00:00:00-07", ConversionUtil.invokeConverter(DateUtil.ISO_8601_CALENDAR_STRING_CONVERTER, mkCal(1976, null, null, null, null, null, null, TimeZone.getTimeZone("GMT-7"))));
        assertEquals("1976", ConversionUtil.invokeConverter(DateUtil.ISO_8601_CALENDAR_STRING_CONVERTER, mkCal(1976, null, null, null, null, null, null, TimeZone.getTimeZone("UTC"))));
        return;
    }

    /**
   * Test {@link DateUtil#ISO_8601_CALENDAR_STRING_FULL_CONVERTER}.
   */
    @SuppressWarnings("boxing")
    @Test
    public void testISO8601CalendarStringFullConverter() {
        assertEquals("1976-05-15T03:42:12.42-07", ConversionUtil.invokeConverter(DateUtil.ISO_8601_CALENDAR_STRING_FULL_CONVERTER, mkCal(1976, Calendar.MAY, 15, 3, 42, 12, 420, TimeZone.getTimeZone("GMT-7"))));
        assertEquals("1976-05-15T03:42:12.42Z", ConversionUtil.invokeConverter(DateUtil.ISO_8601_CALENDAR_STRING_FULL_CONVERTER, mkCal(1976, Calendar.MAY, 15, 3, 42, 12, 420, TimeZone.getTimeZone("UTC"))));
        assertEquals("1976-05-15T03:42:12-07", ConversionUtil.invokeConverter(DateUtil.ISO_8601_CALENDAR_STRING_FULL_CONVERTER, mkCal(1976, Calendar.MAY, 15, 3, 42, 12, null, TimeZone.getTimeZone("GMT-7"))));
        assertEquals("1976-05-15T03:42:12Z", ConversionUtil.invokeConverter(DateUtil.ISO_8601_CALENDAR_STRING_FULL_CONVERTER, mkCal(1976, Calendar.MAY, 15, 3, 42, 12, null, TimeZone.getTimeZone("UTC"))));
        assertEquals("1976-05-15T03:42:00-07", ConversionUtil.invokeConverter(DateUtil.ISO_8601_CALENDAR_STRING_FULL_CONVERTER, mkCal(1976, Calendar.MAY, 15, 3, 42, null, null, TimeZone.getTimeZone("GMT-7"))));
        assertEquals("1976-05-15T03:42:00Z", ConversionUtil.invokeConverter(DateUtil.ISO_8601_CALENDAR_STRING_FULL_CONVERTER, mkCal(1976, Calendar.MAY, 15, 3, 42, null, null, TimeZone.getTimeZone("UTC"))));
        assertEquals("1976-05-15T03:00:00-07", ConversionUtil.invokeConverter(DateUtil.ISO_8601_CALENDAR_STRING_FULL_CONVERTER, mkCal(1976, Calendar.MAY, 15, 3, null, null, null, TimeZone.getTimeZone("GMT-7"))));
        assertEquals("1976-05-15T03:00:00Z", ConversionUtil.invokeConverter(DateUtil.ISO_8601_CALENDAR_STRING_FULL_CONVERTER, mkCal(1976, Calendar.MAY, 15, 3, null, null, null, TimeZone.getTimeZone("UTC"))));
        assertEquals("1976-05-15T00:00:00-07", ConversionUtil.invokeConverter(DateUtil.ISO_8601_CALENDAR_STRING_FULL_CONVERTER, mkCal(1976, Calendar.MAY, 15, null, null, null, null, TimeZone.getTimeZone("GMT-7"))));
        assertEquals("1976-05-15T00:00:00Z", ConversionUtil.invokeConverter(DateUtil.ISO_8601_CALENDAR_STRING_FULL_CONVERTER, mkCal(1976, Calendar.MAY, 15, null, null, null, null, TimeZone.getTimeZone("UTC"))));
        assertEquals("1976-05-01T00:00:00-07", ConversionUtil.invokeConverter(DateUtil.ISO_8601_CALENDAR_STRING_FULL_CONVERTER, mkCal(1976, Calendar.MAY, null, null, null, null, null, TimeZone.getTimeZone("GMT-7"))));
        assertEquals("1976-05-01T00:00:00Z", ConversionUtil.invokeConverter(DateUtil.ISO_8601_CALENDAR_STRING_FULL_CONVERTER, mkCal(1976, Calendar.MAY, null, null, null, null, null, TimeZone.getTimeZone("UTC"))));
        assertEquals("1976-01-01T00:00:00-07", ConversionUtil.invokeConverter(DateUtil.ISO_8601_CALENDAR_STRING_FULL_CONVERTER, mkCal(1976, null, null, null, null, null, null, TimeZone.getTimeZone("GMT-7"))));
        assertEquals("1976-01-01T00:00:00Z", ConversionUtil.invokeConverter(DateUtil.ISO_8601_CALENDAR_STRING_FULL_CONVERTER, mkCal(1976, null, null, null, null, null, null, TimeZone.getTimeZone("UTC"))));
        return;
    }
}
