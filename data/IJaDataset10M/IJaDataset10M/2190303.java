package org.openware.job.data;

import java.util.Date;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Locale;

/**
 * High-speed date formatting functions optimized
 * for use with web server applications.  In particular, this
 * provides several optimizations over the standard Java library
 * date formatting routines:
 * <UL>
* <LI>Unsynchronized.  Multiple threads can call these functions without
 *     waiting for synchronized functions.
 * <LI>Optimized formatting for specific date formats: no need to wait
 *     for general-purpose parsing code.
 * <LI>Provides char-array and byte-array outputs.  Less object-creation
 *     overhead than Strings or StringBuffers.  Also, byte-array versions
 *     are already ISO8859_1-encoded, so they can be output to
 *     sockets or disk files without waiting for char-to-byte conversions.
 * <LI>"now" versions return a pre-formatted current time, which is cached
 *     internally so that the actual formatting only occurs at most once
 *     per second.
 * </UL>
*/
class DateFormat {

    private static final int MILLIS = 0;

    private static final int SECOND = 1;

    private static final int MINUTE = 2;

    private static final int HOUR = 3;

    private static final int DAY = 4;

    private static final int MONTH = 5;

    private static final int YEAR = 6;

    private static final int WEEKDAY = 7;

    private static final int TIME_SIZE = 8;

    /**
     * Convert the provided timestamp (in Java-standard milliseconds
     * since 00:00:00 1 Jan 1970) into date components using
     * Gregorian proleptic calendar.  This is correct for any time
     * from 1 A.D. to the positive limit of timeMillis.
     * Note: This uses Gregorian proleptic prior to October 15, 1582,
     * unlike java.text.GregorianCalendar, which uses Julian
     * proleptic for those dates.
     */
    private static int[] breakDownTime(long timeMillis) {
        int[] components = new int[TIME_SIZE];
        long s = timeMillis;
        if (s >= 0) {
            components[MILLIS] = (int) (s % 1000);
            s /= 1000;
        } else {
            components[MILLIS] = (int) (s % 1000);
            if (components[MILLIS] < 0) {
                components[MILLIS] += 1000;
            }
            s = (s - 999) / 1000;
        }
        int d = (int) (s / 86400);
        s %= 86400;
        if (s < 0) {
            s += 86400;
            d--;
        }
        int m = (int) (s / 60), h = m / 60;
        m %= 60;
        s %= 60;
        components[MINUTE] = m;
        components[HOUR] = h;
        components[SECOND] = (int) s;
        d += 1969 * 365 + 492 - 19 + 4;
        components[WEEKDAY] = (d + 1) % 7;
        int y = 400 * (d / 146097) + 1;
        d %= 146097;
        if (d == 146096) {
            y += 399;
            d = 365;
        } else {
            y += 100 * (d / 36524);
            d %= 36524;
            y += 4 * (d / 1461);
            d %= 1461;
            if (d == 1460) {
                y += 3;
                d = 365;
            } else {
                y += d / 365;
                d %= 365;
            }
        }
        components[YEAR] = y;
        boolean isleap = ((y % 4 == 0) && !(y % 100 == 0)) || (y % 400 == 0);
        if (!isleap && (d >= 59)) d++;
        if (d >= 60) d++;
        int mon = ((d % 214) / 61) * 2 + ((d % 214) % 61) / 31;
        if (d > 213) mon += 7;
        components[MONTH] = mon;
        d = ((d % 214) % 61) % 31 + 1;
        components[DAY] = d;
        return components;
    }

    /**
     * Format the specified time into a character array using
     * RFC1123 formatting conventions.  A typical RFC1123 date
     * looks like: "Sun, 06 Nov 1994 08:49:37 GMT"
     */
    public static char[] formatRFC1123_char(long timeMillis) {
        char[] f = new char[24];
        formatRFC1123_char_internal(timeMillis, f);
        return f;
    }

