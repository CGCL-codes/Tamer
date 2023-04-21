package net.sf.jasperreports.jsf;

import javax.faces.FacesException;

/**
 * The root exception all thrown during the processing of JasperReports Faces
 * requests.
 *
 * @author A. Alonso Dominguez
 */
public class JRFacesException extends FacesException {

    /** */
    private static final long serialVersionUID = 282841613696573983L;

    /** Instantiates a new JRFacesException. */
    public JRFacesException() {
        super();
    }

    /**
     * Instantiates a new JRFacesException.
     *
     * @param msg the message
     * @param t   the cause
     */
    public JRFacesException(final String msg, final Throwable t) {
        super(msg, t);
    }

    /**
     * Instantiates a new JRFacesException.
     *
     * @param msg the message
     */
    public JRFacesException(final String msg) {
        super(msg);
    }

    /**
     * Instantiates a new JRFacesException.
     *
     * @param t the cause
     */
    public JRFacesException(final Throwable t) {
        super(t);
    }
}
