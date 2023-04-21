package org.openliberty.xmltooling.pp;

import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.AbstractXMLObjectMarshaller;
import org.opensaml.xml.io.MarshallingException;
import org.w3c.dom.Element;

public class AddressMarshaller extends AbstractXMLObjectMarshaller {

    public AddressMarshaller() {
        super();
    }

    @Override
    protected void marshallAttributes(XMLObject xmlObject, Element domElement) throws MarshallingException {
        Address ppObject = (Address) xmlObject;
        ppObject.attributes().marshallAttributes(domElement);
    }

    @Override
    protected void marshallElementContent(XMLObject xmlObject, Element domElement) throws MarshallingException {
    }
}
