package org.colimas.services.doc;

import org.colimas.services.ServiceException;

/**
 * <h3>DocException.java</h3>
 *
 * <P>
 * Function:<BR />
 * Doc process exception handler
 * </P>
 * @author zhao lei
 * @version 1.0
 *
 * Modification History:
 * <PRE>
 * SEQ DATE       ORDER DEVELOPER      DESCRIPTION
 * --- ---------- ----- -------------- -----------------------------
 * 001 2005/12/03          zhao lei       INIT
 * </PRE>
 */
public class DocException extends ServiceException {

    public static final long serialVersionUID = 1;

    /**
	 * 
	 */
    public DocException() {
        super();
    }

    /**
	 * @param arg0
	 */
    public DocException(String arg0) {
        super(arg0);
    }

    /**
	 * @param arg0
	 */
    public DocException(Throwable arg0) {
        super(arg0);
    }

    /**
	 * @param arg0
	 * @param arg1
	 */
    public DocException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
}
