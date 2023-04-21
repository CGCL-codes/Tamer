package de.maramuse.soundcomp.parser.math;

import de.maramuse.soundcomp.parser.Number;
import de.maramuse.soundcomp.parser.SCParser;
import de.maramuse.soundcomp.parser.TemplateProvider;
import de.maramuse.soundcomp.parser.SCParser.ParserVal;
import de.maramuse.soundcomp.process.ProcessElement;

/**
 * This parser symbol represents a mathematical division operator
 */
public class Div extends ParserVal implements TemplateProvider, FormulaElement2 {

    private static final ProcessElement template = new de.maramuse.soundcomp.math.div();

    public Div(String s) {
        super(SCParser.DIV, s);
    }

    @Override
    public ParserVal eliminateConst() {
        if (inner.size() != 2) throw new InternalError("\"/\" requires two arguments " + getLocationText());
        if (inner.get(0).isConstant() && inner.get(1).isConstant()) {
            if (inner.get(0).getType() == SCParser.NUMBER && inner.get(1).getType() == SCParser.NUMBER) {
                try {
                    ParserVal ret = new Number(inner.get(0).asDouble() / inner.get(1).asDouble());
                    ret.setFilename(filename);
                    ret.setLine(line);
                    return ret;
                } catch (NumberFormatException ex) {
                    throw new IllegalArgumentException("should not happen: number format error", ex);
                }
            }
        }
        return this;
    }

    @Override
    public ProcessElement getTemplate() {
        return template;
    }

    @Override
    public ParserVal getInput2Val() {
        return inner.get(1);
    }

    @Override
    public ParserVal getInput1Val() {
        return inner.get(0);
    }
}
