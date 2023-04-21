package org.juddi.base;

import org.w3c.dom.*;
import org.juddi.UDDIException;
import org.juddi.base.*;
import org.juddi.request.*;
import org.juddi.response.*;

/**
 * AuthorizedNameHandler
 *
 * "Knows about the creation and populating of AuthorizedName objects.
 * Returns AuthorizedName."
 *
 * @author  Steve Viens
 * @author  Graeme Riddell
 * @author  Chris Dellario
 * @version 0.1 9/14/2000
 * @since   JDK1.2.2
 */
public class AuthorizedNameHandler implements UDDIXMLHandler {

    private AuthorizedName name = null;

    public AuthorizedNameHandler() {
    }

    public Object create(Node node) throws UDDIException {
        String str = node.getNodeValue();
        if ((str != null) && !(str.equals(""))) {
            name = new AuthorizedName(str);
        }
        return name;
    }
}
