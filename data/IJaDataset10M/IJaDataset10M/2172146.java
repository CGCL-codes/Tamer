package org.jscience.mathMLImpl;

import org.w3c.dom.mathML.MathMLAlignMarkElement;

/**
 * Implements a MathML align mark element.
 *
 * @author Mark Hale
 * @version 1.0
 */
public class MathMLAlignMarkElementImpl extends MathMLElementImpl implements MathMLAlignMarkElement {

    /**
     * Constructs a MathML align mark element.
     *
     * @param owner         DOCUMENT ME!
     * @param qualifiedName DOCUMENT ME!
     */
    public MathMLAlignMarkElementImpl(MathMLDocumentImpl owner, String qualifiedName) {
        super(owner, qualifiedName);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getEdge() {
        return getAttribute("edge");
    }

    /**
     * DOCUMENT ME!
     *
     * @param edge DOCUMENT ME!
     */
    public void setEdge(String edge) {
        setAttribute("edge", edge);
    }
}