    /**
     * Internal time-to-byte-array formatter for RFC1123.  This assumes
     * the provided byte array is at least 24 bytes long.
     * RFC 1123 date string: "Sun, 06 Nov 1994 08:49:37 GMT"
     */
    private static final void formatRFC1123_char_internal(long timeMillis, char[] f) {
        int[] components = breakDownTime(timeMillis);
        switch(components[WEEKDAY]) {
            case 0:
                f[0] = 'S';
                f[1] = 'u';
                f[2] = 'n';
                break;
            case 1:
                f[0] = 'M';
                f[1] = 'o';
                f[2] = 'n';
                break;
            case 2:
                f[0] = 'T';
                f[1] = 'u';
                f[2] = 'e';
                break;
            case 3:
                f[0] = 'W';
                f[1] = 'e';
                f[2] = 'd';
                break;
            case 4:
                f[0] = 'T';
                f[1] = 'h';
                f[2] = 'u';
                break;
            case 5:
                f[0] = 'F';
                f[1] = 'r';
                f[2] = 'i';
                break;
            case 6:
                f[0] = 'S';
                f[1] = 'a';
                f[2] = 't';
                break;
        }
        f[3] = ' ';
        f[4] = (char) (components[DAY] / 10 + '0');
        f[5] = (char) (components[DAY] % 10 + '0');
        f[6] = ' ';
        switch(components[MONTH]) {
            case 0:
                f[7] = 'J';
                f[8] = 'a';
                f[9] = 'n';
                break;
            case 1:
                f[7] = 'F';
                f[8] = 'e';
                f[9] = 'b';
                break;
            case 2:
                f[7] = 'M';
                f[8] = 'a';
                f[9] = 'r';
                break;
            case 3:
                f[7] = 'A';
                f[8] = 'p';
                f[9] = 'r';
                break;
            case 4:
                f[7] = 'M';
                f[8] = 'a';
                f[9] = 'y';
                break;
            case 5:
                f[7] = 'J';
                f[8] = 'u';
                f[9] = 'n';
                break;
            case 6:
                f[7] = 'J';
                f[8] = 'u';
                f[9] = 'l';
                break;
            case 7:
                f[7] = 'A';
                f[8] = 'u';
                f[9] = 'g';
                break;
            case 8:
                f[7] = 'S';
                f[8] = 'e';
                f[9] = 'p';
                break;
            case 9:
                f[7] = 'O';
                f[8] = 'c';
                f[9] = 't';
                break;
            case 10:
                f[7] = 'N';
                f[8] = 'o';
                f[9] = 'v';
                break;
            case 11:
                f[7] = 'D';
                f[8] = 'e';
                f[9] = 'c';
                break;
        }
        f[10] = ' ';
        f[11] = (char) (components[YEAR] / 1000 + '0');
        f[12] = (char) ((components[YEAR] / 100) % 10 + '0');
        f[13] = (char) ((components[YEAR] / 10) % 10 + '0');
        f[14] = (char) (components[YEAR] % 10 + '0');
        f[15] = ' ';
        f[16] = (char) (components[HOUR] / 10 + '0');
        f[17] = (char) (components[HOUR] % 10 + '0');
        f[18] = ':';
        f[19] = (char) (components[MINUTE] / 10 + '0');
        f[20] = (char) (components[MINUTE] % 10 + '0');
        f[21] = ':';
        f[22] = (char) (components[SECOND] / 10 + '0');
        f[23] = (char) (components[SECOND] % 10 + '0');
    }

    /**
     * A very efficient form of formatRFC1123_char(System.currentTimeMillis()).
     * This caches the formatted time internally so that repeated calls
     * can usually avoid the formatting overhead entirely. Note that
     * this returns a direct reference to an internal array; DO NOT MODIFY THE
     * RETURNED ARRAY!
     *
     * <P>A note about synchronization: when the second changes, it is
     * possible for multiple copies of this function to invoke the formatter.
     * This does not create any conflicts, since the only field that
     * can't be atomically updated is the long seconds record, and
     * everyone will be writing the same value.  At worst, duplicate updates
     * create a slight performance penalty, but that's probably outweighed
     * by the performance gain of an unsynchronized implementation
     * (acquiring locks takes time, and synchronization stalls threads).</P>
*
     * <P>RFC1123-format dates don't include a millisecond field,
     * so only need updating once/second.  At only once/second, you
     * can afford a full update and you can afford allocating a new
     * char array for each update.  If you want to create a version
     * that includes a millisecond field, you can optimize
     * most updates to modify the millisecond digits directly
     * in the array and only do a full allocation and update once per
     * second.  This does introduce some complications for clients,
     * who might not expect the array to keep changing.</P>
*/
    public static char[] nowRFC1123_char() {
        long now = System.currentTimeMillis();
        long nowSeconds = now / 1000;
        if (nowSeconds == cachedNowSeconds_char) return cachedNowRFC1123_char;
        char[] newFormatted = new char[24];
        formatRFC1123_char_internal(now, newFormatted);
        cachedNowRFC1123_char = newFormatted;
        cachedNowSeconds_char = nowSeconds;
        return cachedNowRFC1123_char;
    }

    private static long cachedNowSeconds_char;

    private static char[] cachedNowRFC1123_char;

    /**
     * Returns an RFC 1123-format date string in a byte array.
     * This is already encoded using ISO8859_1, and is suitable
     * for writing to an HTTP response stream.  Building and emitting
     * byte versions directly avoids the overhead of char-to-byte
     * conversions. RFC 1123 format: "Sun, 06 Nov 1994 08:49:37 GMT" 
     */
    public static byte[] formatRFC1123_byte(long timeMillis) {
        byte[] f = new byte[24];
        formatRFC1123_byte_internal(timeMillis, f);
        return f;
    }

