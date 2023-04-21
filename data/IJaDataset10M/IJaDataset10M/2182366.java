package jfun.yan;

/**
 * Thrown when a java bean object is null.
 * <p>
 * Zephyr Business Solutions Corp.
 *
 * @author Ben Yu
 *
 */
public class NullBeanObjectException extends YanException {

    public NullBeanObjectException() {
        super();
    }

    public NullBeanObjectException(String arg0) {
        super(arg0);
    }

    public NullBeanObjectException(String message, Throwable cause) {
        super(message, cause);
    }

    public NullBeanObjectException(Throwable cause) {
        super(cause);
    }
}
