package org.softnetwork.xml;

import org.softnetwork.xml.dom.AttrImpl;
import org.softnetwork.xml.dom.DOMDocument;
import org.w3c.dom.Element;

/**
 * @author $Author: smanciot $
 *
 * @version $Revision: 97 $
 */
public class XMLAttribute extends AttrImpl {

    static final long serialVersionUID = -5669294599208869808L;

    public XMLAttribute(String attributeName) {
        this(null, null, attributeName, null);
    }

    public XMLAttribute(String attributeName, String attributeValue) {
        this(null, null, null, attributeName);
        this.setAttributeValue(attributeValue);
    }

    public XMLAttribute(String namespaceURI, String attributeName, String attributeValue) {
        this(null, null, namespaceURI, attributeName);
        this.setAttributeValue(attributeValue);
    }

    public XMLAttribute(Element ownerElement, String namespaceURI, String attributeName) {
        this(null, ownerElement, namespaceURI, attributeName);
    }

    public XMLAttribute(DOMDocument ownerDocument, Element ownerElement, String namespaceURI, String attributeName) {
        super(ownerDocument, ownerElement, namespaceURI, attributeName);
    }
}
