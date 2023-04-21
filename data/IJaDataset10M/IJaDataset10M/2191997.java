package mipt.math.function.impl;

import mipt.math.function.AbstractOperator;
import mipt.math.Number;

public class DivideOperator extends AbstractOperator {

    /**
	 * @see mipt.math.function.Operator
	 */
    public final Number calc(Number a, Number b) {
        b = checkArgument(b);
        return a == null ? b.inverse() : Number.divide(a, b);
    }

    /**
	 * @see mipt.math.function.Operator
	 */
    public int getPriority() {
        return AVERAGE_PRIORITY;
    }

    /**
	 * 
	 */
    public String toString() {
        return "/";
    }
}
