package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;
import org.w3c.dom.svg.SVGSwitchElement;

/**
 * This class implements {@link SVGSwitchElement}.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: SVGOMSwitchElement.java,v 1.1 2005/11/21 09:51:24 dev Exp $
 */
public class SVGOMSwitchElement extends SVGGraphicsElement implements SVGSwitchElement {

    /**
     * Creates a new SVGOMSwitchElement object.
     */
    protected SVGOMSwitchElement() {
    }

    /**
     * Creates a new SVGOMSwitchElement object.
     * @param prefix The namespace prefix.
     * @param owner The owner document.
     */
    public SVGOMSwitchElement(String prefix, AbstractDocument owner) {
        super(prefix, owner);
    }

    /**
     * <b>DOM</b>: Implements {@link Node#getLocalName()}.
     */
    public String getLocalName() {
        return SVG_SWITCH_TAG;
    }

    /**
     * Returns a new uninitialized instance of this object's class.
     */
    protected Node newNode() {
        return new SVGOMSwitchElement();
    }

    public TypeInfo getSchemaTypeInfo() {
        return null;
    }

    public void setIdAttribute(String arg0, boolean arg1) throws DOMException {
    }

    public void setIdAttributeNS(String arg0, String arg1, boolean arg2) throws DOMException {
    }

    public void setIdAttributeNode(Attr arg0, boolean arg1) throws DOMException {
    }

    public String getBaseURI() {
        return null;
    }

    public short compareDocumentPosition(Node arg0) throws DOMException {
        return 0;
    }

    public String getTextContent() throws DOMException {
        return null;
    }

    public void setTextContent(String arg0) throws DOMException {
    }

    public boolean isSameNode(Node arg0) {
        return false;
    }

    public String lookupPrefix(String arg0) {
        return null;
    }

    public boolean isDefaultNamespace(String arg0) {
        return false;
    }

    public String lookupNamespaceURI(String arg0) {
        return null;
    }

    public boolean isEqualNode(Node arg0) {
        return false;
    }

    public Object getFeature(String arg0, String arg1) {
        return null;
    }

    public Object setUserData(String arg0, Object arg1, UserDataHandler arg2) {
        return null;
    }

    public Object getUserData(String arg0) {
        return null;
    }
}
