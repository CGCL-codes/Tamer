package de.tud.kom.nat.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Generic logger class.
 *
 * @author Matthias Weinert
 */
public class Logger {

    public static final boolean PRINT_STACK = true;

    private static boolean initialized = false;

    private static PrintWriter pw = null;

    private static boolean loggingEnabled = true;

    private static PrintWriter getLogWriter() {
        if (!initialized) {
            initialized = true;
            try {
                pw = new PrintWriter(new File("imlog.txt"));
            } catch (FileNotFoundException e) {
            }
        }
        return pw;
    }

    public static void log(Object o) {
        writeToFile(o.toString());
        if (loggingEnabled) System.out.println(o.toString());
    }

    public static void logError(Exception e, Object desc) {
        StringBuilder sb = new StringBuilder();
        if (desc != null && desc.toString().length() > 0) sb.append(desc);
        if (e != null) {
            sb.append(e.getLocalizedMessage());
        }
        String logentry = sb.toString();
        writeToFile(logentry);
        if (getLogWriter() != null && e != null) e.printStackTrace(getLogWriter());
        if (loggingEnabled) {
            System.err.println(logentry);
            if (PRINT_STACK && e != null) e.printStackTrace();
        }
    }

    public static void enableLogging(boolean value) {
        loggingEnabled = value;
    }

    public static void logWarning(Object out) {
        String logentry = "Warning: " + out.toString();
        writeToFile(logentry);
        if (loggingEnabled) System.err.println(logentry);
    }

    public static boolean isEnabled() {
        return loggingEnabled;
    }

    private static void writeToFile(String s) {
        PrintWriter bw = getLogWriter();
        if (bw == null) return;
        bw.println(s);
        bw.flush();
    }
}
