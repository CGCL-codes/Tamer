package jacky.lanlan.song.util.time;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import static org.junit.Assert.*;
import org.junit.*;

/**
 * Unit tests {@link org.apache.commons.lang.time.FastDateFormat}.
 * 
 * @author Sean Schofield
 * @author <a href="mailto:ggregory@seagullsw.com">Gary Gregory</a>
 * @author Fredrik Westermarck
 * @since 2.0
 * @version $Id: FastDateFormatTest.java 490388 2006-12-26 22:10:10Z bayard $
 */
public class FastDateFormatTest {

    @Test
    public void test_getInstance() {
        FastDateFormat format1 = FastDateFormat.getInstance();
        FastDateFormat format2 = FastDateFormat.getInstance();
        assertSame(format1, format2);
        assertEquals(new SimpleDateFormat().toPattern(), format1.getPattern());
    }

    @Test
    public void test_getInstance_String() {
        FastDateFormat format1 = FastDateFormat.getInstance("MM/DD/yyyy");
        FastDateFormat format2 = FastDateFormat.getInstance("MM-DD-yyyy");
        FastDateFormat format3 = FastDateFormat.getInstance("MM-DD-yyyy");
        assertTrue(format1 != format2);
        assertSame(format2, format3);
        assertEquals("MM/DD/yyyy", format1.getPattern());
        assertEquals(TimeZone.getDefault(), format1.getTimeZone());
        assertEquals(TimeZone.getDefault(), format2.getTimeZone());
        assertFalse(format1.getTimeZoneOverridesCalendar());
        assertFalse(format2.getTimeZoneOverridesCalendar());
    }

    @Test
    public void test_getInstance_String_TimeZone() {
        Locale realDefaultLocale = Locale.getDefault();
        TimeZone realDefaultZone = TimeZone.getDefault();
        try {
            Locale.setDefault(Locale.US);
            TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
            FastDateFormat format1 = FastDateFormat.getInstance("MM/DD/yyyy", TimeZone.getTimeZone("Atlantic/Reykjavik"));
            FastDateFormat format2 = FastDateFormat.getInstance("MM/DD/yyyy");
            FastDateFormat format3 = FastDateFormat.getInstance("MM/DD/yyyy", TimeZone.getDefault());
            FastDateFormat format4 = FastDateFormat.getInstance("MM/DD/yyyy", TimeZone.getDefault());
            FastDateFormat format5 = FastDateFormat.getInstance("MM-DD-yyyy", TimeZone.getDefault());
            FastDateFormat format6 = FastDateFormat.getInstance("MM-DD-yyyy");
            assertTrue(format1 != format2);
            assertEquals(TimeZone.getTimeZone("Atlantic/Reykjavik"), format1.getTimeZone());
            assertTrue(format1.getTimeZoneOverridesCalendar());
            assertEquals(TimeZone.getDefault(), format2.getTimeZone());
            assertFalse(format2.getTimeZoneOverridesCalendar());
            assertSame(format3, format4);
            assertTrue(format3 != format5);
            assertTrue(format4 != format6);
        } finally {
            Locale.setDefault(realDefaultLocale);
            TimeZone.setDefault(realDefaultZone);
        }
    }

    @Test
    public void test_getInstance_String_Locale() {
        Locale realDefaultLocale = Locale.getDefault();
        try {
            Locale.setDefault(Locale.US);
            FastDateFormat format1 = FastDateFormat.getInstance("MM/DD/yyyy", Locale.GERMANY);
            FastDateFormat format2 = FastDateFormat.getInstance("MM/DD/yyyy");
            FastDateFormat format3 = FastDateFormat.getInstance("MM/DD/yyyy", Locale.GERMANY);
            assertTrue(format1 != format2);
            assertSame(format1, format3);
            assertSame(Locale.GERMANY, format1.getLocale());
        } finally {
            Locale.setDefault(realDefaultLocale);
        }
    }

    @Test
    public void test_getInstance_String_TimeZone_Locale() {
        Locale realDefaultLocale = Locale.getDefault();
        TimeZone realDefaultZone = TimeZone.getDefault();
        try {
            Locale.setDefault(Locale.US);
            TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
            FastDateFormat format1 = FastDateFormat.getInstance("MM/DD/yyyy", TimeZone.getTimeZone("Atlantic/Reykjavik"), Locale.GERMANY);
            FastDateFormat format2 = FastDateFormat.getInstance("MM/DD/yyyy", Locale.GERMANY);
            FastDateFormat format3 = FastDateFormat.getInstance("MM/DD/yyyy", TimeZone.getDefault(), Locale.GERMANY);
            assertTrue(format1 != format2);
            assertEquals(TimeZone.getTimeZone("Atlantic/Reykjavik"), format1.getTimeZone());
            assertEquals(TimeZone.getDefault(), format2.getTimeZone());
            assertEquals(TimeZone.getDefault(), format3.getTimeZone());
            assertTrue(format1.getTimeZoneOverridesCalendar());
            assertFalse(format2.getTimeZoneOverridesCalendar());
            assertTrue(format3.getTimeZoneOverridesCalendar());
            assertEquals(Locale.GERMANY, format1.getLocale());
            assertEquals(Locale.GERMANY, format2.getLocale());
            assertEquals(Locale.GERMANY, format3.getLocale());
        } finally {
            Locale.setDefault(realDefaultLocale);
            TimeZone.setDefault(realDefaultZone);
        }
    }

