package com.google.gwt.emultest.java.sql;

import com.google.gwt.junit.client.GWTTestCase;
import java.sql.Time;

/**
 * Tests {@link java.sql.Time}. We assume that the underlying
 * {@link java.util.Date} implementation is correct and concentrate only on the
 * differences between the two.
 */
@SuppressWarnings("deprecation")
public class SqlTimeTest extends GWTTestCase {

    /**
   * Sets module name so that javascript compiler can operate.
   */
    public String getModuleName() {
        return "com.google.gwt.emultest.EmulSuite";
    }

    public void testUnimplementedFunctions() {
        Time d = new Time(0);
        try {
            d.getYear();
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            d.getMonth();
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            d.getDate();
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            d.getDay();
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            d.setYear(0);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            d.setMonth(0);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            d.setDate(0);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testParse() {
        try {
            Time.parse(null);
            fail("Should have thrown exception");
        } catch (IllegalArgumentException e) {
        }
        try {
            Time.parse("");
        } catch (IllegalArgumentException e) {
        }
        Time t = Time.valueOf("13:01:30");
        assertEquals(13, t.getHours());
        assertEquals(1, t.getMinutes());
        assertEquals(30, t.getSeconds());
        Time d2 = Time.valueOf(t.toString());
        assertEquals(t, d2);
        Time t2 = Time.valueOf("08:09:01");
        assertEquals(8, t2.getHours());
        assertEquals(9, t2.getMinutes());
        assertEquals(1, t2.getSeconds());
        assertEquals(t2, Time.valueOf(t2.toString()));
    }

    public void testToString() {
        Time time = new Time(12, 34, 56);
        assertEquals("12:34:56", time.toString());
    }

    public void testInternalPrecision() {
        long millis = 1283895273475L;
        Time today = new Time(millis);
        Time after = new Time(today.getTime() + 1);
        Time before = new Time(today.getTime() - 1);
        assertTrue(after.after(today));
        assertTrue(before.before(today));
    }
}
