package org.xmldap.saml;

import nu.xom.Element;
import org.xmldap.exceptions.SerializationException;
import org.xmldap.ws.WSConstants;
import org.xmldap.xml.Serializable;

public class AudienceRestrictionCondition implements Serializable {

    String restrictedTo = null;

    public AudienceRestrictionCondition(String restrictedTo) {
        this.restrictedTo = restrictedTo;
    }

    public Element serialize() throws SerializationException {
        Element audienceRestrictionCondition = new Element(WSConstants.SAML_PREFIX + ":AudienceRestrictionCondition", WSConstants.SAML11_NAMESPACE);
        Element audience = new Element(WSConstants.SAML_PREFIX + ":Audience", WSConstants.SAML11_NAMESPACE);
        audience.appendChild(restrictedTo);
        audienceRestrictionCondition.appendChild(audience);
        return audienceRestrictionCondition;
    }

    public String toXML() throws SerializationException {
        return serialize().toXML();
    }
}
