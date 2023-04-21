package org.jdom.transform;

import org.jdom.JDOMException;

/**
 * Thrown when an XSL stylesheet fails to compile or an XSL transform fails
 *
 * @version $Revision: 1.4 $, $Date: 2007/11/10 05:29:02 $
 * @author  Jason Hunter
 */
public class XSLTransformException extends JDOMException {

    private static final String CVS_ID = "@(#) $RCSfile: XSLTransformException.java,v $ $Revision: 1.4 $ $Date: 2007/11/10 05:29:02 $ $Name: jdom_1_1 $";

    public XSLTransformException() {
    }

    public XSLTransformException(String message) {
        super(message);
    }

    public XSLTransformException(String message, Exception cause) {
        super(message, cause);
    }
}