    /**
     * Internal time-to-byte-array formatter for RFC1123.  This assumes
     * the provided byte array is at least 24 bytes long.
     */
    private static final void formatRFC1123_byte_internal(long timeMillis, byte[] f) {
        int[] components = breakDownTime(timeMillis);
        switch(components[WEEKDAY]) {
            case 0:
                f[0] = (byte) 'S';
                f[1] = (byte) 'u';
                f[2] = (byte) 'n';
                break;
            case 1:
                f[0] = (byte) 'M';
                f[1] = (byte) 'o';
                f[2] = (byte) 'n';
                break;
            case 2:
                f[0] = (byte) 'T';
                f[1] = (byte) 'u';
                f[2] = (byte) 'e';
                break;
            case 3:
                f[0] = (byte) 'W';
                f[1] = (byte) 'e';
                f[2] = (byte) 'd';
                break;
            case 4:
                f[0] = (byte) 'T';
                f[1] = (byte) 'h';
                f[2] = (byte) 'u';
                break;
            case 5:
                f[0] = (byte) 'F';
                f[1] = (byte) 'r';
                f[2] = (byte) 'i';
                break;
            case 6:
                f[0] = (byte) 'S';
                f[1] = (byte) 'a';
                f[2] = (byte) 't';
                break;
        }
        f[3] = (byte) ' ';
        f[4] = (byte) (components[DAY] / 10 + '0');
        f[5] = (byte) (components[DAY] % 10 + '0');
        f[6] = (byte) ' ';
        switch(components[MONTH]) {
            case 0:
                f[7] = (byte) 'J';
                f[8] = (byte) 'a';
                f[9] = (byte) 'n';
                break;
            case 1:
                f[7] = (byte) 'F';
                f[8] = (byte) 'e';
                f[9] = (byte) 'b';
                break;
            case 2:
                f[7] = (byte) 'M';
                f[8] = (byte) 'a';
                f[9] = (byte) 'r';
                break;
            case 3:
                f[7] = (byte) 'A';
                f[8] = (byte) 'p';
                f[9] = (byte) 'r';
                break;
            case 4:
                f[7] = (byte) 'M';
                f[8] = (byte) 'a';
                f[9] = (byte) 'y';
                break;
            case 5:
                f[7] = (byte) 'J';
                f[8] = (byte) 'u';
                f[9] = (byte) 'n';
                break;
            case 6:
                f[7] = (byte) 'J';
                f[8] = (byte) 'u';
                f[9] = (byte) 'l';
                break;
            case 7:
                f[7] = (byte) 'A';
                f[8] = (byte) 'u';
                f[9] = (byte) 'g';
                break;
            case 8:
                f[7] = (byte) 'S';
                f[8] = (byte) 'e';
                f[9] = (byte) 'p';
                break;
            case 9:
                f[7] = (byte) 'O';
                f[8] = (byte) 'c';
                f[9] = (byte) 't';
                break;
            case 10:
                f[7] = (byte) 'N';
                f[8] = (byte) 'o';
                f[9] = (byte) 'v';
                break;
            case 11:
                f[7] = (byte) 'D';
                f[8] = (byte) 'e';
                f[9] = (byte) 'c';
                break;
        }
        f[10] = (byte) ' ';
        f[11] = (byte) (components[YEAR] / 1000 + '0');
        f[12] = (byte) ((components[YEAR] / 100) % 10 + '0');
        f[13] = (byte) ((components[YEAR] / 10) % 10 + '0');
        f[14] = (byte) (components[YEAR] % 10 + '0');
        f[15] = (byte) ' ';
        f[16] = (byte) (components[HOUR] / 10 + '0');
        f[17] = (byte) (components[HOUR] % 10 + '0');
        f[18] = (byte) ':';
        f[19] = (byte) (components[MINUTE] / 10 + '0');
        f[20] = (byte) (components[MINUTE] % 10 + '0');
        f[21] = (byte) ':';
        f[22] = (byte) (components[SECOND] / 10 + '0');
        f[23] = (byte) (components[SECOND] % 10 + '0');
    }

    /**
     * A very efficient form of formatRFC1123_byte(System.currentTimeMillis()).
     * This caches the formatted time internally so that repeated calls
     * can usually avoid the formatting overhead entirely. Note that
     * this returns a direct reference to an internal array; DO NOT MODIFY THE
     * RETURNED ARRAY!
     */
    public static byte[] nowRFC1123_byte() {
        long now = System.currentTimeMillis();
        long nowSeconds = now / 1000;
        if (nowSeconds == cachedNowSeconds_byte) return cachedNowRFC1123_byte;
        byte[] newFormatted = new byte[24];
        formatRFC1123_byte_internal(now, newFormatted);
        cachedNowRFC1123_byte = newFormatted;
        cachedNowSeconds_byte = nowSeconds;
        return cachedNowRFC1123_byte;
    }

