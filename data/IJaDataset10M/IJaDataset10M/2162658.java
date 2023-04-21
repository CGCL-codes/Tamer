package org.microemu.log;

import java.io.PrintStream;

/**
 * @author vlads
 * 
 */
public class StdOutAppender implements LoggerAppender {

    public static boolean enabled = true;

    public static String formatLocation(StackTraceElement ste) {
        if (ste == null) {
            return "";
        }
        return ste.getClassName() + "." + ste.getMethodName() + "(" + ste.getFileName() + ":" + ste.getLineNumber() + ")";
    }

    public void append(LoggingEvent event) {
        if (!enabled) {
            return;
        }
        PrintStream out = System.out;
        if (event.getLevel() == LoggingEvent.ERROR) {
            out = System.err;
        }
        String data = "";
        if (event.hasData()) {
            data = " [" + event.getFormatedData() + "]";
        }
        String location = formatLocation(event.getLocation());
        if (location.length() > 0) {
            location = "\n\t  " + location;
        }
        out.println(event.getMessage() + data + location);
        if (event.getThrowable() != null) {
            event.getThrowable().printStackTrace(out);
        }
    }
}
