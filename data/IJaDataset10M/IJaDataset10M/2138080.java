package net.sf.balm.common.beanutils;

/**
 * @author dz
 */
public class DefaultPrimitiveValueNotFoundException extends RuntimeException {

    public DefaultPrimitiveValueNotFoundException() {
        super();
    }

    public DefaultPrimitiveValueNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public DefaultPrimitiveValueNotFoundException(String message) {
        super(message);
    }

    public DefaultPrimitiveValueNotFoundException(Throwable cause) {
        super(cause);
    }
}
