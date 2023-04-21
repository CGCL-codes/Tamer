package org.openliberty.xmltooling.epr;

import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.AbstractXMLObjectMarshaller;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Element;

public class TokenMarshaller extends AbstractXMLObjectMarshaller {

    @Override
    protected void marshallAttributes(XMLObject xmlObject, Element domElement) throws MarshallingException {
        Token token = (Token) xmlObject;
        if (token.getId() != null) {
            domElement.setAttributeNS(null, Token.ATT_ID, token.getId());
            domElement.setIdAttributeNS(null, Token.ATT_ID, true);
        }
        if (token.getRef() != null) {
            domElement.setAttributeNS(null, Token.ATT_REF, token.getRef());
        }
        if (token.getUsage() != null) {
            domElement.setAttributeNS(null, Token.ATT_USAGE, token.getUsage());
        }
    }

    @Override
    protected void marshallElementContent(XMLObject xmlObject, Element domElement) throws MarshallingException {
        Token token = (Token) xmlObject;
        XMLHelper.appendTextContent(domElement, token.getValue());
    }
}