    @Test
    public void testFormat() {
        Locale realDefaultLocale = Locale.getDefault();
        TimeZone realDefaultZone = TimeZone.getDefault();
        try {
            Locale.setDefault(Locale.US);
            TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
            FastDateFormat fdf = null;
            SimpleDateFormat sdf = null;
            GregorianCalendar cal1 = new GregorianCalendar(2003, 0, 10, 15, 33, 20);
            GregorianCalendar cal2 = new GregorianCalendar(2003, 6, 10, 9, 00, 00);
            Date date1 = cal1.getTime();
            Date date2 = cal2.getTime();
            long millis1 = date1.getTime();
            long millis2 = date2.getTime();
            fdf = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss");
            sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            assertEquals(sdf.format(date1), fdf.format(date1));
            assertEquals("2003-01-10T15:33:20", fdf.format(date1));
            assertEquals("2003-01-10T15:33:20", fdf.format(cal1));
            assertEquals("2003-01-10T15:33:20", fdf.format(millis1));
            assertEquals("2003-07-10T09:00:00", fdf.format(date2));
            assertEquals("2003-07-10T09:00:00", fdf.format(cal2));
            assertEquals("2003-07-10T09:00:00", fdf.format(millis2));
            fdf = FastDateFormat.getInstance("Z");
            assertEquals("-0500", fdf.format(date1));
            assertEquals("-0500", fdf.format(cal1));
            assertEquals("-0500", fdf.format(millis1));
            fdf = FastDateFormat.getInstance("Z");
            assertEquals("-0400", fdf.format(date2));
            assertEquals("-0400", fdf.format(cal2));
            assertEquals("-0400", fdf.format(millis2));
            fdf = FastDateFormat.getInstance("ZZ");
            assertEquals("-05:00", fdf.format(date1));
            assertEquals("-05:00", fdf.format(cal1));
            assertEquals("-05:00", fdf.format(millis1));
            fdf = FastDateFormat.getInstance("ZZ");
            assertEquals("-04:00", fdf.format(date2));
            assertEquals("-04:00", fdf.format(cal2));
            assertEquals("-04:00", fdf.format(millis2));
            String pattern = "GGGG GGG GG G yyyy yyy yy y MMMM MMM MM M" + " dddd ddd dd d DDDD DDD DD D EEEE EEE EE E aaaa aaa aa a zzzz zzz zz z";
            fdf = FastDateFormat.getInstance(pattern);
            sdf = new SimpleDateFormat(pattern);
            assertEquals(sdf.format(date1), fdf.format(date1));
            assertEquals(sdf.format(date2), fdf.format(date2));
        } finally {
            Locale.setDefault(realDefaultLocale);
            TimeZone.setDefault(realDefaultZone);
        }
    }

    /**
   * Test case for {@link FastDateFormat#getDateInstance(int, java.util.Locale)}.
   */
    @Test
    public void testShortDateStyleWithLocales() {
        Locale usLocale = Locale.US;
        Locale swedishLocale = new Locale("sv", "SE");
        Calendar cal = Calendar.getInstance();
        cal.set(2004, 1, 3);
        FastDateFormat fdf = FastDateFormat.getDateInstance(FastDateFormat.SHORT, usLocale);
        assertEquals("2/3/04", fdf.format(cal));
        fdf = FastDateFormat.getDateInstance(FastDateFormat.SHORT, swedishLocale);
        assertEquals("2004-02-03", fdf.format(cal));
    }

    /**
   * Tests that pre-1000AD years get padded with yyyy
   */
    @Test
    public void testLowYearPadding() {
        Calendar cal = Calendar.getInstance();
        FastDateFormat format = FastDateFormat.getInstance("yyyy/MM/DD");
        cal.set(1, 0, 1);
        assertEquals("0001/01/01", format.format(cal));
        cal.set(10, 0, 1);
        assertEquals("0010/01/01", format.format(cal));
        cal.set(100, 0, 1);
        assertEquals("0100/01/01", format.format(cal));
        cal.set(999, 0, 1);
        assertEquals("0999/01/01", format.format(cal));
    }

    /**
   * Show Bug #39410 is solved
   */
    @Test
    public void testMilleniumBug() {
        Calendar cal = Calendar.getInstance();
        FastDateFormat format = FastDateFormat.getInstance("dd.MM.yyyy");
        cal.set(1000, 0, 1);
        assertEquals("01.01.1000", format.format(cal));
    }

    @Test
    public void testSimpleDate() {
        Calendar cal = Calendar.getInstance();
        FastDateFormat format = FastDateFormat.getInstance("yyyy/MM/dd");
        cal.set(2004, 11, 31);
        assertEquals("2004/12/31", format.format(cal));
        cal.set(999, 11, 31);
        assertEquals("0999/12/31", format.format(cal));
        cal.set(1, 2, 2);
        assertEquals("0001/03/02", format.format(cal));
    }
}
