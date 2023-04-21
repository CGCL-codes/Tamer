package ar.com.datos.ftrs.exceptions;

public class NoSePudoIndexarPalabraException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
	 * Constructor de Clase
	 * @param mensaje
	 */
    public NoSePudoIndexarPalabraException(String mensaje) {
        super(mensaje);
    }

    /**
	 * Constructor de Clase
	 * @param mensaje
	 * @param t
	 */
    public NoSePudoIndexarPalabraException(String mensaje, Throwable t) {
        super(mensaje, t);
    }
}
