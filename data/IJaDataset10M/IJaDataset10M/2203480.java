package org.matheclipse.core.stat.descriptive.summary;

import static org.matheclipse.core.expression.F.eval;
import java.io.Serializable;
import org.apache.commons.math3.util.FastMath;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.stat.descriptive.AbstractSymbolicStorelessUnivariateStatistic;

/**
 * Returns the sum of the natural logs for this collection of values.
 * <p>
 * Therefore,
 * <ul>
 * <li>If any of values are &lt; 0, the result is <code>NaN.</code></li>
 * <li>If all values are non-negative and less than
 * <code>Double.POSITIVE_INFINITY</code>, but at least one value is 0, the
 * result is <code>Double.NEGATIVE_INFINITY.</code></li>
 * <li>If both <code>Double.POSITIVE_INFINITY</code> and
 * <code>Double.NEGATIVE_INFINITY</code> are among the values, the result is
 * <code>NaN.</code></li>
 * </ul>
 * </p>
 * <p>
 * <strong>Note that this implementation is not synchronized.</strong> If
 * multiple threads access an instance of this class concurrently, and at least
 * one of the threads invokes the <code>increment()</code> or
 * <code>clear()</code> method, it must be synchronized externally.
 * </p>
 * 
 */
public class SymbolicSumOfLogs extends AbstractSymbolicStorelessUnivariateStatistic implements Serializable {

    /** Serializable version identifier */
    private static final long serialVersionUID = -370076995648386763L;

    /** Number of values that have been added */
    private int n;

    /**
	 * The currently running value
	 */
    private IExpr value;

    /**
	 * Create a SumOfLogs instance
	 */
    public SymbolicSumOfLogs() {
        value = F.C0;
        n = 0;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void increment(final IExpr d) {
        value = F.eval(F.Plus(value, F.Log(d)));
        n++;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public IExpr getResult() {
        return value;
    }

    /**
	 * {@inheritDoc}
	 */
    public long getN() {
        return n;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void clear() {
        value = F.C0;
        n = 0;
    }

    /**
	 * Returns the sum of the natural logs of the entries in the specified portion
	 * of the input array, or <code>Double.NaN</code> if the designated subarray
	 * is empty.
	 * <p>
	 * Throws <code>IllegalArgumentException</code> if the array is null.
	 * </p>
	 * <p>
	 * See {@link SymbolicSumOfLogs}.
	 * </p>
	 * 
	 * @param values
	 *          the input array
	 * @param begin
	 *          index of the first array element to include
	 * @param length
	 *          the number of elements to include
	 * @return the sum of the natural logs of the values or 0 if length = 0
	 * @throws IllegalArgumentException
	 *           if the array is null or the array index parameters are not valid
	 */
    @Override
    public IExpr evaluate(final IAST values, final int begin, final int length) {
        if (test(values, begin, length, true)) {
            IAST sumLog = F.Plus();
            for (int i = begin; i < begin + length; i++) {
                sumLog.add(F.Log(values.get(i)));
            }
            if (sumLog.size() > 1) {
                return F.eval(sumLog);
            }
        }
        return null;
    }
}
