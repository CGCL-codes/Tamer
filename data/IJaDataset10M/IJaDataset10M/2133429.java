package org.w3c.dom.css;

/**
 *  The <code>Rect</code> interface is used to represent any rect value. This 
 * interface reflects the values in the underlying style property. Hence, 
 * modifications made to the <code>CSSPrimitiveValue</code> objects modify 
 * the style property. 
 * <p>See also the <a href='http://www.w3.org/TR/2000/REC-DOM-Level-2-Style-20001113'>Document Object Model (DOM) Level 2 Style Specification</a>.
 * @since DOM Level 2
 */
public interface Rect {

    /**
     *  This attribute is used for the top of the rect. 
     */
    public CSSPrimitiveValue getTop();

    /**
     *  This attribute is used for the right of the rect. 
     */
    public CSSPrimitiveValue getRight();

    /**
     *  This attribute is used for the bottom of the rect. 
     */
    public CSSPrimitiveValue getBottom();

    /**
     *  This attribute is used for the left of the rect. 
     */
    public CSSPrimitiveValue getLeft();
}
