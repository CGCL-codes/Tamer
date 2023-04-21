package com.symbolicplotter.interpreter.basicfunctions;

import com.symbolicplotter.interpreter.types.IVariant;
import com.symbolicplotter.interpreter.environment.Environment;
import com.symbolicplotter.interpreter.exceptions.SyntaxException;
import com.symbolicplotter.interpreter.exceptions.EvaluationException;
import com.symbolicplotter.interpreter.*;
import com.symbolicplotter.interpreter.types.INumber;
import com.symbolicplotter.interpreter.types.VFloat;

/**
 * Calculate the exponential of a value.
 * 
 * Accepts a parameter, and calculates its exponent.
 * 
 * f = E^a
 */
public class Exp extends Function {

    @Override
    public IVariant evaluate(Environment environment) throws EvaluationException {
        IVariant v = parameters.get(0).evaluate(environment);
        if (!v.isReal()) throw new EvaluationException("exp() expects argument 0 of type Real");
        double a = ((INumber) v).getDouble();
        return new VFloat(Math.exp(a));
    }

    @Override
    public void validate() throws SyntaxException {
        if (parameters.size() != 1) throw new SyntaxException("exp() expects exactly 1 parameter");
    }
}
