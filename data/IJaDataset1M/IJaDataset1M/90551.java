package jopt.mp.spi;

/**
 * Thrown when problem is determined to be unbounded
 */
public class UnboundedException extends Exception {

    public UnboundedException(String msg) {
        super(msg);
    }
}
