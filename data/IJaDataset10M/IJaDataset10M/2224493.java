package mx.com.nyak.base.security.cipher.exception;

/** 
 * 
 * Derechos Reservados (c)Jose Carlos Perez Cervantes 2009 
 * 
 * 
 * */
@SuppressWarnings("serial")
public class CadenaNoCifradaException extends Exception {

    /**
	 * @param message
	 * @param cause
	 */
    public CadenaNoCifradaException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
	 * @param message
	 */
    public CadenaNoCifradaException(String message) {
        super(message);
    }

    /**
	 * @param cause
	 */
    public CadenaNoCifradaException(Throwable cause) {
        super(cause);
    }
}
