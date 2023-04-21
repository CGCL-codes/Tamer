package com.levigo.jbig2.util;

/**
 * Can be used if the maximum value limit of an integer is exceeded.
 * 
 * @author <a href="mailto:m.krzikalla@levigo.de">Matthäus Krzikalla</a>
 * 
 */
public class IntegerMaxValueException extends JBIG2Exception {

    private static final long serialVersionUID = -5534202639860867867L;

    public IntegerMaxValueException() {
    }

    public IntegerMaxValueException(String message) {
        super(message);
    }

    public IntegerMaxValueException(Throwable cause) {
        super(cause);
    }

    public IntegerMaxValueException(String message, Throwable cause) {
        super(message, cause);
    }
}
