package org.apache.wsdl4reg;

/**
 * @author BrandO
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class WSDL4RegWSDLException extends WSDL4RegException {

    /**
     * Constructor for WSDL4RegWSDLException.
     */
    public WSDL4RegWSDLException() {
        super();
    }

    /**
     * Constructor for WSDL4RegWSDLException.
     * @param nestedException
     */
    public WSDL4RegWSDLException(Exception nestedException) {
        super(nestedException);
    }

    /**
     * Constructor for WSDL4RegWSDLException.
     * @param s
     */
    public WSDL4RegWSDLException(String s) {
        super(s);
    }

    /**
     * Constructor for WSDL4RegWSDLException.
     * @param s
     * @param nestedException
     */
    public WSDL4RegWSDLException(String s, Exception nestedException) {
        super(s, nestedException);
    }
}
