package uk.co.wilson.ng.runtime.metaclass.primitives.floatimpl;

import java.math.BigDecimal;
import java.math.BigInteger;
import ng.runtime.metaclass.MetaClass;
import ng.runtime.threadcontext.ExtendedThreadContext;

/**
 * @author John
 * 
 */
public class RemainderDivide extends BaseBinaryArithmeticOperation {

    public Object doApply(final ExtendedThreadContext tc, final Object lhs, final Object rhs) {
        return tc.remainderDivide().apply(tc.unwrapToFloat(lhs), rhs);
    }

    public Object doApply(final ExtendedThreadContext tc, final Object lhs, final MetaClass rhsMetaClass, final Object rhs) {
        return tc.remainderDivide().apply(tc.unwrapToFloat(lhs), rhsMetaClass, rhs);
    }

    public Object doReverseApply(final ExtendedThreadContext tc, final Object lhs, final Object rhs) {
        return tc.remainderDivide().apply(lhs, tc.unwrapToFloat(rhs));
    }

    public Object doApply(final ExtendedThreadContext tc, final char lhs, final Object rhs) {
        return tc.remainderDivide().apply(lhs, tc.unwrapToFloat(rhs));
    }

    public Object doApply(final ExtendedThreadContext tc, final byte lhs, final Object rhs) {
        return tc.remainderDivide().apply(lhs, tc.unwrapToFloat(rhs));
    }

    public Object doApply(final ExtendedThreadContext tc, final short lhs, final Object rhs) {
        return tc.remainderDivide().apply(lhs, tc.unwrapToFloat(rhs));
    }

    public Object doApply(final ExtendedThreadContext tc, final int lhs, final Object rhs) {
        return tc.remainderDivide().apply(lhs, tc.unwrapToFloat(rhs));
    }

    public Object doApply(final ExtendedThreadContext tc, final long lhs, final Object rhs) {
        return tc.remainderDivide().apply(lhs, tc.unwrapToFloat(rhs));
    }

    public Object doApply(final ExtendedThreadContext tc, final float lhs, final Object rhs) {
        return tc.remainderDivide().apply(lhs, tc.unwrapToFloat(rhs));
    }

    public Object doApply(final ExtendedThreadContext tc, final double lhs, final Object rhs) {
        return tc.remainderDivide().apply(lhs, tc.unwrapToFloat(rhs));
    }

    public Object doApply(final ExtendedThreadContext tc, final BigInteger lhs, final Object rhs) {
        return tc.remainderDivide().apply(lhs, tc.unwrapToFloat(rhs));
    }

    public Object doApply(final ExtendedThreadContext tc, final BigDecimal lhs, final Object rhs) {
        return tc.remainderDivide().apply(lhs, tc.unwrapToFloat(rhs));
    }

    public float doFloatApply(final ExtendedThreadContext tc, final float lhs, final char rhs) {
        final double tmp = lhs / rhs;
        return (float) (lhs - Math.floor(tmp) * rhs);
    }

    public float doFloatApply(final ExtendedThreadContext tc, final float lhs, final byte rhs) {
        final double tmp = lhs / rhs;
        return (float) (lhs - Math.floor(tmp) * rhs);
    }

    public float doFloatApply(final ExtendedThreadContext tc, final float lhs, final short rhs) {
        final double tmp = lhs / rhs;
        return (float) (lhs - Math.floor(tmp) * rhs);
    }

    public float doFloatApply(final ExtendedThreadContext tc, final float lhs, final int rhs) {
        final double tmp = lhs / rhs;
        return (float) (lhs - Math.floor(tmp) * rhs);
    }

    public float doFloatApply(final ExtendedThreadContext tc, final float lhs, final long rhs) {
        final double tmp = lhs / rhs;
        return (float) (lhs - Math.floor(tmp) * rhs);
    }

    public float doFloatApply(final ExtendedThreadContext tc, final float lhs, final float rhs) {
        final double tmp = lhs / rhs;
        return (float) (lhs - Math.floor(tmp) * rhs);
    }

    public double doDoubleApply(final ExtendedThreadContext tc, final float lhs, final double rhs) {
        final double tmp = lhs / rhs;
        return lhs - Math.floor(tmp) * rhs;
    }

    public float doFloatApply(final ExtendedThreadContext tc, final float lhs, final BigInteger rhs) {
        final double tmp = lhs / rhs.longValue();
        return (float) (lhs - Math.floor(tmp) * rhs.longValue());
    }

    public BigDecimal doBigDecimalApply(final ExtendedThreadContext tc, final float lhs, final BigDecimal rhs) {
        return BigDecimal.valueOf(lhs).divideAndRemainder(rhs)[1];
    }
}
