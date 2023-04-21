package org.openliberty.xmltooling.epr;

import javax.xml.namespace.QName;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.AbstractXMLObjectUnmarshaller;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Attr;

public class FrameworkUnmarshaller extends AbstractXMLObjectUnmarshaller {

    @Override
    protected void processAttribute(XMLObject xmlObject, Attr attribute) throws UnmarshallingException {
        Framework framework = (Framework) xmlObject;
        if (attribute.getLocalName().equals(Framework.ATT_VERSION)) {
            framework.setVersion(attribute.getValue());
        } else {
            QName attribQName = XMLHelper.getNodeQName(attribute);
            if (attribute.isId()) {
                framework.getUnknownAttributes().registerID(attribQName);
            }
            framework.getUnknownAttributes().put(attribQName, attribute.getValue());
        }
    }

    @Override
    protected void processChildElement(XMLObject parentXMLObject, XMLObject childXMLObject) throws UnmarshallingException {
    }

    @Override
    protected void processElementContent(XMLObject xmlObject, String elementContent) {
    }
}
