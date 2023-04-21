package org.openliberty.xmltooling.soapbinding;

import javax.xml.namespace.QName;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.AbstractXMLObjectUnmarshaller;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Attr;

/**
 * @author tguion
 *
 */
public class ToUnmarshaller extends AbstractXMLObjectUnmarshaller {

    @Override
    protected void processAttribute(XMLObject xmlObject, Attr attribute) throws UnmarshallingException {
        To to = (To) xmlObject;
        QName attribQName = XMLHelper.getNodeQName(attribute);
        if (attribute.isId()) {
            to.getUnknownAttributes().registerID(attribQName);
        }
        to.getUnknownAttributes().put(attribQName, attribute.getValue());
    }

    @Override
    protected void processChildElement(XMLObject parentXMLObject, XMLObject childXMLObject) throws UnmarshallingException {
    }

    @Override
    protected void processElementContent(XMLObject xmlObject, String elementContent) {
        To to = (To) xmlObject;
        if (elementContent != null) {
            to.setValue(elementContent.trim());
        }
    }
}
