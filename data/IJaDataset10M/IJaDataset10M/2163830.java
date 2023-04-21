package org.apache.ws.jaxme.xs.types;

import org.apache.ws.jaxme.xs.XSParser;
import org.apache.ws.jaxme.xs.XSType;
import org.apache.ws.jaxme.xs.xml.XsQName;

/** <p>The xs:normalizedString type.</p>
 *
 * @author <a href="mailto:joe@ispsoft.de">Jochen Wiedmann</a>
 */
public class XSNormalizedString extends XSString {

    private static final XSNormalizedString theInstance = new XSNormalizedString();

    private static final XsQName name = new XsQName(XSParser.XML_SCHEMA_URI, "normalizedString", null);

    protected XSNormalizedString() {
    }

    public XsQName getName() {
        return name;
    }

    public boolean isReplacing() {
        return true;
    }

    public static XSType getInstance() {
        return theInstance;
    }

    public XSType getRestrictedType() {
        return XSString.getInstance();
    }
}
