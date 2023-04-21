package net.sf.jasperreports.jsf.fill;

import net.sf.jasperreports.jsf.JRFacesException;

/**
 * The Class FillerException.
 */
public class FillerException extends JRFacesException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -3240029115273719789L;

    /**
     * Instantiates a new filler exception.
     * 
     * @param msg the msg
     * @param t the t
     */
    public FillerException(final String msg, final Throwable t) {
        super(msg, t);
    }

    /**
     * Instantiates a new filler exception.
     * 
     * @param msg the msg
     */
    public FillerException(final String msg) {
        super(msg);
    }

    /**
     * Instantiates a new filler exception.
     * 
     * @param t the t
     */
    public FillerException(final Throwable t) {
        super(t);
    }
}
