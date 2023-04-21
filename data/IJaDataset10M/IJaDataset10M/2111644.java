package spc.gaius.actalis.util;

/**
 * @author Administrator
 * 
 */
public class DisabledTargetException extends GaiusException {

    public DisabledTargetException(String message) {
        super(message);
    }

    public DisabledTargetException(Throwable cause) {
        super(cause);
    }

    public DisabledTargetException(String message, Throwable cause) {
        super(message, cause);
    }
}
