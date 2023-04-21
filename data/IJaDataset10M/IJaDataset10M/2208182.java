package mipt.math.function.impl;

import mipt.math.function.*;
import mipt.math.Number;

public class CosFunction extends AbstractDifferentiableFunction {

    /**
 * @see mipt.math.function.Function
 */
    public final Number calc(Number value) {
        value = checkArgument(value);
        return createNumber(value, Math.cos(value.doubleValue()));
    }

    /**
 * 
 */
    protected Function initDerivative() {
        return new FunctionFactor(new SinFunction(), -1.);
    }

    /**
 * 
 */
    public String toString() {
        return "cos(x)";
    }
}
