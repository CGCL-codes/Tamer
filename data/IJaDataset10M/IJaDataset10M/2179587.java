package de.maramuse.soundcomp.parser.math;

import de.maramuse.soundcomp.parser.Number;
import de.maramuse.soundcomp.parser.SCParser;
import de.maramuse.soundcomp.parser.TemplateProvider;
import de.maramuse.soundcomp.parser.SCParser.ParserVal;
import de.maramuse.soundcomp.process.ProcessElement;

/**
 * this symbol class represents a mathematical sine operation
 */
public class MSin extends ParserVal implements TemplateProvider, FormulaElement1 {

    private static final ProcessElement template = new de.maramuse.soundcomp.math.sin();

    public MSin(String s) {
        super(SCParser.MSIN, s);
    }

    @Override
    public ParserVal eliminateConst() {
        if (inner.size() == 1) {
            if (inner.get(0).isConstant()) return new Number(Math.sin(inner.get(0).asDouble()));
            return this;
        }
        throw new IllegalArgumentException("sine needs one argument " + getLocationText());
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
