package uk.co.wilson.ng.runtime.metaclass.primitives.longimpl;

import java.math.BigDecimal;
import java.math.BigInteger;
import ng.runtime.metaclass.BinaryOperation;
import ng.runtime.metaclass.primitives.longimpl.LongBinaryArithmeticOperation;
import ng.runtime.threadcontext.ExtendedThreadContext;
import ng.runtime.threadcontext.NotPerformed;
import uk.co.wilson.ng.runtime.metaclass.primitives.ArithmeticOperationWrapper;

/**
 * @author John
 * 
 */
public class LongBinaryArithmeticOperationWrapper extends ArithmeticOperationWrapper implements LongBinaryArithmeticOperation {

    /**
   * @param delegate
   */
    public LongBinaryArithmeticOperationWrapper(final BinaryOperation delegate) {
        super(delegate);
    }

    public Object doApply(final ExtendedThreadContext tc, final long lhs, final BigDecimal rhs) {
        return doApply(tc, tc.wrap(lhs), rhs);
    }

    public Object doApply(final ExtendedThreadContext tc, final long lhs, final BigInteger rhs) {
        return doApply(tc, tc.wrap(lhs), rhs);
    }

    public Object doApply(final ExtendedThreadContext tc, final long lhs, final byte rhs) {
        return doApply(tc, tc.wrap(lhs), rhs);
    }

    public Object doApply(final ExtendedThreadContext tc, final long lhs, final char rhs) {
        return doApply(tc, tc.wrap(lhs), rhs);
    }

    public Object doApply(final ExtendedThreadContext tc, final long lhs, final double rhs) {
        return doApply(tc, tc.wrap(lhs), rhs);
    }

    public Object doApply(final ExtendedThreadContext tc, final long lhs, final float rhs) {
        return doApply(tc, tc.wrap(lhs), rhs);
    }

    public Object doApply(final ExtendedThreadContext tc, final long lhs, final int rhs) {
        return doApply(tc, tc.wrap(lhs), rhs);
    }

    public Object doApply(final ExtendedThreadContext tc, final long lhs, final long rhs) {
        return doApply(tc, tc.wrap(lhs), rhs);
    }

    public Object doApply(final ExtendedThreadContext tc, final long lhs, final short rhs) {
        return doApply(tc, tc.wrap(lhs), rhs);
    }

    public BigDecimal doBigDecimalApply(final ExtendedThreadContext tc, final long lhs, final BigDecimal rhs) throws NotPerformed {
        throw ExtendedThreadContext.NOT_PERFORMED_EXCEPTION;
    }

    public BigInteger doBigIntegerApply(final ExtendedThreadContext tc, final long lhs, final BigInteger rhs) throws NotPerformed {
        throw ExtendedThreadContext.NOT_PERFORMED_EXCEPTION;
    }

    public long doLongApply(final ExtendedThreadContext tc, final long lhs, final byte rhs) throws NotPerformed {
        throw ExtendedThreadContext.NOT_PERFORMED_EXCEPTION;
    }

    public long doLongApply(final ExtendedThreadContext tc, final long lhs, final char rhs) throws NotPerformed {
        throw ExtendedThreadContext.NOT_PERFORMED_EXCEPTION;
    }

    public double doDoubleApply(final ExtendedThreadContext tc, final long lhs, final double rhs) throws NotPerformed {
        throw ExtendedThreadContext.NOT_PERFORMED_EXCEPTION;
    }

    public float doFloatApply(final ExtendedThreadContext tc, final long lhs, final float rhs) throws NotPerformed {
        throw ExtendedThreadContext.NOT_PERFORMED_EXCEPTION;
    }

    public long doLongApply(final ExtendedThreadContext tc, final long lhs, final int rhs) throws NotPerformed {
        throw ExtendedThreadContext.NOT_PERFORMED_EXCEPTION;
    }

    public long doLongApply(final ExtendedThreadContext tc, final long lhs, final long rhs) throws NotPerformed {
        throw ExtendedThreadContext.NOT_PERFORMED_EXCEPTION;
    }

    public long doLongApply(final ExtendedThreadContext tc, final long lhs, final short rhs) throws NotPerformed {
        throw ExtendedThreadContext.NOT_PERFORMED_EXCEPTION;
    }
}
