package es.caib.pagos.exceptions;

/**
 *  Clase para las excepciones producidas en el cliente del WS
 * 
 * @author jcsoler
 *
 */
public class ClienteException extends Exception {

    protected int code;

    public ClienteException() {
        super();
    }

    public ClienteException(int code, String message) {
        super(message);
        this.code = code;
    }

    public ClienteException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public ClienteException(Throwable cause) {
        super(cause);
    }

    /**
	 * 
	 * Devuelve el C�digo del Error. Los c�digos est�n definidos en UtilWs
	 * 
	 * @return
	 */
    public int getCode() {
        return code;
    }

    protected void setCode(int code) {
        this.code = code;
    }
}
