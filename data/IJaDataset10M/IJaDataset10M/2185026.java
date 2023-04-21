package mipt.math.function.impl.big;

import mipt.math.BigNumber;
import mipt.math.function.AbstractOperation;
import mipt.math.function.impl.SumOperation;

/**
 * 
 * @author Evdokimov
 */
public class BigSumOperation extends SumOperation {

    protected int precision;

    /**
	 * You can use BigNumber.DEFAULT_PRECISION
	 */
    public BigSumOperation(int precision) {
        this.precision = precision;
    }

    /**
	 * @see mipt.math.function.impl.SumOperation#clearContext(mipt.math.function.AbstractOperation.Context)
	 */
    protected void clearContext(AbstractOperation.Context context) {
        context.currentValue = BigNumber.zeroBig(precision);
    }
}
