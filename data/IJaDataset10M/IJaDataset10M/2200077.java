package net.greencoding.thysdrus.circuitbreaker.exception;

/**
 * 
 * @author Nabil Ben Said (nabil.ben.said@net-m.de)
 *
 */
public class CircuitBreakerMethodExecutionException extends Exception {

    private static final long serialVersionUID = 469923151407502871L;

    public CircuitBreakerMethodExecutionException() {
        super();
    }

    public CircuitBreakerMethodExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public CircuitBreakerMethodExecutionException(String message) {
        super(message);
    }

    public CircuitBreakerMethodExecutionException(Throwable cause) {
        super(cause);
    }
}
