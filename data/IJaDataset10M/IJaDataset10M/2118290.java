package org.notify4b;

import java.io.PrintStream;
import java.io.PrintWriter;

public class NotifyException extends Exception {

    private static final long serialVersionUID = -5930411080686519130L;

    private Throwable nestedThrowable = null;

    public NotifyException() {
        super();
    }

    public NotifyException(String msg) {
        super(msg);
    }

    public NotifyException(Throwable nestedThrowable) {
        this.nestedThrowable = nestedThrowable;
    }

    public NotifyException(String msg, Throwable nestedThrowable) {
        super(msg);
        this.nestedThrowable = nestedThrowable;
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
        if (nestedThrowable != null) nestedThrowable.printStackTrace();
    }

    @Override
    public void printStackTrace(PrintStream ps) {
        super.printStackTrace(ps);
        if (nestedThrowable != null) nestedThrowable.printStackTrace();
    }

    @Override
    public void printStackTrace(PrintWriter ps) {
        super.printStackTrace(ps);
        if (nestedThrowable != null) nestedThrowable.printStackTrace();
    }
}
