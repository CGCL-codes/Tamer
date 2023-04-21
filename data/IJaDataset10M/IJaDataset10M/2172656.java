package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.svg.SVGAnimatedLengthList;
import org.w3c.dom.svg.SVGAnimatedNumberList;
import org.w3c.dom.svg.SVGTextPositioningElement;

/**
 * This class implements {@link org.w3c.dom.svg.SVGTextPositioningElement}.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: SVGOMTextPositioningElement.java,v 1.1 2005/11/21 09:51:24 dev Exp $
 */
public abstract class SVGOMTextPositioningElement extends SVGOMTextContentElement implements SVGTextPositioningElement {

    /**
     * Creates a new SVGOMTextPositioningElement object.
     */
    protected SVGOMTextPositioningElement() {
    }

    /**
     * Creates a new SVGOMTextPositioningElement object.
     * @param prefix The namespace prefix.
     * @param owner The owner document.
     */
    protected SVGOMTextPositioningElement(String prefix, AbstractDocument owner) {
        super(prefix, owner);
    }

    /**
     * <b>DOM</b>: Implements {@link SVGTextPositioningElement#getX()}.
     */
    public SVGAnimatedLengthList getX() {
        return SVGTextPositioningElementSupport.getX(this);
    }

    /**
     * <b>DOM</b>: Implements {@link SVGTextPositioningElement#getY()}.
     */
    public SVGAnimatedLengthList getY() {
        return SVGTextPositioningElementSupport.getY(this);
    }

    /**
     * <b>DOM</b>: Implements {@link SVGTextPositioningElement#getDx()}.
     */
    public SVGAnimatedLengthList getDx() {
        return SVGTextPositioningElementSupport.getDx(this);
    }

    /**
     * <b>DOM</b>: Implements {@link SVGTextPositioningElement#getDy()}.
     */
    public SVGAnimatedLengthList getDy() {
        return SVGTextPositioningElementSupport.getDy(this);
    }

    /**
     * <b>DOM</b>: Implements {@link SVGTextPositioningElement#getRotate()}.
     */
    public SVGAnimatedNumberList getRotate() {
        throw new RuntimeException(" !!! SVGOMTextPositioningElement.getRotate()");
    }
}
