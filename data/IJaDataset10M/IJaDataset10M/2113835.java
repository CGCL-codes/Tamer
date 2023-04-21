package uk.co.wilson.ng.runtime.metaclass.primitives.intimpl;

import java.math.BigDecimal;
import java.math.BigInteger;
import ng.runtime.metaclass.primitives.intimpl.IntBinaryLogicalOperation;
import ng.runtime.threadcontext.ExtendedThreadContext;
import ng.runtime.threadcontext.NotPerformed;

/**
 * @author John
 * 
 */
public abstract class BaseBinaryLogicalOperation extends BaseBinaryOperation implements IntBinaryLogicalOperation {

    public Object doApply(final ExtendedThreadContext tc, final int lhs, final char rhs) {
        try {
            return tc.wrap(doIntApply(tc, lhs, rhs));
        } catch (final NotPerformed e) {
            return ExtendedThreadContext.NOT_PERFORMED;
        }
    }

    public Object doApply(final ExtendedThreadContext tc, final int lhs, final byte rhs) {
        try {
            return tc.wrap(doIntApply(tc, lhs, rhs));
        } catch (final NotPerformed e) {
            return ExtendedThreadContext.NOT_PERFORMED;
        }
    }

    public Object doApply(final ExtendedThreadContext tc, final int lhs, final short rhs) {
        try {
            return tc.wrap(doIntApply(tc, lhs, rhs));
        } catch (final NotPerformed e) {
            return ExtendedThreadContext.NOT_PERFORMED;
        }
    }

    public Object doApply(final ExtendedThreadContext tc, final int lhs, final int rhs) {
        try {
            return tc.wrap(doIntApply(tc, lhs, rhs));
        } catch (final NotPerformed e) {
            return ExtendedThreadContext.NOT_PERFORMED;
        }
    }

    public Object doApply(final ExtendedThreadContext tc, final int lhs, final long rhs) {
        try {
            return tc.wrap(doLongApply(tc, lhs, rhs));
        } catch (final NotPerformed e) {
            return ExtendedThreadContext.NOT_PERFORMED;
        }
    }

    public Object doApply(final ExtendedThreadContext tc, final int lhs, final float rhs) {
        return ExtendedThreadContext.NOT_PERFORMED;
    }

    public Object doApply(final ExtendedThreadContext tc, final int lhs, final double rhs) {
        return ExtendedThreadContext.NOT_PERFORMED;
    }

    public Object doApply(final ExtendedThreadContext tc, final int lhs, final BigInteger rhs) {
        try {
            return doBigIntegerApply(tc, lhs, rhs);
        } catch (final NotPerformed e) {
            return ExtendedThreadContext.NOT_PERFORMED;
        }
    }

    public Object doApply(final ExtendedThreadContext tc, final int lhs, final BigDecimal rhs) {
        return ExtendedThreadContext.NOT_PERFORMED;
    }
}
