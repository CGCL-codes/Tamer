package es.eside.deusto.pfc.kernel.impl.dao.exception;

public class NotPersistableException extends Exception {

    /** Default serialisacion id */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor con mensaje explicativo, que debe aprovechar de los
     * mechanismos de internacionalisacion de {@link IMessageLocator}.
     *
     */
    public NotPersistableException() {
        super();
    }

    /**
     * Constructor con encadenamiento de la causa del error.
     *
     * @param description la causa del error.
     */
    public NotPersistableException(String description) {
        super(description);
    }

    /**
     * Constructor con encadenamiento de la causa del error.
     *
     * @param cause la causa del error.
     */
    public NotPersistableException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor con encadenamiento de la causa del error y una descripcion.
     *
     * @param description la descripcion del error.
     * @param cause la cuasa del error.
     */
    public NotPersistableException(String description, Throwable cause) {
        super(description, cause);
    }
}
