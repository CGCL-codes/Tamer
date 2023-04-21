package mipt.math.function.impl;

import mipt.math.function.*;
import mipt.math.Number;

public class AcosFunction extends AbstractDifferentiableFunction {

    /**
 * @see mipt.math.function.Function
 */
    public final Number calc(Number value) {
        value = checkArgument(value);
        double a = value.doubleValue();
        if (a < -1. || a > 1.) throwArgumentException("Acos argument must be >= -1 and <= 1");
        return createNumber(value, Math.acos(a));
    }

    /**
 * 
 */
    protected Function initDerivative() {
        return new FunctionFactor(new ComplexFunction(new PowFunction(-0.5), new FunctionsOperator(new TrivialFunction(Number.one()), new PowFunction(2.0), new MinusOperator())), -1.);
    }

    /**
 * 
 */
    public String toString() {
        return "acos(x)";
    }
}
