package gnu.javax.crypto.sasl;

import javax.security.sasl.AuthenticationException;

/**
 * A checked exception thrown to indicate that an operation that should be
 * invoked on a completed mechanism was invoked but the authentication phase of
 * that mechanism was not completed yet, or that an operation that should be
 * invoked on incomplete mechanisms was invoked but the authentication phase of
 * that mechanism was already completed.
 */
public class IllegalMechanismStateException extends AuthenticationException {

    /**
   * Constructs a new instance of <code>IllegalMechanismStateException</code>
   * with no detail message.
   */
    public IllegalMechanismStateException() {
        super();
    }

    /**
   * Constructs a new instance of <code>IllegalMechanismStateException</code>
   * with the specified detail message.
   * 
   * @param detail the detail message.
   */
    public IllegalMechanismStateException(String detail) {
        super(detail);
    }

    /**
   * Constructs a new instance of <code>IllegalMechanismStateException</code>
   * with the specified detail message, and cause.
   * 
   * @param detail the detail message.
   * @param ex the original cause.
   */
    public IllegalMechanismStateException(String detail, Throwable ex) {
        super(detail, ex);
    }
}
