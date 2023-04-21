package org.apache.ws.jaxme.xs.types;

import org.apache.ws.jaxme.xs.XSParser;
import org.apache.ws.jaxme.xs.XSType;
import org.apache.ws.jaxme.xs.xml.XsQName;

/** <p>The type xs:dateTime.</p>
 *
 * @author <a href="mailto:joe@ispsoft.de">Jochen Wiedmann</a>
 */
public class XSDuration extends AbstractAtomicType {

    private static final XSDuration theInstance = new XSDuration();

    private static final XsQName name = new XsQName(XSParser.XML_SCHEMA_URI, "duration", null);

    protected XSDuration() {
    }

    public XsQName getName() {
        return name;
    }

    public static final XSType getInstance() {
        return theInstance;
    }

    public boolean isRestriction() {
        return true;
    }

    public XSType getRestrictedType() {
        return XSAnySimpleType.getInstance();
    }
}
