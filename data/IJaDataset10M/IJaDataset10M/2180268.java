package br.com.devx.test;

import java.io.File;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.GregorianCalendar;

/**
 * User: gandralf
 * Date: Jan 6, 2003
 * Time: 10:46:23 PM
 */
public class JUnitHelper {

    private static String s_testHome;

    private static NumberFormat s_nf;

    private static DateFormat s_df = DateFormat.getDateInstance(DateFormat.MEDIUM);

    private static DateFormat s_dtf = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);

    static {
        s_nf = NumberFormat.getInstance();
    }

    public static String getHomeDir() {
        if (s_testHome == null) {
            String testHome = System.getProperty("test.home");
            if (testHome == null) {
                testHome = "src/test/webapp";
            }
            File file = new File(testHome + "/WEB-INF");
            if (!file.exists()) {
                throw new IllegalStateException("Test home not found: " + testHome + ". Please, define test.home system environment or change the working directory");
            }
            s_testHome = testHome;
        }
        return s_testHome;
    }

    public static String dateString(int year, int month, int day) {
        return s_df.format(new GregorianCalendar(year, month, day).getTime());
    }

    public static String dateString(int year, int month, int day, int hour, int minute, int second) {
        return s_dtf.format(new GregorianCalendar(year, month, day, hour, minute, second).getTime());
    }

    public static String doubleString(double value) {
        return s_nf.format(value);
    }

    public static String doubleString(double value, int minimumFractionDigits, int maximumFractionDigits) {
        final NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(minimumFractionDigits);
        nf.setMaximumFractionDigits(maximumFractionDigits);
        return nf.format(value);
    }
}
