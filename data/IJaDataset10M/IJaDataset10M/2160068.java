package org.opendte.common;

import java.io.PrintStream;
import java.io.PrintWriter;

public class DteException extends Exception {

    Throwable mRootCause;

    public DteException(String message) {
        super(message);
    }

    public DteException(String message, Throwable rootCause) {
        super(message);
        mRootCause = rootCause;
    }

    public void printStackTrace() {
        printStackTrace(System.err);
    }

    public void printStackTrace(PrintStream ps) {
        printStackTrace(new PrintWriter(ps));
    }

    public void printStackTrace(PrintWriter pw) {
        if (mRootCause == null) {
            super.printStackTrace(pw);
        } else {
            super.printStackTrace(pw);
            pw.println("Root Cause:");
            mRootCause.printStackTrace(pw);
        }
        pw.flush();
    }
}
