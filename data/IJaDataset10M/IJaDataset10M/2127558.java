package de.ipkgatersleben.agbi.uploader.database.upload;

/**
 * @author Thomas Rutkowski
 */
public class UplException extends Exception {

    /**
	 *
	 */
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    public UplException() {
        super();
    }

    /**
     * @param arg0
     */
    public UplException(String arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     */
    public UplException(Throwable arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     * @param arg1
     */
    public UplException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
}
