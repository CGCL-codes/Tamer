package uchicago.src.sim.util;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * A RePast specific extension intended to wrap other extensions. Simplifies
 * exception handling.
 */
public class RepastException extends Exception {

    private Exception ex;

    private String message;

    public RepastException(String message, Exception ex) {
        super(message, ex);
        this.ex = ex;
        this.message = message;
    }

    public RepastException(Exception ex, String message) {
        super(message, ex);
        this.ex = ex;
        this.message = message;
    }

    public RepastException(String message) {
        this.message = message;
    }

    public void printStackTrace() {
        System.out.println(message);
        if (ex != null) ex.printStackTrace();
    }

    public void printStackTrace(PrintStream s) {
        s.println(message);
        if (ex != null) ex.printStackTrace(s);
    }

    public void printStackTrace(PrintWriter w) {
        w.println(message);
        if (ex != null) ex.printStackTrace(w);
    }

    public String getMessage() {
        if (ex != null) return message + "\n" + ex.getMessage();
        return message;
    }
}
