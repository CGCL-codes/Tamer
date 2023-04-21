package uk.co.wilson.ng.runtime.metaclass.primitives.floatimpl;

import java.math.BigDecimal;
import java.math.BigInteger;
import ng.runtime.metaclass.MetaClass;
import ng.runtime.threadcontext.ExtendedThreadContext;

/**
 * @author John
 * 
 */
public class GreaterThan extends BaseBooleanComparison {

    public Object doApply(final ExtendedThreadContext tc, final Object lhs, final Object rhs) {
        return tc.greaterThan().apply(tc.unwrapToFloat(lhs), rhs);
    }

    public Object doApply(final ExtendedThreadContext tc, final Object lhs, final MetaClass rhsMetaClass, final Object rhs) {
        return tc.greaterThan().apply(tc.unwrapToFloat(lhs), rhsMetaClass, rhs);
    }

    public Object doReverseApply(final ExtendedThreadContext tc, final Object lhs, final Object rhs) {
        return tc.greaterThan().apply(lhs, tc.unwrapToFloat(rhs));
    }

    public boolean doApplyBoolean(final ExtendedThreadContext tc, final Object lhs, final Object rhs) {
        return tc.greaterThan().applyBoolean(tc.unwrapToFloat(lhs), rhs);
    }

    public boolean doApplyBoolean(final ExtendedThreadContext tc, final Object lhs, final MetaClass rhsMetaClass, final Object rhs) {
        return tc.greaterThan().applyBoolean(tc.unwrapToFloat(lhs), rhsMetaClass, rhs);
    }

    public boolean doReverseApplyBoolean(final ExtendedThreadContext tc, final Object lhs, final Object rhs) {
        return tc.greaterThan().applyBoolean(lhs, tc.unwrapToFloat(rhs));
    }

    public boolean doApplyBoolean(final ExtendedThreadContext tc, final char lhs, final Object rhs) {
        return tc.greaterThan().applyBoolean(lhs, tc.unwrapToFloat(rhs));
    }

    public boolean doApplyBoolean(final ExtendedThreadContext tc, final byte lhs, final Object rhs) {
        return tc.greaterThan().applyBoolean(lhs, tc.unwrapToFloat(rhs));
    }

    public boolean doApplyBoolean(final ExtendedThreadContext tc, final short lhs, final Object rhs) {
        return tc.greaterThan().applyBoolean(lhs, tc.unwrapToFloat(rhs));
    }

    public boolean doApplyBoolean(final ExtendedThreadContext tc, final int lhs, final Object rhs) {
        return tc.greaterThan().applyBoolean(lhs, tc.unwrapToFloat(rhs));
    }

    public boolean doApplyBoolean(final ExtendedThreadContext tc, final long lhs, final Object rhs) {
        return tc.greaterThan().applyBoolean(lhs, tc.unwrapToFloat(rhs));
    }

    public boolean doApplyBoolean(final ExtendedThreadContext tc, final float lhs, final Object rhs) {
        return tc.greaterThan().applyBoolean(lhs, tc.unwrapToFloat(rhs));
    }

    public boolean doApplyBoolean(final ExtendedThreadContext tc, final double lhs, final Object rhs) {
        return tc.greaterThan().applyBoolean(lhs, tc.unwrapToFloat(rhs));
    }

    public boolean doApplyBoolean(final ExtendedThreadContext tc, final BigInteger lhs, final Object rhs) {
        return tc.greaterThan().applyBoolean(lhs, tc.unwrapToFloat(rhs));
    }

    public boolean doApplyBoolean(final ExtendedThreadContext tc, final BigDecimal lhs, final Object rhs) {
        return tc.greaterThan().applyBoolean(lhs, tc.unwrapToFloat(rhs));
    }

    public boolean doApplyBoolean(final ExtendedThreadContext tc, final float lhs, final char rhs) {
        return lhs > rhs;
    }

    public boolean doApplyBoolean(final ExtendedThreadContext tc, final float lhs, final byte rhs) {
        return lhs > rhs;
    }

    public boolean doApplyBoolean(final ExtendedThreadContext tc, final float lhs, final short rhs) {
        return lhs > rhs;
    }

    public boolean doApplyBoolean(final ExtendedThreadContext tc, final float lhs, final int rhs) {
        return lhs > rhs;
    }

    public boolean doApplyBoolean(final ExtendedThreadContext tc, final float lhs, final long rhs) {
        return lhs > rhs;
    }

    public boolean doApplyBoolean(final ExtendedThreadContext tc, final float lhs, final float rhs) {
        return lhs > rhs;
    }

    public boolean doApplyBoolean(final ExtendedThreadContext tc, final float lhs, final double rhs) {
        return lhs > rhs;
    }

    public boolean doApplyBoolean(final ExtendedThreadContext tc, final float lhs, final BigInteger rhs) {
        return BigInteger.valueOf((long) lhs).compareTo(rhs) > 0;
    }

    public boolean doApplyBoolean(final ExtendedThreadContext tc, final float lhs, final BigDecimal rhs) {
        return BigDecimal.valueOf(lhs).compareTo(rhs) > 0;
    }
}
