package uk.co.wilson.ng.runtime.metaclass.primitives.byteimpl;

import java.math.BigDecimal;
import java.math.BigInteger;
import ng.runtime.metaclass.MetaClass;
import ng.runtime.threadcontext.ExtendedThreadContext;
import ng.runtime.threadcontext.NotPerformed;

/**
 * @author John
 * 
 */
public class And extends BaseBinaryLogicalOperation {

    public Object doApply(final ExtendedThreadContext tc, final Object lhs, final Object rhs) {
        return tc.and().apply(tc.unwrapToByte(lhs), rhs);
    }

    public Object doApply(final ExtendedThreadContext tc, final Object lhs, final MetaClass rhsMetaClass, final Object rhs) {
        return tc.and().apply(tc.unwrapToByte(lhs), rhsMetaClass, rhs);
    }

    public Object doReverseApply(final ExtendedThreadContext tc, final Object lhs, final Object rhs) {
        return tc.and().apply(lhs, tc.unwrapToByte(rhs));
    }

    public int doIntApply(final ExtendedThreadContext tc, final byte lhs, final char rhs) throws NotPerformed {
        return lhs & rhs;
    }

    public int doIntApply(final ExtendedThreadContext tc, final byte lhs, final byte rhs) throws NotPerformed {
        return lhs & rhs;
    }

    public int doIntApply(final ExtendedThreadContext tc, final byte lhs, final short rhs) throws NotPerformed {
        return lhs & rhs;
    }

    public int doIntApply(final ExtendedThreadContext tc, final byte lhs, final int rhs) throws NotPerformed {
        return lhs & rhs;
    }

    public long doLongApply(final ExtendedThreadContext tc, final byte lhs, final long rhs) throws NotPerformed {
        return lhs & rhs;
    }

    public BigInteger doBigIntegerApply(final ExtendedThreadContext tc, final byte lhs, final BigInteger rhs) throws NotPerformed {
        return BigInteger.valueOf(lhs).and(rhs);
    }

    public Object doApply(final ExtendedThreadContext tc, final char lhs, final Object rhs) {
        return tc.and().apply(lhs, tc.unwrapToByte(rhs));
    }

    public Object doApply(final ExtendedThreadContext tc, final byte lhs, final Object rhs) {
        return tc.and().apply(lhs, tc.unwrapToByte(rhs));
    }

    public Object doApply(final ExtendedThreadContext tc, final short lhs, final Object rhs) {
        return tc.and().apply(lhs, tc.unwrapToByte(rhs));
    }

    public Object doApply(final ExtendedThreadContext tc, final int lhs, final Object rhs) {
        return tc.and().apply(lhs, tc.unwrapToByte(rhs));
    }

    public Object doApply(final ExtendedThreadContext tc, final long lhs, final Object rhs) {
        return tc.and().apply(lhs, tc.unwrapToByte(rhs));
    }

    public Object doApply(final ExtendedThreadContext tc, final float lhs, final Object rhs) {
        return tc.and().apply(lhs, tc.unwrapToByte(rhs));
    }

    public Object doApply(final ExtendedThreadContext tc, final double lhs, final Object rhs) {
        return tc.and().apply(lhs, tc.unwrapToByte(rhs));
    }

    public Object doApply(final ExtendedThreadContext tc, final BigInteger lhs, final Object rhs) {
        return tc.and().apply(lhs, tc.unwrapToByte(rhs));
    }

    public Object doApply(final ExtendedThreadContext tc, final BigDecimal lhs, final Object rhs) {
        return tc.and().apply(lhs, tc.unwrapToByte(rhs));
    }
}
