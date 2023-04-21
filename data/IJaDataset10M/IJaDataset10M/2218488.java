package org.apache.batik.dom.svg;

import java.awt.geom.Point2D;
import org.w3c.dom.svg.SVGPoint;
import org.w3c.dom.svg.SVGMatrix;
import org.w3c.dom.DOMException;

/**
 * The clas provides support for the SVGPath interface.
 *
 * @author <a href="mailto:deweese@apache.org">deweese</a>
 * @version $Id: SVGPathSupport.java,v 1.1 2005/11/21 09:51:24 dev Exp $
 */
public class SVGPathSupport {

    /**
     * To implement {@link org.w3c.dom.svg.SVGPathElement#getTotalLength()}.
     */
    public static float getTotalLength(SVGOMPathElement path) {
        SVGPathContext pathCtx = (SVGPathContext) path.getSVGContext();
        return pathCtx.getTotalLength();
    }

    /**
     * To implement {@link org.w3c.dom.svg.SVGPathElement#getPointAtLength(float)}.
     */
    public static SVGPoint getPointAtLength(final SVGOMPathElement path, final float distance) {
        final SVGPathContext pathCtx = (SVGPathContext) path.getSVGContext();
        if (pathCtx == null) return null;
        return new SVGPoint() {

            public float getX() {
                Point2D pt = pathCtx.getPointAtLength(distance);
                return (float) pt.getX();
            }

            public float getY() {
                Point2D pt = pathCtx.getPointAtLength(distance);
                return (float) pt.getY();
            }

            public void setX(float x) throws DOMException {
                throw path.createDOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "readonly.point", null);
            }

            public void setY(float y) throws DOMException {
                throw path.createDOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "readonly.point", null);
            }

            public SVGPoint matrixTransform(SVGMatrix matrix) {
                throw path.createDOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "readonly.point", null);
            }
        };
    }
}

;
