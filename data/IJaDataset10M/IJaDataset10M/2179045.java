package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.util.XLinkSupport;
import org.apache.batik.dom.util.XMLSupport;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;
import org.w3c.dom.svg.SVGAnimatedBoolean;
import org.w3c.dom.svg.SVGAnimatedLength;
import org.w3c.dom.svg.SVGCursorElement;
import org.w3c.dom.svg.SVGStringList;

/**
 * This class implements {@link org.w3c.dom.svg.SVGCursorElement}.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: SVGOMCursorElement.java,v 1.1 2005/11/21 09:51:29 dev Exp $
 */
public class SVGOMCursorElement extends SVGOMURIReferenceElement implements SVGCursorElement {

    /**
     * The attribute initializer.
     */
    protected static final AttributeInitializer attributeInitializer;

    static {
        attributeInitializer = new AttributeInitializer(4);
        attributeInitializer.addAttribute(XMLSupport.XMLNS_NAMESPACE_URI, null, "xmlns:xlink", XLinkSupport.XLINK_NAMESPACE_URI);
        attributeInitializer.addAttribute(XLinkSupport.XLINK_NAMESPACE_URI, "xlink", "type", "simple");
        attributeInitializer.addAttribute(XLinkSupport.XLINK_NAMESPACE_URI, "xlink", "show", "other");
        attributeInitializer.addAttribute(XLinkSupport.XLINK_NAMESPACE_URI, "xlink", "actuate", "onLoad");
    }

    /**
     * Creates a new SVGOMCursorElement object.
     */
    protected SVGOMCursorElement() {
    }

    /**
     * Creates a new SVGOMCursorElement object.
     * @param prefix The namespace prefix.
     * @param owner The owner document.
     */
    public SVGOMCursorElement(String prefix, AbstractDocument owner) {
        super(prefix, owner);
    }

    /**
     * <b>DOM</b>: Implements {@link Node#getLocalName()}.
     */
    public String getLocalName() {
        return SVG_CURSOR_TAG;
    }

    /**
     * <b>DOM</b>: Implements {@link SVGCursorElement#getX()}.
     */
    public SVGAnimatedLength getX() {
        return getAnimatedLengthAttribute(null, SVG_X_ATTRIBUTE, SVG_CURSOR_X_DEFAULT_VALUE, SVGOMAnimatedLength.HORIZONTAL_LENGTH);
    }

    /**
     * <b>DOM</b>: Implements {@link SVGCursorElement#getY()}.
     */
    public SVGAnimatedLength getY() {
        return getAnimatedLengthAttribute(null, SVG_Y_ATTRIBUTE, SVG_CURSOR_Y_DEFAULT_VALUE, SVGOMAnimatedLength.VERTICAL_LENGTH);
    }

    /**
     * <b>DOM</b>: Implements {@link
     * org.w3c.dom.svg.SVGExternalResourcesRequired#getExternalResourcesRequired()}.
     */
    public SVGAnimatedBoolean getExternalResourcesRequired() {
        return SVGExternalResourcesRequiredSupport.getExternalResourcesRequired(this);
    }

    /**
     * <b>DOM</b>: Implements {@link
     * org.w3c.dom.svg.SVGTests#getRequiredFeatures()}.
     */
    public SVGStringList getRequiredFeatures() {
        return SVGTestsSupport.getRequiredFeatures(this);
    }

    /**
     * <b>DOM</b>: Implements {@link
     * org.w3c.dom.svg.SVGTests#getRequiredExtensions()}.
     */
    public SVGStringList getRequiredExtensions() {
        return SVGTestsSupport.getRequiredExtensions(this);
    }

    /**
     * <b>DOM</b>: Implements {@link
     * org.w3c.dom.svg.SVGTests#getSystemLanguage()}.
     */
    public SVGStringList getSystemLanguage() {
        return SVGTestsSupport.getSystemLanguage(this);
    }

    /**
     * <b>DOM</b>: Implements {@link
     * org.w3c.dom.svg.SVGTests#hasExtension(String)}.
     */
    public boolean hasExtension(String extension) {
        return SVGTestsSupport.hasExtension(this, extension);
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
        return new SVGOMCursorElement();
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
