package com.gamalocus.sgs.profile.listener.report;

import java.io.PrintWriter;
import java.io.Serializable;

public class RawThrowable implements Serializable {

    private static final long serialVersionUID = -2571185393204426301L;

    public String throwableClassName;

    public RawThrowable cause;

    public String message;

    public String localized_message;

    public StackTraceElement[] stack_trace;

    public RawThrowable getCause() {
        return cause;
    }

    public void printStackTrace(PrintWriter s) {
        s.println(throwableClassName + " " + message);
        for (int i = 0; i < stack_trace.length; i++) {
            s.println("\tat " + stack_trace[i]);
        }
        if (cause != null) {
            s.println("Caused by: " + this);
            cause.printStackTrace(s);
        }
    }

    public String getSimpleThrowableClassName() {
        String[] parts = throwableClassName.split("\\.");
        return parts[parts.length - 1];
    }
}
