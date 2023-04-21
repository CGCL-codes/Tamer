package tcl.lang;

/**
 * Signals that a unrecoverable run-time error in the interpreter.
 * Similar to the panic() function in C.
 */
public class TclRuntimeError extends RuntimeException {

    /**
     * Constructs a TclRuntimeError with the specified detail
     * message.
     *
     * @param s the detail message.
     */
    public TclRuntimeError(String s) {
        super(s);
    }
}
