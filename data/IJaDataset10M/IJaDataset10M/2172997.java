package org.matsim.utils.misc;

import org.matsim.testcases.MatsimTestCase;

/**
 * Tests for {@link Time}.
 * 
 * @author mrieser
 */
public class TimeTest extends MatsimTestCase {

    public void testFormats() {
        double time = 12 * 3600 + 34 * 60 + 56.789;
        assertEquals("12:34:56", Time.writeTime(time, Time.TIMEFORMAT_HHMMSS));
        assertEquals("12:34", Time.writeTime(time, Time.TIMEFORMAT_HHMM));
        assertEquals(Integer.toString((int) time), Time.writeTime(time, Time.TIMEFORMAT_SSSS));
        try {
            String str = Time.writeTime(time, "ABCD");
            fail("expected IllegalArgumentException, got result: " + str);
        } catch (IllegalArgumentException expected) {
        }
        try {
            String str = Time.writeTime(time, null);
            fail("expected IllegalArgumentException, got result: " + str);
        } catch (IllegalArgumentException expected) {
        }
        assertEquals(12 * 3600.0 + 34 * 60.0 + 56.0, Time.parseTime("12:34:56"), 0.0);
        assertEquals(12 * 3600.0 + 34 * 60.0 + 56.78, Time.parseTime("12:34:56.78"), 0.0);
        assertEquals(12 * 3600.0 + 34 * 60.0, Time.parseTime("12:34"), 0.0);
        assertEquals(12.0, Time.parseTime("12"), 0.0);
        assertEquals(123456.0, Time.parseTime("123456"), 0.0);
        assertEquals(123456.78, Time.parseTime("123456.78"), 0.0);
        try {
            double t = Time.parseTime("12:34:-01");
            fail("expected IllegalArgumentException, got result: " + t);
        } catch (IllegalArgumentException expected) {
        }
        try {
            double t = Time.parseTime("12:34:60");
            fail("expected IllegalArgumentException, got result: " + t);
        } catch (IllegalArgumentException expected) {
        }
        try {
            double t = Time.parseTime("12:-01:56");
            fail("expected IllegalArgumentException, got result: " + t);
        } catch (IllegalArgumentException expected) {
        }
        try {
            double t = Time.parseTime("12:60:56");
            fail("expected IllegalArgumentException, got result: " + t);
        } catch (IllegalArgumentException expected) {
        }
        try {
            double t = Time.parseTime("12:-01");
            fail("expected IllegalArgumentException, got result: " + t);
        } catch (IllegalArgumentException expected) {
        }
        try {
            double t = Time.parseTime("12:60");
            fail("expected IllegalArgumentException, got result: " + t);
        } catch (IllegalArgumentException expected) {
        }
        try {
            double t = Time.parseTime("12:34:56:78");
            fail("expected IllegalArgumentException, got result: " + t);
        } catch (IllegalArgumentException expected) {
        }
    }

    public void testSeparators() {
        double dTime = 12 * 3600 + 34 * 60 + 56.789;
        assertEquals("12:34:56", Time.writeTime(dTime, ':'));
        assertEquals("12/34/56", Time.writeTime(dTime, '/'));
        assertEquals("12-34-56", Time.writeTime(dTime, '-'));
        double iTime = 12 * 3600 + 34 * 60 + 56;
        assertEquals(iTime, Time.parseTime("12:34:56", ':'), 0.0);
        assertEquals(iTime, Time.parseTime("12/34/56", '/'), 0.0);
        assertEquals(iTime, Time.parseTime("12-34-56", '-'), 0.0);
        assertEquals(-iTime, Time.parseTime("-12:34:56", ':'), 0.0);
        assertEquals(-iTime, Time.parseTime("-12/34/56", '/'), 0.0);
        assertEquals(-iTime, Time.parseTime("-12-34-56", '-'), 0.0);
    }

    public void testUndefined() {
        assertEquals("undefined", Time.writeTime(Time.UNDEFINED_TIME));
        assertEquals(Time.UNDEFINED_TIME, Time.parseTime("undefined"), 0.0);
        assertEquals(Time.UNDEFINED_TIME, Time.parseTime(""), 0.0);
        assertEquals(Time.UNDEFINED_TIME, Time.parseTime(null), 0.0);
    }

    public void testSetDefault() {
        Time.setDefaultTimeFormat(Time.TIMEFORMAT_HHMMSS);
        assertEquals("12:34:56", Time.writeTime(12 * 3600 + 34 * 60 + 56.789));
        Time.setDefaultTimeFormat(Time.TIMEFORMAT_HHMM);
        assertEquals("12:34", Time.writeTime(12 * 3600 + 34 * 60 + 56.789));
        Time.setDefaultTimeFormat(Time.TIMEFORMAT_SSSS);
        assertEquals(Integer.toString(12 * 3600 + 34 * 60 + 56), Time.writeTime(12 * 3600 + 34 * 60 + 56.789));
    }

    public void testWriting() {
        Time.setDefaultTimeFormat(Time.TIMEFORMAT_HHMMSS);
        assertEquals("12:34:56", Time.writeTime(12 * 3600 + 34 * 60 + 56.789));
        assertEquals("01:02:03", Time.writeTime(1 * 3600 + 2 * 60 + 3.4));
        assertEquals("-12:34:56", Time.writeTime(-12 * 3600 - 34 * 60 - 56.789));
        assertEquals("-01:02:03", Time.writeTime(-1 * 3600 - 2 * 60 - 3.4));
        assertEquals("00:00:00", Time.writeTime(0.0));
        assertEquals("-596523:14:08", Time.writeTime(Integer.MIN_VALUE));
    }

    public void testParsing() {
        assertEquals(12 * 3600.0 + 34 * 60.0 + 56.0, Time.parseTime("12:34:56"), 0.0);
        assertEquals(12 * 3600.0 + 34 * 60.0 + 56.7, Time.parseTime("12:34:56.7"), 0.0);
        assertEquals(1 * 3600.0 + 2 * 60.0 + 3.0, Time.parseTime("01:02:03"), 0.0);
        assertEquals(-12 * 3600.0 - 34 * 60.0 - 56.0, Time.parseTime("-12:34:56"), 0.0);
        assertEquals(-12 * 3600.0 - 34 * 60.0 - 56.7, Time.parseTime("-12:34:56.7"), 0.0);
        assertEquals(0.0, Time.parseTime("00:00:00"), 0.0);
        assertEquals(Integer.MIN_VALUE, Time.parseTime("-596523:14:08"), 0.0);
    }
}
