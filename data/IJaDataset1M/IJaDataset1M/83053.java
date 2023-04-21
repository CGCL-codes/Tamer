package mobi.ilabs;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Misc static helpers.
 *
 */
public final class Misc {

    private Misc() {
    }

    /**
     * Extract the stacktrace from an exception into a string.
     * @param e The exception
     * @return A string containing the stacktrace.
     */
    public static String stacktraceToString(final Exception e) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }

    /**
     * Produce a simple report describing an exception, containing 
     * a short introductory text, the string representation of the exception,
     * and a stacktrace from the exception.
     */
    public static String stacktraceToReport(final String text, final Exception e) {
        return text + e.toString() + "\n" + stacktraceToString(e);
    }
}
