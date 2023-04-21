package org.openliberty.xmltooling.ps;

import org.openliberty.xmltooling.Konstantz;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.AbstractXMLObjectUnmarshaller;
import org.opensaml.xml.io.UnmarshallingException;
import org.w3c.dom.Attr;

public class PSObjectRefUnmarshaller extends AbstractXMLObjectUnmarshaller {

    public PSObjectRefUnmarshaller() {
        super(Konstantz.PS_NS, PSObjectRef.LOCAL_NAME);
    }

    @Override
    protected void processAttribute(XMLObject xmlObject, Attr attribute) throws UnmarshallingException {
    }

    @Override
    protected void processChildElement(XMLObject parentXMLObject, XMLObject childXMLObject) throws UnmarshallingException {
    }

    @Override
    protected void processElementContent(XMLObject xmlObject, String elementContent) {
        ((PSObjectRef) xmlObject).setValue(elementContent);
    }
}
