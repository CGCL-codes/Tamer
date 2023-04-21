package org.jscience.mathMLImpl;

import org.w3c.dom.mathML.MathMLBvarElement;
import org.w3c.dom.mathML.MathMLListElement;

/**
 * Implements a MathML <code>list</code> element.
 *
 * @author Mark Hale
 * @version 1.0
 */
public class MathMLListElementImpl extends MathMLContentContainerImpl implements MathMLListElement {

    /**
     * Constructs a MathML <code>list</code> element.
     *
     * @param owner         DOCUMENT ME!
     * @param qualifiedName DOCUMENT ME!
     */
    public MathMLListElementImpl(MathMLDocumentImpl owner, String qualifiedName) {
        super(owner, qualifiedName);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getIsExplicit() {
        return !(getFirstChild() instanceof MathMLBvarElement);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getOrdering() {
        return getAttribute("order");
    }

    /**
     * DOCUMENT ME!
     *
     * @param ordering DOCUMENT ME!
     */
    public void setOrdering(String ordering) {
        setAttribute("order", ordering);
    }
}
