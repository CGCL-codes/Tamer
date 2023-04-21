package uk.co.wilson.ng.runtime.metaclass.primitives.byteimpl;

import java.math.BigDecimal;
import java.math.BigInteger;
import ng.runtime.metaclass.ShiftOperation;
import ng.runtime.metaclass.primitives.byteimpl.ByteShiftOperation;
import ng.runtime.threadcontext.ExtendedThreadContext;
import ng.runtime.threadcontext.NotPerformed;
import uk.co.wilson.ng.runtime.metaclass.primitives.ArithmeticOperationWrapper;

/**
 * @author John
 * 
 */
public class ByteShiftOperationWrapper extends ArithmeticOperationWrapper implements ByteShiftOperation {

    /**
   * @param delegate
   */
    public ByteShiftOperationWrapper(final ShiftOperation delegate) {
        super(delegate);
    }

    public Object doApply(final ExtendedThreadContext tc, final byte lhs, final BigDecimal rhs) {
        return doApply(tc, tc.wrap(lhs), rhs);
    }

    public Object doApply(final ExtendedThreadContext tc, final byte lhs, final BigInteger rhs) {
        return doApply(tc, tc.wrap(lhs), rhs);
    }

    public Object doApply(final ExtendedThreadContext tc, final byte lhs, final byte rhs) {
        return doApply(tc, tc.wrap(lhs), rhs);
    }

    public Object doApply(final ExtendedThreadContext tc, final byte lhs, final char rhs) {
        return doApply(tc, tc.wrap(lhs), rhs);
    }

    public Object doApply(final ExtendedThreadContext tc, final byte lhs, final double rhs) {
        return doApply(tc, tc.wrap(lhs), rhs);
    }

    public Object doApply(final ExtendedThreadContext tc, final byte lhs, final float rhs) {
        return doApply(tc, tc.wrap(lhs), rhs);
    }

    public Object doApply(final ExtendedThreadContext tc, final byte lhs, final int rhs) {
        return doApply(tc, tc.wrap(lhs), rhs);
    }

    public Object doApply(final ExtendedThreadContext tc, final byte lhs, final long rhs) {
        return doApply(tc, tc.wrap(lhs), rhs);
    }

    public Object doApply(final ExtendedThreadContext tc, final byte lhs, final short rhs) {
        return doApply(tc, tc.wrap(lhs), rhs);
    }

    public int doIntApply(final ExtendedThreadContext tc, final byte lhs, final BigInteger rhs) throws NotPerformed {
        throw ExtendedThreadContext.NOT_PERFORMED_EXCEPTION;
    }

    public int doIntApply(final ExtendedThreadContext tc, final byte lhs, final byte rhs) throws NotPerformed {
        throw ExtendedThreadContext.NOT_PERFORMED_EXCEPTION;
    }

    public int doIntApply(final ExtendedThreadContext tc, final byte lhs, final char rhs) throws NotPerformed {
        throw ExtendedThreadContext.NOT_PERFORMED_EXCEPTION;
    }

    public int doIntApply(final ExtendedThreadContext tc, final byte lhs, final int rhs) throws NotPerformed {
        throw ExtendedThreadContext.NOT_PERFORMED_EXCEPTION;
    }

    public int doIntApply(final ExtendedThreadContext tc, final byte lhs, final short rhs) throws NotPerformed {
        throw ExtendedThreadContext.NOT_PERFORMED_EXCEPTION;
    }

    public int doIntApply(final ExtendedThreadContext tc, final byte lhs, final long rhs) throws NotPerformed {
        throw ExtendedThreadContext.NOT_PERFORMED_EXCEPTION;
    }
}
