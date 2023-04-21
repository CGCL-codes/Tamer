package org.openliberty.xmltooling.wsu;

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
public class AttributedDateTimeUnmarshaller extends AbstractXMLObjectUnmarshaller {

    @Override
    protected void processAttribute(XMLObject xmlObject, Attr attribute) throws UnmarshallingException {
        AttributedDateTime obj = (AttributedDateTime) xmlObject;
        if (attribute.getLocalName().equals(AttributedDateTime.ATT_ID)) {
            obj.setId(attribute.getValue());
        } else {
            AttributedDateTime adt = (AttributedDateTime) xmlObject;
            QName attribQName = XMLHelper.getNodeQName(attribute);
            if (attribute.isId()) {
                adt.getUnknownAttributes().registerID(attribQName);
            }
            adt.getUnknownAttributes().put(attribQName, attribute.getValue());
        }
    }

    @Override
    protected void processChildElement(XMLObject parentXMLObject, XMLObject childXMLObject) throws UnmarshallingException {
    }

    @Override
    protected void processElementContent(XMLObject xmlObject, String elementContent) {
        AttributedDateTime adt = (AttributedDateTime) xmlObject;
        adt.setValue(elementContent);
    }
}
