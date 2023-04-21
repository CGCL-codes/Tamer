package org.slasoi.ss.v11.poc.impl.exceptions;

/**
 * The <code>NoVMSpecifiedException</code> exception class represents that there
 * is not VM specified.
 * 
 * @author Kuan Lu
 */
public class NoVMSpecifiedException extends Exception {

    private static final long serialVersionUID = 1L;

    public NoVMSpecifiedException() {
    }

    public NoVMSpecifiedException(String message) {
        super(message);
    }

    public NoVMSpecifiedException(Throwable cause) {
        super(cause);
    }

    public NoVMSpecifiedException(String message, Throwable cause) {
        super(message, cause);
    }
}
