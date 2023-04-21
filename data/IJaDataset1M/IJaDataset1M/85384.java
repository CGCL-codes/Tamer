package org.xmldap.ws.soap.headers.addressing.impl;

import nu.xom.Element;
import org.xmldap.exceptions.SerializationException;
import org.xmldap.ws.WSConstants;
import org.xmldap.xml.Serializable;

public class ActionHeader implements Serializable {

    private String action = null;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public ActionHeader(String action) {
        this.action = action;
    }

    public String toXML() throws SerializationException {
        String xml = null;
        Element element = serialize();
        if (element != null) xml = element.toXML();
        return xml;
    }

    public Element serialize() throws SerializationException {
        Element serialized = null;
        if (action != null) {
            serialized = new Element(WSConstants.WSA_PREFIX + ":Action", WSConstants.WSA_NAMESPACE_04_08);
            serialized.appendChild(action);
        }
        return serialized;
    }
}
