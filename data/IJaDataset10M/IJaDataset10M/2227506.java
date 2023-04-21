package de.bea.domingo.util;

import java.util.TimeZone;
import junit.framework.TestCase;

/**
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class TimezonesTest extends TestCase {

    /**
     * @param name the name of the test
     */
    public TimezonesTest(String name) {
        super(name);
    }

    /**
     * test time zone conversions.
     */
    public void testTimezoneEuropeBerlinString() {
        String javaTimeZone = "Europe/Berlin";
        String lotusTimeZone = Timezones.getLotusTimeZoneString(javaTimeZone);
        System.out.println("Lotus time zone for '" + javaTimeZone + "' = '" + lotusTimeZone + "'");
        assertEquals("Z=-1$DO=1$DL=3 -1 1 10 -1 1$ZX=70$ZN=W. Europe", lotusTimeZone);
    }

    /**
     * test time zone conversions.
     */
    public void testTimezoneUSPacificString() {
        String javaTimeZone = "US/Pacific";
        String lotusTimeZone = Timezones.getLotusTimeZoneString(javaTimeZone);
        System.out.println("Lotus time zone for '" + javaTimeZone + "' = '" + lotusTimeZone + "'");
        assertEquals("Z=8$DO=1$DL=3 2 1 11 1 1$ZX=61$ZN=Pacific", lotusTimeZone);
    }

    /**
     * test time zone conversions.
     */
    public void testTimezoneEuropeBerlinTimeZone() {
        TimeZone javaTimeZone = TimeZone.getTimeZone("Europe/Berlin");
        String lotusTimeZone = Timezones.getLotusTimeZoneString(javaTimeZone);
        System.out.println("Lotus time zone for '" + javaTimeZone + "' = '" + lotusTimeZone + "'");
        assertEquals("Z=-1$DO=1$DL=3 -1 1 10 -1 1$ZX=70$ZN=W. Europe", lotusTimeZone);
    }

    /**
     * test time zone conversions.
     */
    public void testTimezoneUSPacificTimeZone() {
        TimeZone javaTimeZone = TimeZone.getTimeZone("US/Pacific");
        String lotusTimeZone = Timezones.getLotusTimeZoneString(javaTimeZone);
        System.out.println("Lotus time zone for '" + javaTimeZone + "' = '" + lotusTimeZone + "'");
        assertEquals("Z=8$DO=1$DL=3 2 1 11 1 1$ZX=61$ZN=Pacific", lotusTimeZone);
    }

    /**
     * test time zone conversions.
     */
    public void testTimezoneUnknown() {
        String javaTimeZone = "Unknown";
        try {
            Timezones.getLotusTimeZoneString(javaTimeZone);
        } catch (NullPointerException e) {
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            return;
        }
        fail("IllegalArgumentException expected");
    }

    /**
     * test time zone null string.
     */
    public void testTimezoneNullString() {
        String javaTimeZone = null;
        try {
            Timezones.getLotusTimeZoneString((String) javaTimeZone);
        } catch (NullPointerException e) {
            return;
        } catch (IllegalArgumentException e) {
            fail("NullPointerException expected");
        }
        fail("NullPointerException expected");
    }

    /**
     * test time zone empty string.
     */
    public void testTimezoneEmptyString() {
        String javaTimeZone = "";
        try {
            Timezones.getLotusTimeZoneString((String) javaTimeZone);
        } catch (NullPointerException e) {
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            return;
        }
        fail("IllegalArgumentException expected");
    }

    /**
     * test time zone null timezone.
     */
    public void testTimezoneNullTimezone() {
        TimeZone javaTimeZone = null;
        try {
            Timezones.getLotusTimeZoneString((TimeZone) javaTimeZone);
        } catch (NullPointerException e) {
            return;
        } catch (IllegalArgumentException e) {
            fail("NullPointerException expected");
        }
        fail("NullPointerException expected");
    }
}
