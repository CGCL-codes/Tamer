package de.maramuse.soundcomp.parser.math;

import de.maramuse.soundcomp.parser.Element;
import de.maramuse.soundcomp.parser.Number;
import de.maramuse.soundcomp.parser.SCParser;
import de.maramuse.soundcomp.parser.TemplateProvider;
import de.maramuse.soundcomp.parser.SCParser.ParserVal;
import de.maramuse.soundcomp.process.ProcessElement;

public class Ident extends ParserVal implements TemplateProvider, FormulaElement1 {

    private static final ProcessElement template = new de.maramuse.soundcomp.math.ident();

    public Ident(String s) {
        super(SCParser.IDENT, s);
    }

    @Override
    public ParserVal eliminateConst() {
        if (inner.size() == 1) {
            if (inner.get(0).isConstant()) return new Number(Math.acos(Double.parseDouble(inner.get(0).getText())));
            return this;
        }
        throw new IllegalArgumentException("ident needs one argument " + getLocationText());
    }

    /**
   * sets the element to which the relation is "identical"
   * @param elem the element
   */
    public void setIdentity(Element elem) {
        append(elem);
    }

    @Override
    public ProcessElement getTemplate() {
        return template;
    }

    @Override
    public ParserVal getInput1Val() {
        return inner.get(0);
    }
}
