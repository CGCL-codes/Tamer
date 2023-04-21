package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;
import org.w3c.dom.svg.SVGAnimateMotionElement;

/**
 * This class implements {@link SVGAnimateMotionElement}.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: SVGOMAnimateMotionElement.java,v 1.1 2005/11/21 09:51:29 dev Exp $
 */
public class SVGOMAnimateMotionElement extends SVGOMAnimationElement implements SVGAnimateMotionElement {

    /**
     * The attribute initializer.
     */
    protected static final AttributeInitializer attributeInitializer;

    static {
        attributeInitializer = new AttributeInitializer(1);
        attributeInitializer.addAttribute(null, null, SVG_CALC_MODE_ATTRIBUTE, SVG_PACED_VALUE);
    }

    /**
     * Creates a new SVGOMAnimateMotionElement object.
     */
    protected SVGOMAnimateMotionElement() {
    }

    /**
     * Creates a new SVGOMAnimateMotionElement object.
     * @param prefix The namespace prefix.
     * @param owner The owner document.
     */
    public SVGOMAnimateMotionElement(String prefix, AbstractDocument owner) {
        super(prefix, owner);
    }

    /**
     * <b>DOM</b>: Implements {@link Node#getLocalName()}.
     */
    public String getLocalName() {
        return SVG_ANIMATE_MOTION_TAG;
    }

    /**
     * Returns the AttributeInitializer for this element type.
     * @return null if this element has no attribute with a default value.
     */
    protected AttributeInitializer getAttributeInitializer() {
        return attributeInitializer;
    }

    /**
     * Returns a new uninitialized instance of this object's class.
     */
    protected Node newNode() {
        return new SVGOMAnimateMotionElement();
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
