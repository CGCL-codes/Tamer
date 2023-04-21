package mipt.math.function.impl.count;

import mipt.math.CountScalarNumber;
import mipt.math.Number;
import mipt.math.function.impl.AbsFunction;

/**
 * Abs(x) function counting calls and optionally returning CountScalarNumbers
 */
public class CountAbs extends AbsFunction {

    protected boolean createCountNumbers;

    protected boolean countCalls = true;

    public CountAbs(boolean createCountNumbers, boolean countCalls) {
        this.createCountNumbers = createCountNumbers;
        this.countCalls = countCalls;
    }

    public CountAbs() {
    }

    /**
	 * @see mipt.math.function.AbstractFunction#createNumber(double)
	 */
    public Number createNumber(Number sample, double value) {
        if (countCalls) Counter.functionEvaluated();
        return createCountNumbers ? new CountScalarNumber(value) : super.createNumber(sample, value);
    }
}
