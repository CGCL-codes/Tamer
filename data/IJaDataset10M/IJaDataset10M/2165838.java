package org.openliberty.xmltooling.soapbinding;

import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.AbstractXMLObjectMarshaller;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Element;

/**
 * @author tguion
 *
 */
public class RedirectRequestMarshaller extends AbstractXMLObjectMarshaller {

    @Override
    protected void marshallAttributes(XMLObject xmlObject, Element domElement) throws MarshallingException {
        RedirectRequest redirectRequest = (RedirectRequest) xmlObject;
        if (redirectRequest.getRedirectURL() != null) {
            domElement.setAttributeNS(null, RedirectRequest.REDIRECT_URL_ATTR_NAME, redirectRequest.getRedirectURL());
        }
    }

    @Override
    protected void marshallElementContent(XMLObject xmlObject, Element domElement) throws MarshallingException {
        RedirectRequest redirectRequest = (RedirectRequest) xmlObject;
        XMLHelper.appendTextContent(domElement, redirectRequest.getReason());
    }
}
