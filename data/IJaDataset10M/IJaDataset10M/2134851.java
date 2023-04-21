package items.componentsItem;

import items.TermIF;

/** */
public class SinTerm extends AbstractTerm {

    public SinTerm(double c, double a) {
        super(c, a);
    }

    /** */
    public double evaluate(double x) {
        return coeff * Math.sin(attribute * x);
    }

    /** */
    public TermIF differentiate() {
        return new CosTerm(coeff * attribute, attribute);
    }

    /** */
    public String toString() {
        return coeff + " sin (" + attribute + " X)";
    }

    /** */
    public void addTerm(TermIF term) {
    }
}
