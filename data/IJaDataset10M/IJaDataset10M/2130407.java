package uk.co.wilson.ng.runtime.metaclass.primitives.floatimpl;

import java.math.BigDecimal;
import java.math.BigInteger;
import ng.runtime.metaclass.BinaryOperation;
import ng.runtime.metaclass.primitives.floatimpl.FloatBinaryArithmeticOperation;
import ng.runtime.threadcontext.ExtendedThreadContext;
import ng.runtime.threadcontext.NotPerformed;
import uk.co.wilson.ng.runtime.metaclass.primitives.ArithmeticOperationWrapper;

/**
 * @author John
 * 
 */
public class FloatBinaryArithmeticOperationWrapper extends ArithmeticOperationWrapper implements FloatBinaryArithmeticOperation {

    /**
   * @param delegate
   */
    public FloatBinaryArithmeticOperationWrapper(final BinaryOperation delegate) {
        super(delegate);
    }

    public Object doApply(final ExtendedThreadContext tc, final float lhs, final BigDecimal rhs) {
        return doApply(tc, tc.wrap(lhs), rhs);
    }

    public Object doApply(final ExtendedThreadContext tc, final float lhs, final BigInteger rhs) {
        return doApply(tc, tc.wrap(lhs), rhs);
    }

    public Object doApply(final ExtendedThreadContext tc, final float lhs, final byte rhs) {
        return doApply(tc, tc.wrap(lhs), rhs);
    }

    public Object doApply(final ExtendedThreadContext tc, final float lhs, final char rhs) {
        return doApply(tc, tc.wrap(lhs), rhs);
    }

    public Object doApply(final ExtendedThreadContext tc, final float lhs, final double rhs) {
        return doApply(tc, tc.wrap(lhs), rhs);
    }

    public Object doApply(final ExtendedThreadContext tc, final float lhs, final float rhs) {
        return doApply(tc, tc.wrap(lhs), rhs);
    }

    public Object doApply(final ExtendedThreadContext tc, final float lhs, final int rhs) {
        return doApply(tc, tc.wrap(lhs), rhs);
    }

    public Object doApply(final ExtendedThreadContext tc, final float lhs, final long rhs) {
        return doApply(tc, tc.wrap(lhs), rhs);
    }

    public Object doApply(final ExtendedThreadContext tc, final float lhs, final short rhs) {
        return doApply(tc, tc.wrap(lhs), rhs);
    }

    public BigDecimal doBigDecimalApply(final ExtendedThreadContext tc, final float lhs, final BigDecimal rhs) throws NotPerformed {
        throw ExtendedThreadContext.NOT_PERFORMED_EXCEPTION;
    }

    public float doFloatApply(final ExtendedThreadContext tc, final float lhs, final BigInteger rhs) throws NotPerformed {
        throw ExtendedThreadContext.NOT_PERFORMED_EXCEPTION;
    }

    public float doFloatApply(final ExtendedThreadContext tc, final float lhs, final byte rhs) throws NotPerformed {
        throw ExtendedThreadContext.NOT_PERFORMED_EXCEPTION;
    }

    public float doFloatApply(final ExtendedThreadContext tc, final float lhs, final char rhs) throws NotPerformed {
        throw ExtendedThreadContext.NOT_PERFORMED_EXCEPTION;
    }

    public double doDoubleApply(final ExtendedThreadContext tc, final float lhs, final double rhs) throws NotPerformed {
        throw ExtendedThreadContext.NOT_PERFORMED_EXCEPTION;
    }

    public float doFloatApply(final ExtendedThreadContext tc, final float lhs, final float rhs) throws NotPerformed {
        throw ExtendedThreadContext.NOT_PERFORMED_EXCEPTION;
    }

    public float doFloatApply(final ExtendedThreadContext tc, final float lhs, final int rhs) throws NotPerformed {
        throw ExtendedThreadContext.NOT_PERFORMED_EXCEPTION;
    }

    public float doFloatApply(final ExtendedThreadContext tc, final float lhs, final long rhs) throws NotPerformed {
        throw ExtendedThreadContext.NOT_PERFORMED_EXCEPTION;
    }

    public float doFloatApply(final ExtendedThreadContext tc, final float lhs, final short rhs) throws NotPerformed {
        throw ExtendedThreadContext.NOT_PERFORMED_EXCEPTION;
    }
}