    private static long cachedNowSeconds_byte;

    private static byte[] cachedNowRFC1123_byte;

    public static String format(Date date) {
        Calendar cal = null;
        int offset;
        TimeZone tz = null;
        tz = TimeZone.getDefault();
        cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        cal.setTime(date);
        offset = tz.getOffset(cal.get(Calendar.ERA), cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.DAY_OF_WEEK), cal.get(Calendar.MILLISECOND));
        return new String(formatRFC1123_char(date.getTime() + offset));
    }

    private static final java.text.DateFormat rfc1123ComparisonFormat = new java.text.SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", java.util.Locale.US);

    private static void testit(long milliseconds) {
        String ref = rfc1123ComparisonFormat.format(new java.util.Date(milliseconds)).substring(0, 24);
        String n = new String(formatRFC1123_char(milliseconds));
        if (!n.equals(ref)) {
            System.out.println("New: " + n);
            System.out.println("Ref:  " + ref);
            System.out.println("Time: " + milliseconds + " Seconds: " + (milliseconds / 1000 % 86400) + " Milliseconds: " + (milliseconds % 1000));
        }
        String n2 = new String(formatRFC1123_byte(milliseconds));
        if (!n2.equals(ref)) {
            System.out.println("New (byte): " + n2);
            System.out.println("Ref:  " + ref);
            System.out.println("Time: " + milliseconds + " Seconds: " + (milliseconds / 1000 % 86400) + " Milliseconds: " + (milliseconds % 1000));
        }
    }

    public static void main(String[] args) {
        System.out.println("today = " + format(new Date()));
        System.out.println("Self-test for new date formatting code.");
        System.out.println("Tests assume that your default platform encoding is ISO8859_1");
        System.out.println("and your current time zone is GMT.");
        testit(System.currentTimeMillis());
        testit((new java.util.Date(69, 12, 30)).getTime());
        testit((new java.util.Date(69, 12, 31)).getTime());
        {
            System.out.println("Timing new date formatter");
            long start = System.currentTimeMillis();
            long conversions = 0;
            for (int y = 70; y < 120; y++) {
                for (int m = 0; m < 12; m++) {
                    for (int d = 1; d < 31; d++) {
                        char[] f = formatRFC1123_char((new java.util.Date(y, m, d)).getTime());
                        conversions++;
                    }
                }
            }
            System.err.println("New: " + conversions + " conversions in " + (System.currentTimeMillis() - start) + " milliseconds");
        }
        {
            System.out.println("Timing reference code");
            long start = System.currentTimeMillis();
            long conversions = 0;
            for (int y = 70; y < 120; y++) {
                for (int m = 0; m < 12; m++) {
                    for (int d = 1; d < 31; d++) {
                        String std = rfc1123ComparisonFormat.format(new java.util.Date(y, m, d));
                        conversions++;
                    }
                }
            }
            System.err.println("System: " + conversions + " conversions in " + (System.currentTimeMillis() - start) + " milliseconds");
        }
        {
            System.out.println("Timing cached now() code");
            long start = System.currentTimeMillis();
            int conversions = 200000;
            for (int i = 0; i < conversions; i++) {
                char[] f = nowRFC1123_char();
            }
            System.err.println("Cached: " + conversions + " conversions in " + (System.currentTimeMillis() - start) + " milliseconds");
        }
        {
            System.out.println("Timing cached now() code plus char-to-byte conversion");
            long start = System.currentTimeMillis();
            int conversions = 200000;
            for (int i = 0; i < conversions; i++) {
                char[] f = nowRFC1123_char();
                byte[] b = new String(f).getBytes();
            }
            System.err.println("Cached: " + conversions + " conversions in " + (System.currentTimeMillis() - start) + " milliseconds");
        }
        {
            System.out.println("Timing: new String(\"this is a test\").getBytes()");
            long start = System.currentTimeMillis();
            int conversions = 200000;
            for (int i = 0; i < conversions; i++) {
                byte[] b = new String("this is a test").getBytes();
            }
            System.err.println("Cached: " + conversions + " conversions in " + (System.currentTimeMillis() - start) + " milliseconds");
        }
        System.out.println("Comparing new code to standard code for every day from 1850 to 2150");
        {
            long t = (new java.util.Date(-50, 0, 1)).getTime();
            for (int y = 0; y < 300; y++) {
                System.out.println("Year: " + (y + 1850));
                for (int m = 0; m < 12; m++) {
                    for (int d = 1; d < 31; d++) {
                        testit(t);
                        t += 86397;
                    }
                }
            }
        }
        System.out.println("Comparing for every millisecond around 1969/1970 sign change");
        {
            for (long t = -50000; t < 50000; t++) testit(t);
        }
    }
}
