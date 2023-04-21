package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.svg.SVGAnimatedLength;
import org.w3c.dom.svg.SVGAnimatedString;
import org.w3c.dom.svg.SVGFilterPrimitiveStandardAttributes;

/**
 * This class represents a SVGElement with support for standard filter
 * attributes.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: SVGOMFilterPrimitiveStandardAttributes.java,v 1.1 2005/11/21 09:51:24 dev Exp $
 */
public abstract class SVGOMFilterPrimitiveStandardAttributes extends SVGStylableElement implements SVGFilterPrimitiveStandardAttributes {

    /**
     * Creates a new SVGOMFilterPrimitiveStandardAttributes object.
     */
    protected SVGOMFilterPrimitiveStandardAttributes() {
    }

    /**
     * Creates a new SVGOMFilterPrimitiveStandardAttributes object.
     * @param prefix The namespace prefix.
     * @param owner The owner document.
     */
    protected SVGOMFilterPrimitiveStandardAttributes(String prefix, AbstractDocument owner) {
        super(prefix, owner);
    }

    /**
     * <b>DOM</b>: Implements {@link
     * org.w3c.dom.svg.SVGFilterPrimitiveStandardAttributes#getX()}.
     */
    public SVGAnimatedLength getX() {
        return getAnimatedLengthAttribute(null, SVG_X_ATTRIBUTE, SVG_FILTER_PRIMITIVE_X_DEFAULT_VALUE, SVGOMAnimatedLength.HORIZONTAL_LENGTH);
    }

    /**
     * <b>DOM</b>: Implements {@link
     * org.w3c.dom.svg.SVGFilterPrimitiveStandardAttributes#getY()}.
     */
    public SVGAnimatedLength getY() {
        return getAnimatedLengthAttribute(null, SVG_Y_ATTRIBUTE, SVG_FILTER_PRIMITIVE_Y_DEFAULT_VALUE, SVGOMAnimatedLength.VERTICAL_LENGTH);
    }

    /**
     * <b>DOM</b>: Implements {@link
     * org.w3c.dom.svg.SVGFilterPrimitiveStandardAttributes#getWidth()}.
     */
    public SVGAnimatedLength getWidth() {
        return getAnimatedLengthAttribute(null, SVG_WIDTH_ATTRIBUTE, SVG_FILTER_PRIMITIVE_WIDTH_DEFAULT_VALUE, SVGOMAnimatedLength.HORIZONTAL_LENGTH);
    }

    /**
     * <b>DOM</b>: Implements {@link
     * org.w3c.dom.svg.SVGFilterPrimitiveStandardAttributes#getHeight()}.
     */
    public SVGAnimatedLength getHeight() {
        return getAnimatedLengthAttribute(null, SVG_HEIGHT_ATTRIBUTE, SVG_FILTER_PRIMITIVE_HEIGHT_DEFAULT_VALUE, SVGOMAnimatedLength.VERTICAL_LENGTH);
    }

    /**
     * <b>DOM</b>: Implements {@link
     * org.w3c.dom.svg.SVGFilterPrimitiveStandardAttributes#getResult()}.
     */
    public SVGAnimatedString getResult() {
        return getAnimatedStringAttribute(null, SVG_RESULT_ATTRIBUTE);
    }
}
