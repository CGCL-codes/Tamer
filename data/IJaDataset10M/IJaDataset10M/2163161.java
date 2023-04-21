package uk.co.wilson.ng.runtime.metaclass.primitives.bigdecimal;

import java.math.BigDecimal;
import java.math.BigInteger;
import ng.runtime.metaclass.primitives.bigdecimal.BigDecimalBinaryOperation;
import ng.runtime.threadcontext.ExtendedThreadContext;

abstract class BaseBinaryOperation implements BigDecimalBinaryOperation {

    public Object doApply(final ExtendedThreadContext tc, final Object lhs, final char rhs) {
        return doApply(tc, (BigDecimal) lhs, rhs);
    }

    public Object doApply(final ExtendedThreadContext tc, final Object lhs, final byte rhs) {
        return doApply(tc, (BigDecimal) lhs, rhs);
    }

    public Object doApply(final ExtendedThreadContext tc, final Object lhs, final short rhs) {
        return doApply(tc, (BigDecimal) lhs, rhs);
    }

    public Object doApply(final ExtendedThreadContext tc, final Object lhs, final int rhs) {
        return doApply(tc, (BigDecimal) lhs, rhs);
    }

    public Object doApply(final ExtendedThreadContext tc, final Object lhs, final long rhs) {
        return doApply(tc, (BigDecimal) lhs, rhs);
    }

    public Object doApply(final ExtendedThreadContext tc, final Object lhs, final float rhs) {
        return doApply(tc, (BigDecimal) lhs, rhs);
    }

    public Object doApply(final ExtendedThreadContext tc, final Object lhs, final double rhs) {
        return doApply(tc, (BigDecimal) lhs, rhs);
    }

    public Object doApply(final ExtendedThreadContext tc, final Object lhs, final BigInteger rhs) {
        return doApply(tc, (BigDecimal) lhs, rhs);
    }

    public Object doApply(final ExtendedThreadContext tc, final Object lhs, final BigDecimal rhs) {
        return doApply(tc, (BigDecimal) lhs, rhs);
    }
}
