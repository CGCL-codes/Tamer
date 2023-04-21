package org.apache.batik.css.engine.value;

import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;

/**
 * This class represents CSS rect values.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: RectValue.java,v 1.1 2005/11/21 09:51:39 dev Exp $
 */
public class RectValue extends AbstractValue {

    /**
     * The top value.
     */
    protected Value top;

    /**
     * The right value.
     */
    protected Value right;

    /**
     * The bottom value.
     */
    protected Value bottom;

    /**
     * The left value.
     */
    protected Value left;

    /**
     * Creates a new Rect value.
     */
    public RectValue(Value t, Value r, Value b, Value l) {
        top = t;
        right = r;
        bottom = b;
        left = l;
    }

    /**
     * The type of the value.
     */
    public short getPrimitiveType() {
        return CSSPrimitiveValue.CSS_RECT;
    }

    /**
     *  A string representation of the current value. 
     */
    public String getCssText() {
        return "rect(" + top.getCssText() + ", " + right.getCssText() + ", " + bottom.getCssText() + ", " + left.getCssText() + ")";
    }

    /**
     * Implements {@link Value#getTop()}.
     */
    public Value getTop() throws DOMException {
        return top;
    }

    /**
     * Implements {@link Value#getRight()}.
     */
    public Value getRight() throws DOMException {
        return right;
    }

    /**
     * Implements {@link Value#getBottom()}.
     */
    public Value getBottom() throws DOMException {
        return bottom;
    }

    /**
     * Implements {@link Value#getLeft()}.
     */
    public Value getLeft() throws DOMException {
        return left;
    }

    /**
     * Returns a printable representation of this value.
     */
    public String toString() {
        return getCssText();
    }
}
