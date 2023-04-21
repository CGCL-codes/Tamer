package es.eucm.eadventure.editor.control.security.jarsigner;

/**
 * Class representing the exceptions specific for jarsigner. 
 */
public class JarSignerException extends Exception {

    /**
     * serial version UID.
     */
    private static final long serialVersionUID = 5012429301200382392L;

    /**
     * Default constructor.
     */
    public JarSignerException() {
        super();
    }

    /**
     * @param msg -
     *            exception message to print
     */
    public JarSignerException(String msg) {
        super(msg);
    }

    /**
     * @param msg -
     *            exception message to print
     * @param cause -
     *            throwable that caused this exception to be thrown
     */
    public JarSignerException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * @param cause -
     *            throwable that caused this exception to be thrown
     */
    public JarSignerException(Throwable cause) {
        super(cause);
    }
}
