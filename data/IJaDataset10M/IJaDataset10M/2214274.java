package ar.com.temporis.framework.domain;

import ar.com.temporis.framework.exception.ApplicationException;

/**
 * @author matias.sulik
 * 
 */
public class DomainException extends ApplicationException {

    public DomainException() {
        super();
    }

    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }

    public DomainException(Throwable cause) {
        super(cause);
    }

    public static void assertNotNull(Object object) {
        assertTrue(object != null, null);
    }

    public static void assertTrue(boolean b, String message) {
        if (!b) {
            throw new DomainException(message);
        }
    }
}
