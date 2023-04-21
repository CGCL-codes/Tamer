package org.robotframework.jvmconnector.common;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Wraps exception thrown by keyword in order to provide meaningful stacktrace
 * and error message for robot logging.
 */
public class TestFailedException extends RuntimeException {

    private static final long serialVersionUID = 711203696134553094L;

    private String sourceExceptionClassName;

    private String stackTraceAsString;

    public TestFailedException(Throwable cause) {
        super(cause.getMessage());
        emptyStackTrace();
        initSourceExceptionClassName(cause);
        initStackTraceString(cause);
    }

    /**
	 * @return message string containing the original detail message and the
	 *         class name of the source exception.
	 */
    public String getMessage() {
        String message = "caused by: " + sourceExceptionClassName;
        if (super.getMessage() == null) return message;
        return message + ": " + super.getMessage();
    }

    public String getSourceExceptionClassName() {
        return sourceExceptionClassName;
    }

    public void printStackTrace(PrintStream stream) {
        stream.print(stackTraceAsString);
    }

    public void printStackTrace(PrintWriter writer) {
        writer.write(stackTraceAsString);
    }

    private void initSourceExceptionClassName(Throwable cause) {
        sourceExceptionClassName = cause.getClass().getName();
    }

    private void initStackTraceString(Throwable cause) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        cause.printStackTrace(new PrintStream(output));
        stackTraceAsString = output.toString();
    }

    private void emptyStackTrace() {
        setStackTrace(new StackTraceElement[0]);
    }
}
