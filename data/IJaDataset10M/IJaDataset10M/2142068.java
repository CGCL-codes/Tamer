package org.das2.util;

/**
 * ExceptionHandler that prints stack traces out to the stderr.
 * @author jbf
 */
public class ConsoleExceptionHandler implements ExceptionHandler {

    /** Creates a new instance of ConsoleExceptionHandler */
    public ConsoleExceptionHandler() {
    }

    public void handle(Throwable t) {
        t.printStackTrace();
    }

    public void handleUncaught(Throwable t) {
        t.printStackTrace();
    }
}
