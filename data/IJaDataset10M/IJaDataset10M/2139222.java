package net.sf.opendf.cal.interpreter;

/**
 *
 * @author Jorn W. Janneck <janneck@eecs.berkeley.edu>
 */
public class InterpreterException extends RuntimeException {

    public InterpreterException() {
    }

    public InterpreterException(String msg) {
        super(msg);
    }

    public InterpreterException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public InterpreterException(Throwable cause) {
        super(cause);
    }
}
