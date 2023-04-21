package net.sf.katta.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Logger {

    private static boolean DEBUG = false;

    private static boolean ERROR = false;

    private static boolean INFO = false;

    private static boolean WARN = false;

    static {
        String logLevel = System.getProperty("KATTA_LOG_LEVEL");
        if (logLevel == null) {
            logLevel = "DEBUG";
        }
        if (logLevel.equalsIgnoreCase("DEBUG")) {
            DEBUG = true;
            ERROR = true;
            INFO = true;
            WARN = true;
        } else if (logLevel.equalsIgnoreCase("ERROR")) {
            ERROR = true;
        } else if (logLevel.equalsIgnoreCase("INFO")) {
            INFO = true;
            ERROR = true;
            WARN = true;
        } else if (logLevel.equalsIgnoreCase("WARN")) {
            WARN = true;
            ERROR = true;
        }
    }

    public static void info(final String string, final Throwable t) {
        if (INFO) {
            System.out.println(string + " \t" + stringifyException(t));
        }
    }

    private static String stringifyException(final Throwable t) {
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);
        t.printStackTrace(printWriter);
        printWriter.close();
        return stringWriter.toString();
    }

    public static void warn(final String string, final Throwable t) {
        if (WARN) {
            System.err.println(string + " \t" + stringifyException(t));
        }
    }

    public static void info(final String string) {
        if (INFO) {
            System.out.println(string);
        }
    }

    public static void debug(final String string) {
        if (DEBUG) {
            System.out.println(string);
        }
    }

    public static void warn(final String string) {
        if (WARN) {
            System.out.println(string);
        }
    }

    public static void error(final String string, final Throwable t) {
        if (ERROR) {
            System.err.println(string + " \t" + stringifyException(t));
        }
    }

    public static void error(final String string) {
        if (ERROR) {
            System.err.println(string);
        }
    }

    public static boolean isDebug() {
        return DEBUG;
    }

    public static boolean isError() {
        return ERROR;
    }

    public static boolean isInfo() {
        return INFO;
    }

    public static boolean isWarn() {
        return WARN;
    }
}
