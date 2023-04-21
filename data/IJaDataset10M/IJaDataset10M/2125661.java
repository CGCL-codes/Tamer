package org.jcvi.common.core.symbol;

/**
 * An <code>IllegalEncodedValueException</code> is thrown when an encoded
 * positions or quality string contains a value or structure which is
 * illegal.  This includes situations like included non-printable characters
 * or attempting to decode an empty {@link String}.
 * 
 * @author jsitz
 * @author dkatzel
 */
public class IllegalEncodedValueException extends RuntimeException {

    /**
     * The Serial Version UID
     */
    private static final long serialVersionUID = -6796187135101242713L;

    /**
     * Creates a new <code>IllegalEncodedValueException</code>.
     * 
     * @param message A message describing the Exception.
     * @param cause The {@link Throwable} declared as the cause of this Exception.
     */
    public IllegalEncodedValueException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new <code>IllegalEncodedValueException</code>.
     * 
     * @param message A message describing the cause of this Exception.
     */
    public IllegalEncodedValueException(String message) {
        super(message);
    }
}
