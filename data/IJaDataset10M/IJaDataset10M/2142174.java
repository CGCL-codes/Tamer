package org.jtestcase.core.digester;

/**
 * Exception class for all Exceptions thrown in parsing of the JTestCaseWizard XML
 * 
 * @author <a href="mailto:faustothegrey@users.sourceforge.net">Fausto Lelli</a>
 * 
 * $Id: DigesterException.java,v 1.2 2005/11/05 11:20:35 faustothegrey Exp $
 */
public class DigesterException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
     * @see java.lang.Exception#Exception(java.lang.String, java.lang.Throwable) 
     */
    public DigesterException(String message, Throwable cause) {
        super(message, cause);
    }

    /** 
     * @see java.lang.Exception#Exception(java.lang.Throwable) 
     */
    public DigesterException(Throwable cause) {
        super(cause);
    }
}
