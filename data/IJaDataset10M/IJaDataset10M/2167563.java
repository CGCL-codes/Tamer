package org.colimas.services.doc;

import org.colimas.services.ServiceException;

/**
 * <h3>ExcelParserException.java</h3>
 *
 * <P>
 * Function:<BR />
 * Excel parser exception handler
 * </P>
 * @author zhao lei
 * @version 1.0
 *
 * Modification History:
 * <PRE>
 * SEQ DATE       ORDER DEVELOPER      DESCRIPTION
 * --- ---------- ----- -------------- -----------------------------
 * 001 2005/12/24          zhao lei       INIT
 * </PRE>
 */
public class ExcelParserException extends ServiceException {

    public static final long serialVersionUID = 1;

    /**
	 *<p>constructor</p>
	 */
    public ExcelParserException() {
        super();
    }

    /**
	 *<p>constructor</p>
	 * @param arg0
	 */
    public ExcelParserException(String arg0) {
        super(arg0);
    }

    /**
	 *<p>constructor</p>
	 * @param arg0
	 */
    public ExcelParserException(Throwable arg0) {
        super(arg0);
    }

    /**
	 *<p>constructor</p>
	 * @param arg0
	 * @param arg1
	 */
    public ExcelParserException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
}
