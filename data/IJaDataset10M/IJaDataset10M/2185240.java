package org.lightframework.mvc.internal.reflect;

/**
 * <code>{@link ReflectException}</code>
 *
 * @author fenghm (live.fenghm@gmail.com)
 * 
 * @since 1.1.0
 */
public class ReflectException extends RuntimeException {

    private static final long serialVersionUID = 7687935225753613218L;

    public ReflectException() {
        super();
    }

    public ReflectException(String message) {
        super(message);
    }

    public ReflectException(Throwable cause) {
        super(cause);
    }

    public ReflectException(String message, Throwable cause) {
        super(message, cause);
    }
}
