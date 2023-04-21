package wsmg;

import java.io.*;

public class WsmgAdapterException extends Exception {

    Throwable base;

    public WsmgAdapterException(String message, Throwable _base) {
        super(message + " Caused by: " + _base.getMessage());
        base = _base;
    }

    public WsmgAdapterException(String message) {
        super(message);
    }

    public void printStackTrace(PrintStream s) {
        super.printStackTrace(s);
        if (base != null) {
            s.println("Caused by:");
            base.printStackTrace(s);
        }
    }

    public void printStackTrace(PrintWriter s) {
        super.printStackTrace(s);
        if (base != null) {
            s.println("Caused by:");
            base.printStackTrace(s);
        }
    }
}
