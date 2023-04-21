package mipt.math.fuzzy.function;

import mipt.math.Number;
import mipt.math.function.impl.ExpFunction;
import mipt.math.fuzzy.ExactNumber;

/**
 * Exp(x) function returning ExactNumbers
 * Can be used as contents of DirectCombinationFunction to use with TermDirect*Numbers  
 */
public class ExactExp extends ExpFunction {

    public Number createNumber(double value) {
        return new ExactNumber(value);
    }
}
