package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;
import org.w3c.dom.svg.SVGAnimatedEnumeration;
import org.w3c.dom.svg.SVGAnimatedString;
import org.w3c.dom.svg.SVGFEBlendElement;

/**
 * This class implements {@link SVGFEBlendElement}.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: SVGOMFEBlendElement.java,v 1.1 2005/11/21 09:51:24 dev Exp $
 */
public class SVGOMFEBlendElement extends SVGOMFilterPrimitiveStandardAttributes implements SVGFEBlendElement {

    /**
     * The 'mode' attribute values.
     */
    protected static final String[] MODE_VALUES = { "", SVG_NORMAL_VALUE, SVG_MULTIPLY_VALUE, SVG_SCREEN_VALUE, SVG_DARKEN_VALUE, SVG_LIGHTEN_VALUE };

    /**
     * Creates a new SVGOMFEBlendElement object.
     */
    protected SVGOMFEBlendElement() {
    }

    /**
     * Creates a new SVGOMFEBlendElement object.
     * @param prefix The namespace prefix.
     * @param owner The owner document.
     */
    public SVGOMFEBlendElement(String prefix, AbstractDocument owner) {
        super(prefix, owner);
    }

    /**
     * <b>DOM</b>: Implements {@link Node#getLocalName()}.
     */
    public String getLocalName() {
        return SVG_FE_BLEND_TAG;
    }

    /**
     * <b>DOM</b>: Implements {@link SVGFEBlendElement#getIn1()}.
     */
    public SVGAnimatedString getIn1() {
        return getAnimatedStringAttribute(null, SVG_IN_ATTRIBUTE);
    }

    /**
     * <b>DOM</b>: Implements {@link SVGFEBlendElement#getIn2()}.
     */
    public SVGAnimatedString getIn2() {
        return getAnimatedStringAttribute(null, SVG_IN2_ATTRIBUTE);
    }

    /**
     * <b>DOM</b>: Implements {@link SVGFEBlendElement#getMode()}.
     */
    public SVGAnimatedEnumeration getMode() {
        return getAnimatedEnumerationAttribute(null, SVG_MODE_ATTRIBUTE, MODE_VALUES, (short) 1);
    }

    /**
     * Returns a new uninitialized instance of this object's class.
     */
    protected Node newNode() {
        return new SVGOMFEBlendElement();
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
