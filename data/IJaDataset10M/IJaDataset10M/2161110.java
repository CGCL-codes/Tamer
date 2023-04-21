package mipt.math.function.impl.big;

import mipt.math.BigNumber;
import mipt.math.Number;
import mipt.math.function.Function;
import mipt.math.function.impl.SinFunction;

/**
 * sin(x) function returning BigNumbers
 * Note: both this function and its derivative uses double precision
 * TO DO: implement real algorithm (http://algolist.manual.ru/maths/count_fast/sincos.php)
 *   to achieve any given precision
 */
public class BigSin extends SinFunction {

    protected int precision;

    /**
	 * You can use BigNumber.DEFAULT_PRECISION
	 */
    public BigSin(int precision) {
        this.precision = precision;
    }

    /**
	 * @see mipt.math.function.AbstractFunction#createNumber(double)
	 */
    public Number createNumber(Number sample, double value) {
        return new BigNumber(value, precision);
    }

    /**
	 * @see mipt.math.function.AbstractDifferentiableFunction#initDerivative()
	 */
    protected Function initDerivative() {
        return new BigCos(precision);
    }
}
