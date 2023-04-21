package net.sf.istcontract.wsimport.tools.wsdl.document.http;

import net.sf.istcontract.wsimport.tools.wsdl.framework.ExtensionImpl;
import org.xml.sax.Locator;
import javax.xml.namespace.QName;

/**
 * A HTTP operation extension.
 *
 * @author WS Development Team
 */
public class HTTPOperation extends ExtensionImpl {

    public HTTPOperation(Locator locator) {
        super(locator);
    }

    public QName getElementName() {
        return HTTPConstants.QNAME_OPERATION;
    }

    public String getLocation() {
        return _location;
    }

    public void setLocation(String s) {
        _location = s;
    }

    public void validateThis() {
        if (_location == null) {
            failValidation("validation.missingRequiredAttribute", "location");
        }
    }

    private String _location;
}
