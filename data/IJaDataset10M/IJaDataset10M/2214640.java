package net.scharlie.lumberjack4logs.time;

import static org.junit.Assert.assertEquals;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import org.junit.BeforeClass;
import org.junit.Test;

public class ExtendedTimeStampParserTest {

    private static final int NUMBER_OF_TESTS = 1000000;

    private static final char NUL_CHAR = '\0';

    private static final String NUL_CHAR_STRING = "\0";

    private static final String DATE_FORMAT_01_1 = "yyyy-MM-dd HH:mm:ss.SSS" + NUL_CHAR;

    private static final String DATE_FORMAT_01_2 = "yy-MM-dd HH:mm:ss.SSS" + NUL_CHAR;

    private static final String DATE_FORMAT_01_3 = "y-M-d H:m:s.SSS" + NUL_CHAR;

    private static final String DATE_FORMAT_02_1 = "dd.MM.yyyy HH:mm:ss,SSS" + NUL_CHAR;

    private static final String DATE_FORMAT_02_2 = "dd.MM.yy HH:mm:ss,SSS" + NUL_CHAR;

    private static final String DATE_FORMAT_03_1 = "dd.MM.yyyy/SSS" + NUL_CHAR + "/HH:mm:ss";

    private static final String DATE_FORMAT_03_2 = "dd.MM.yy/SSS" + NUL_CHAR + "/HH:mm:ss";

    private static final String DATE_FORMAT_04_1 = "yyyy-MM-dd'SSS'HH:mm:ss.SSS" + NUL_CHAR;

    private static final String DATE_FORMAT_04_2 = "yy-MM-dd'SSS'HH:mm:ss.SSS" + NUL_CHAR;

    private static final String DATE_FORMAT_05_1 = "HH:mm:ss.SSS" + NUL_CHAR;

    private static final String DATE_FORMAT_05_2 = "H:m:s.SSS" + NUL_CHAR;

    private static final String DATE_FORMAT_06_1 = "yyyyMMddHHmmssSSS" + NUL_CHAR;

    private static final String DATE_FORMAT_07_1 = "yyMMddHHmmssSSS" + NUL_CHAR;

    private static final String DATE_FORMAT_08_1 = "yyyyMMSSS" + NUL_CHAR + "ddHHmmss";

    private static final String DATE_FORMAT_09_1 = "H''m'H's.SSS" + NUL_CHAR;

    private static final String DATE_FORMAT_10_1 = "yyyy-MMM-dd HH:mm:ss.SSS" + NUL_CHAR;

    private static final String DATE_FORMAT_11_1 = "yyyy-DDD HH:mm:ss.SSS" + NUL_CHAR;

    private static final String DATE_FORMAT_12_1 = "yyyy-DDD hh:mm:ss.SSS" + NUL_CHAR + "a";

    private static final String DATE_FORMAT_13_1 = "SSS" + NUL_CHAR;

    private static final String[][] DATE_FORMATS = { { DATE_FORMAT_01_1, DATE_FORMAT_01_2, DATE_FORMAT_01_3 }, { DATE_FORMAT_02_1, DATE_FORMAT_02_2 }, { DATE_FORMAT_03_1, DATE_FORMAT_03_2 }, { DATE_FORMAT_04_1, DATE_FORMAT_04_2 }, { DATE_FORMAT_05_1, DATE_FORMAT_05_2 }, { DATE_FORMAT_06_1 }, { DATE_FORMAT_07_1 }, { DATE_FORMAT_08_1 }, { DATE_FORMAT_09_1 }, { DATE_FORMAT_10_1 }, { DATE_FORMAT_11_1 }, { DATE_FORMAT_12_1 }, { DATE_FORMAT_13_1 } };

    private static final NumberFormat PICO_FORMATTER = new DecimalFormat("000000000");

