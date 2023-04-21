package org.openliberty.xmltooling.soapbinding;

import java.util.Map.Entry;
import javax.xml.namespace.QName;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.AbstractXMLObjectMarshaller;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

public class ActionMarshaller extends AbstractXMLObjectMarshaller {

    @Override
    protected void marshallAttributes(XMLObject xmlObject, Element domElement) throws MarshallingException {
        Action action = (Action) xmlObject;
        Attr attr;
        for (Entry<QName, String> entry : action.getUnknownAttributes().entrySet()) {
            attr = XMLHelper.constructAttribute(domElement.getOwnerDocument(), entry.getKey());
            attr.setValue(entry.getValue());
            domElement.setAttributeNodeNS(attr);
            if (Configuration.isIDAttribute(entry.getKey()) || action.getUnknownAttributes().isIDAttribute(entry.getKey())) {
                attr.getOwnerElement().setIdAttributeNode(attr, true);
            }
        }
    }

    @Override
    protected void marshallElementContent(XMLObject xmlObject, Element domElement) throws MarshallingException {
        Action action = (Action) xmlObject;
        XMLHelper.appendTextContent(domElement, action.getValue());
    }
}
