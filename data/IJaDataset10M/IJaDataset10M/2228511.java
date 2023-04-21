package net.sourceforge.pebble.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * A collection of utility methods for manipulating exceptions.
 *
 * @author    Simon Brown
 */
public final class ExceptionUtils {

    /**
   * Given a Throwable, this method returns a String representation of the
   * complete stack trace.
   *
   * @param t   the Throwable from which to extract the stack trace
   * @return  a String representation of the stack trace
   */
    public static String getStackTraceAsString(Throwable t) {
        if (t != null) {
            StringWriter sw = new StringWriter();
            PrintWriter writer = new PrintWriter(sw);
            t.printStackTrace(writer);
            return sw.getBuffer().toString();
        } else {
            return "";
        }
    }
}