    private static Random random;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        random = new Random(System.currentTimeMillis());
    }

    @Test
    public void testParse1() {
        final ExtendedTimeStampParser p = new ExtendedTimeStampParser(DATE_FORMAT_01_1.replaceAll(NUL_CHAR_STRING, ""));
        assertEquals("\npattern = \"" + p.getSimpleDatePattern() + "\", \"" + p.getAdditionalDigitsPattern() + "\"\ninput = \"null\"\n", new ExtendedTimeStamp(), p.parse(null));
    }

    @Test
    public void testParse2() {
        for (int i = 0; i < NUMBER_OF_TESTS; i++) {
            final String[] ff = i < NUMBER_OF_TESTS / 10 ? randomSelectedDateFormats() : randomGeneratedDateFormats();
            final String[] dd = randomDateTimeStrings(ff[0]);
            final ExtendedTimeStampParser p = new ExtendedTimeStampParser(ff[1]);
            final ExtendedTimeStamp expected = p.parse(dd[0], dd[1]);
            final ExtendedTimeStamp actual = p.parse(dd[2]);
            final String msg = "\n(Test " + i + ") pattern = \"" + p.getSimpleDatePattern() + "\", \"" + p.getAdditionalDigitsPattern() + "\"\ninput = \"" + dd[0] + "\" \"" + dd[1] + "\", \"" + dd[2] + "\"\n";
            if (random.nextInt(NUMBER_OF_TESTS) % (NUMBER_OF_TESTS / 10) == 0) {
                System.out.println(msg + "output = \"" + actual + "\"");
            }
            assertEquals(msg, expected, actual);
        }
    }

    private static String[] randomSelectedDateFormats() {
        final String[] ff = DATE_FORMATS[random.nextInt(DATE_FORMATS.length)];
        final String f0 = ff[random.nextInt(ff.length)];
        final String f1 = ff[random.nextInt(ff.length)].replaceAll(NUL_CHAR_STRING, "");
        return new String[] { f0, f1 };
    }

    private static String[] randomGeneratedDateFormats() {
        final String f0 = createRandomDateFormat();
        final String f1 = f0.replaceAll(NUL_CHAR_STRING, "");
        return new String[] { f0, f1 };
    }

    private static String[] randomDateTimeStrings(String dateFormat) {
        final String d1 = formatTimeStamp(dateFormat, (long) (random.nextDouble() * (new Date().getTime())));
        String d2 = random.nextBoolean() ? "" : null;
        if (d1.indexOf(NUL_CHAR) >= 0) {
            d2 = formatAdditionalDigitsWithRandomLength(random.nextInt(1000000000));
        }
        return new String[] { d1.replaceAll(NUL_CHAR_STRING, ""), d2, d1.replaceAll(NUL_CHAR_STRING, d2) };
    }

    private static String formatTimeStamp(final String pattern, long timeStamp) {
        return new SimpleDateFormat(pattern).format(new Date(timeStamp));
    }

    private static String formatAdditionalDigitsWithRandomLength(int picoSeconds) {
        return PICO_FORMATTER.format(picoSeconds).substring(0, random.nextInt(10));
    }

    private static String createRandomDateFormat() {
        boolean eraDone = false;
        boolean yearDone = false;
        boolean monthDone = false;
        boolean weekDone = false;
        boolean dayDone = false;
        boolean dayInWeekDone = false;
        boolean hourDone = false;
        boolean amPmMarkerDone = false;
        boolean minuteDone = false;
        boolean secondDone = false;
        boolean millisDone = false;
        boolean timeZoneDone = false;
        StringBuilder b = new StringBuilder();
        int iEnd = 10 + random.nextInt(41);
        for (int i = 0; i < iEnd; i++) {
            switch(random.nextInt(17)) {
                case 0:
                    if (!eraDone) {
                        addRandomFormat(b, 'G', 1, 10);
                        eraDone = true;
                    }
                    break;
                case 1:
                    if (!yearDone) {
                        if (random.nextBoolean()) {
                            b.append(random.nextBoolean() ? "yy" : "yyyy");
                        } else {
                            addRandomSeparator(b);
                            addRandomFormat(b, 'y', 1, 4);
                            addRandomSeparator(b);
                        }
                        yearDone = true;
                    }
                    break;
                case 2:
                    if (!monthDone) {
                        if (random.nextBoolean()) {
                            addRandomFormat(b, 'M', 2, 10);
                        } else {
                            addRandomSeparator(b);
                            addRandomFormat(b, 'M', 1, 10);
                            addRandomSeparator(b);
                        }
                        monthDone = true;
                    }
                    break;
                case 3:
                    if (!weekDone && !monthDone) {
                        if (random.nextBoolean()) {
                            b.append("ww");
                        } else {
                            addRandomSeparator(b);
                            addRandomFormat(b, 'w', 1, 2);
                            addRandomSeparator(b);
                        }
                        monthDone = true;
                        weekDone = true;
                    }
                    break;
                case 4:
                    if (!weekDone) {
                        b.append('W');
                        weekDone = true;
                    }
                    break;
                case 5:
                    if (!dayDone && !weekDone && !monthDone) {
                        if (random.nextBoolean()) {
                            b.append("DDD");
                        } else {
                            addRandomSeparator(b);
                            addRandomFormat(b, 'D', 1, 3);
                            addRandomSeparator(b);
                        }
                        monthDone = true;
                        weekDone = true;
                        dayDone = true;
                    }
                    break;
                case 6:
                    if (!dayDone && !weekDone) {
                        if (random.nextBoolean()) {
                            b.append("dd");
                        } else {
                            addRandomSeparator(b);
                            addRandomFormat(b, 'd', 1, 2);
                            addRandomSeparator(b);
                        }
                        weekDone = true;
                        dayDone = true;
                    }
                    break;
                case 7:
                    if (!dayDone) {
                        b.append('F');
                        dayDone = true;
                    }
                    break;
                case 8:
                    if (!dayInWeekDone) {
                        addRandomFormat(b, 'E', 1, 10);
                        dayInWeekDone = true;
                    }
                    break;
                case 9:
                    if (!hourDone && !amPmMarkerDone) {
                        if (random.nextBoolean()) {
                            b.append(random.nextBoolean() ? "HH" : "kk");
                        } else {
                            addRandomSeparator(b);
                            addRandomFormat(b, random.nextBoolean() ? 'H' : 'k', 1, 2);
                            addRandomSeparator(b);
                        }
                        hourDone = true;
                        amPmMarkerDone = true;
                    }
                    break;
                case 10:
                    if (!hourDone) {
                        if (random.nextBoolean()) {
                            b.append(random.nextBoolean() ? "KK" : "hh");
                        } else {
                            addRandomSeparator(b);
                            addRandomFormat(b, random.nextBoolean() ? 'K' : 'h', 1, 2);
                            addRandomSeparator(b);
                        }
                        hourDone = true;
                    }
                    break;
                case 11:
                    if (!amPmMarkerDone) {
                        b.append('a');
                        amPmMarkerDone = true;
                    }
                    break;
                case 12:
                    if (!minuteDone) {
                        if (random.nextBoolean()) {
                            b.append("mm");
                        } else {
                            addRandomSeparator(b);
                            addRandomFormat(b, 'm', 1, 2);
                            addRandomSeparator(b);
                        }
                        minuteDone = true;
                    }
                    break;
                case 13:
                    if (!secondDone) {
                        if (random.nextBoolean()) {
                            b.append("ss");
                        } else {
                            addRandomSeparator(b);
                            addRandomFormat(b, 's', 1, 2);
                            addRandomSeparator(b);
                        }
                        secondDone = true;
                    }
                    break;
                case 14:
                    if (!millisDone) {
                        b.append("SSS" + NUL_CHAR);
                        millisDone = true;
                    }
                    break;
                case 15:
                    if (!timeZoneDone) {
                        b.append(random.nextBoolean() ? 'z' : 'Z');
                        timeZoneDone = true;
                    }
                    break;
                default:
                    if (random.nextBoolean()) {
                        addRandomSeparator(b);
                    }
                    break;
            }
        }
        return b.toString();
    }

    private static void addRandomFormat(StringBuilder b, char c, int min, int max) {
        int n = min < max ? (min + random.nextInt(max - min + 1)) : min;
        for (int i = 0; i < n; i++) {
            b.append(c);
        }
    }

    private static void addRandomSeparator(StringBuilder b) {
        if (random.nextBoolean()) {
            addRandomQuotedSeparator(b);
        } else {
            addRandomNonQuotedSeparator(b);
        }
    }

    private static void addRandomQuotedSeparator(StringBuilder b) {
        b.append('\'');
        int n = random.nextInt(11);
        for (int i = 0; i < n; i++) {
            b.append(char2String((i == 0 || i == n - 1) ? randomSeparatorChar() : randomPrintableChar()));
        }
        b.append('\'');
    }

    private static void addRandomNonQuotedSeparator(StringBuilder b) {
        int n = 1 + random.nextInt(10);
        for (int i = 0; i < n; i++) {
            b.append(char2String(randomSeparatorChar()));
        }
    }

    private static char randomSeparatorChar() {
        while (true) {
            final char c = randomPrintableChar();
            if (!Character.isLetterOrDigit(c)) {
                return c;
            }
        }
    }

    private static char randomPrintableChar() {
        return (char) (32 + random.nextInt(95));
    }

    private static String char2String(final char c) {
        final StringBuilder b = new StringBuilder();
        b.append(c);
        if (c == '\'' || c == '\\') {
            b.append(c);
        }
        return b.toString();
    }
}
