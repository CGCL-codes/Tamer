package org.openliberty.xmltooling.disco;

import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.AbstractXMLObjectMarshaller;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Element;

public class SecurityMechIDMarshaller extends AbstractXMLObjectMarshaller {

    @Override
    protected void marshallAttributes(XMLObject xmlObject, Element domElement) throws MarshallingException {
    }

    @Override
    protected void marshallElementContent(XMLObject xmlObject, Element domElement) throws MarshallingException {
        SecurityMechID securityMechID = (SecurityMechID) xmlObject;
        XMLHelper.appendTextContent(domElement, securityMechID.getValue());
    }
}
