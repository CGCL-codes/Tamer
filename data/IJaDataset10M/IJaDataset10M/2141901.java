package net.sf.sail.webapp.dao.sds;

import net.sf.sail.webapp.domain.sds.SdsOffering;

/**
 * This exception is thrown when a curnitmap is not retrieved for whatever
 * reason. When the exception is thrown you can preserve the sdsoffering by
 * saving it within this exception, and then retreiving it when the exception is
 * caught.
 * 
 * @author Laurel Williams
 * 
 * @version $Id: CurnitMapNotFoundException.java 1338 2007-10-11 12:37:19Z laurel $
 */
public class CurnitMapNotFoundException extends HttpStatusCodeException {

    private SdsOffering sdsOffering;

    /**
	 * @return the sdsOffering
	 */
    public SdsOffering getSdsOffering() {
        return sdsOffering;
    }

    /**
	 * @param sdsOffering
	 *            the sdsOffering to set
	 */
    public void setSdsOffering(SdsOffering sdsOffering) {
        this.sdsOffering = sdsOffering;
    }

    public CurnitMapNotFoundException(String message) {
        super(message);
    }

    private static final long serialVersionUID = 1L;
}
