package common.exceptions;

/**
 * La clase extiende de Exeption y la idea es que implemente los tipos de
 * entradas que no son soportados por el sistema el caso de mensajes que no se
 * adecuan a los parametros establecidos por los dise�adores es un ejemplo.
 * 
 * @author javier
 */
public final class UnsopportedMessageException extends Exception {

    /**
	 * Codigo autogenerado por eclipse.
	 */
    private static final long serialVersionUID = 1133088844831513215L;

    /** La leyenda que se se usara para describir la excepcion. */
    private static final String LEYENDA = "Tipo de mensaje no soportado: ";

    /**
	 * Construye la excepcion con la descrippcion por defecto, seguida por la
	 * LEYENDA que se pasa como parametro.
	 * 
	 * @param msgType
	 *            El tipo de mensaje que no esta soportado.
	 * @param LEYENDA
	 *            string estatico con el mensaje que se desea dar.
	 */
    public UnsopportedMessageException(final String msgType) {
        super(LEYENDA + msgType);
    }
}
