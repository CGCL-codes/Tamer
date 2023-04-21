package de.icehorsetools.exeption;

/**
 * @author kruegertom
 * @version $Id: IcehorsetoolsRuntimeException.java 242 2008-03-11 17:53:12Z kruegertom $
 */
public class IcehorsetoolsRuntimeException extends RuntimeException {

    public IcehorsetoolsRuntimeException(String message) {
        super(message);
    }

    public IcehorsetoolsRuntimeException(Throwable cause) {
        super(cause);
    }

    public IcehorsetoolsRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public IcehorsetoolsRuntimeException() {
    }
}
