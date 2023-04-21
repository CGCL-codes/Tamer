package uk.co.wilson.ng.runtime.metaclass.primitives.floatimpl;

import java.math.BigDecimal;
import java.math.BigInteger;
import ng.runtime.metaclass.MetaClass;
import ng.runtime.threadcontext.ExtendedThreadContext;

/**
 * @author John
 * 
 */
public class Modulo extends BaseBinaryArithmeticOperation {

    public Object doApply(final ExtendedThreadContext tc, final Object lhs, final Object rhs) {
        return tc.modulo().apply(tc.unwrapToFloat(lhs), rhs);
    }

    public Object doApply(final ExtendedThreadContext tc, final Object lhs, final MetaClass rhsMetaClass, final Object rhs) {
        return tc.modulo().apply(tc.unwrapToFloat(lhs), rhsMetaClass, rhs);
    }

    public Object doReverseApply(final ExtendedThreadContext tc, final Object lhs, final Object rhs) {
        return tc.modulo().apply(lhs, tc.unwrapToFloat(rhs));
    }

    public Object doApply(final ExtendedThreadContext tc, final char lhs, final Object rhs) {
        return tc.modulo().apply(lhs, tc.unwrapToFloat(rhs));
    }

    public Object doApply(final ExtendedThreadContext tc, final byte lhs, final Object rhs) {
        return tc.modulo().apply(lhs, tc.unwrapToFloat(rhs));
    }

    public Object doApply(final ExtendedThreadContext tc, final short lhs, final Object rhs) {
        return tc.modulo().apply(lhs, tc.unwrapToFloat(rhs));
    }

    public Object doApply(final ExtendedThreadContext tc, final int lhs, final Object rhs) {
        return tc.modulo().apply(lhs, tc.unwrapToFloat(rhs));
    }

    public Object doApply(final ExtendedThreadContext tc, final long lhs, final Object rhs) {
        return tc.modulo().apply(lhs, tc.unwrapToFloat(rhs));
    }

    public Object doApply(final ExtendedThreadContext tc, final float lhs, final Object rhs) {
        return tc.modulo().apply(lhs, tc.unwrapToFloat(rhs));
    }

    public Object doApply(final ExtendedThreadContext tc, final double lhs, final Object rhs) {
        return tc.modulo().apply(lhs, tc.unwrapToFloat(rhs));
    }

    public Object doApply(final ExtendedThreadContext tc, final BigInteger lhs, final Object rhs) {
        return tc.modulo().apply(lhs, tc.unwrapToFloat(rhs));
    }

    public Object doApply(final ExtendedThreadContext tc, final BigDecimal lhs, final Object rhs) {
        return tc.modulo().apply(lhs, tc.unwrapToFloat(rhs));
    }

    public float doFloatApply(final ExtendedThreadContext tc, final float lhs, final char rhs) {
        return (float) Math.floor(lhs / rhs);
    }

    public float doFloatApply(final ExtendedThreadContext tc, final float lhs, final byte rhs) {
        return (float) Math.floor(lhs / rhs);
    }

    public float doFloatApply(final ExtendedThreadContext tc, final float lhs, final short rhs) {
        return (float) Math.floor(lhs / rhs);
    }

    public float doFloatApply(final ExtendedThreadContext tc, final float lhs, final int rhs) {
        return (float) Math.floor(lhs / rhs);
    }

    public float doFloatApply(final ExtendedThreadContext tc, final float lhs, final long rhs) {
        return (float) Math.floor(lhs / rhs);
    }

    public float doFloatApply(final ExtendedThreadContext tc, final float lhs, final float rhs) {
        return (float) Math.floor(lhs / rhs);
    }

    public double doDoubleApply(final ExtendedThreadContext tc, final float lhs, final double rhs) {
        return Math.floor(lhs / rhs);
    }

    public float doFloatApply(final ExtendedThreadContext tc, final float lhs, final BigInteger rhs) {
        return (float) Math.floor(lhs / rhs.floatValue());
    }

    public BigDecimal doBigDecimalApply(final ExtendedThreadContext tc, final float lhs, final BigDecimal rhs) {
        return BigDecimal.valueOf(lhs).divideToIntegralValue(rhs);
    }
}
