package net.sf.javascribe.generator.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DCS
 * This class represents a parameter expression.
 * TODO: Decide whether to have separate classes for different types of expressions, or just use one expression class.
 */
public class ParameterExpression {

    List parameters = null;

    public int getSize() {
        return parameters.size();
    }

    public ParameterExpression(List params) {
        parameters = params;
    }

    public ParameterExpression() {
        parameters = new ArrayList();
    }

    public ParameterExpressionAtom getAtom(int i) {
        ParameterExpressionAtom ret = null;
        ret = (ParameterExpressionAtom) parameters.get(i);
        return ret;
    }
}
