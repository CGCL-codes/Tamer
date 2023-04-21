package mipt.math.function.impl.count;

import mipt.math.CountScalarNumber;
import mipt.math.Number;
import mipt.math.function.impl.StepFunction;

/**
 * Step(x) function counting calls and optionally returning CountScalarNumbers
 */
public class CountStep extends StepFunction {

    protected boolean createCountNumbers;

    protected boolean countCalls = true;

    public CountStep(boolean createCountNumbers, boolean countCalls) {
        this.createCountNumbers = createCountNumbers;
        this.countCalls = countCalls;
    }

    public CountStep() {
    }

    /**
	 * @see mipt.math.function.AbstractFunction#createNumber(double)
	 */
    public Number createNumber(Number sample, double value) {
        if (countCalls) Counter.functionEvaluated();
        return createCountNumbers ? new CountScalarNumber(value) : super.createNumber(sample, value);
    }
}
