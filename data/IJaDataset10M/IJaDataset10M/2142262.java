package org.xmldap.wsse;

import nu.xom.Element;
import org.xmldap.exceptions.SerializationException;
import org.xmldap.ws.WSConstants;
import org.xmldap.xml.Serializable;

public class SecurityTokenReference implements Serializable {

    Serializable serializable;

    public SecurityTokenReference(KeyIdentifier keyIdentifier) {
        this.serializable = keyIdentifier;
    }

    public SecurityTokenReference(Reference reference) {
        this.serializable = reference;
    }

    public Element serialize() throws SerializationException {
        Element wsse = new Element(WSConstants.WSSE_PREFIX + ":SecurityTokenReference", WSConstants.WSSE_NAMESPACE_OASIS_10);
        wsse.appendChild(serializable.serialize());
        return wsse;
    }

    public String toXML() throws SerializationException {
        return serialize().toXML();
    }
}
