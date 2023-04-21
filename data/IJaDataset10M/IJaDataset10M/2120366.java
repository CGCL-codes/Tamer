package uk.co.wilson.ng.runtime.metaclass.primitives.intimpl;

import java.math.BigDecimal;
import java.math.BigInteger;
import ng.runtime.metaclass.LogicalBinaryOperation;
import ng.runtime.metaclass.primitives.intimpl.IntBinaryLogicalOperation;
import ng.runtime.threadcontext.ExtendedThreadContext;
import ng.runtime.threadcontext.NotPerformed;
import uk.co.wilson.ng.runtime.metaclass.primitives.ArithmeticOperationWrapper;

/**
 * @author John
 * 
 */
public class IntBinaryLogicalOperationWrapper extends ArithmeticOperationWrapper implements IntBinaryLogicalOperation {

    /**
   * @param delegate
   */
    public IntBinaryLogicalOperationWrapper(final LogicalBinaryOperation delegate) {
        super(delegate);
    }

    public Object doApply(final ExtendedThreadContext tc, final int lhs, final BigDecimal rhs) {
        return doApply(tc, tc.wrap(lhs), rhs);
    }

    public Object doApply(final ExtendedThreadContext tc, final int lhs, final BigInteger rhs) {
        return doApply(tc, tc.wrap(lhs), rhs);
    }

    public Object doApply(final ExtendedThreadContext tc, final int lhs, final byte rhs) {
        return doApply(tc, tc.wrap(lhs), rhs);
    }

    public Object doApply(final ExtendedThreadContext tc, final int lhs, final char rhs) {
        return doApply(tc, tc.wrap(lhs), rhs);
    }

    public Object doApply(final ExtendedThreadContext tc, final int lhs, final double rhs) {
        return doApply(tc, tc.wrap(lhs), rhs);
    }

    public Object doApply(final ExtendedThreadContext tc, final int lhs, final float rhs) {
        return doApply(tc, tc.wrap(lhs), rhs);
    }

    public Object doApply(final ExtendedThreadContext tc, final int lhs, final int rhs) {
        return doApply(tc, tc.wrap(lhs), rhs);
    }

    public Object doApply(final ExtendedThreadContext tc, final int lhs, final long rhs) {
        return doApply(tc, tc.wrap(lhs), rhs);
    }

    public Object doApply(final ExtendedThreadContext tc, final int lhs, final short rhs) {
        return doApply(tc, tc.wrap(lhs), rhs);
    }

    public BigInteger doBigIntegerApply(final ExtendedThreadContext tc, final int lhs, final BigInteger rhs) throws NotPerformed {
        throw ExtendedThreadContext.NOT_PERFORMED_EXCEPTION;
    }

    public int doIntApply(final ExtendedThreadContext tc, final int lhs, final byte rhs) throws NotPerformed {
        throw ExtendedThreadContext.NOT_PERFORMED_EXCEPTION;
    }

    public int doIntApply(final ExtendedThreadContext tc, final int lhs, final char rhs) throws NotPerformed {
        throw ExtendedThreadContext.NOT_PERFORMED_EXCEPTION;
    }

    public int doIntApply(final ExtendedThreadContext tc, final int lhs, final int rhs) throws NotPerformed {
        throw ExtendedThreadContext.NOT_PERFORMED_EXCEPTION;
    }

    public int doIntApply(final ExtendedThreadContext tc, final int lhs, final short rhs) throws NotPerformed {
        throw ExtendedThreadContext.NOT_PERFORMED_EXCEPTION;
    }

    public long doLongApply(final ExtendedThreadContext tc, final int lhs, final long rhs) throws NotPerformed {
        throw ExtendedThreadContext.NOT_PERFORMED_EXCEPTION;
    }
}
