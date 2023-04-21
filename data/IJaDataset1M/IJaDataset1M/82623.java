package org.jscience.mathMLImpl;

import org.w3c.dom.mathML.MathMLElement;
import org.w3c.dom.mathML.MathMLFractionElement;

/**
 * Implements a MathML fraction element.
 *
 * @author Mark Hale
 * @version 1.0
 */
public class MathMLFractionElementImpl extends MathMLElementImpl implements MathMLFractionElement {

    /**
     * Constructs a MathML fraction element.
     *
     * @param owner         DOCUMENT ME!
     * @param qualifiedName DOCUMENT ME!
     */
    public MathMLFractionElementImpl(MathMLDocumentImpl owner, String qualifiedName) {
        super(owner, qualifiedName);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getLinethickness() {
        return getAttribute("linethickness");
    }

    /**
     * DOCUMENT ME!
     *
     * @param linethickness DOCUMENT ME!
     */
    public void setLinethickness(String linethickness) {
        setAttribute("linethickness", linethickness);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MathMLElement getNumerator() {
        return (MathMLElement) getFirstChild();
    }

    /**
     * DOCUMENT ME!
     *
     * @param numerator DOCUMENT ME!
     */
    public void setNumerator(MathMLElement numerator) {
        replaceChild(numerator, getFirstChild());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MathMLElement getDenominator() {
        return (MathMLElement) item(1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param denominator DOCUMENT ME!
     */
    public void setDenominator(MathMLElement denominator) {
        replaceChild(denominator, item(1));
    }
}
