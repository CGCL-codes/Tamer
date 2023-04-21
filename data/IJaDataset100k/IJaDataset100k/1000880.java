package ng.runtime.metaclass;

import java.math.BigDecimal;
import java.math.BigInteger;
import ng.runtime.threadcontext.ExtendedThreadContext;

public interface BinaryOperation {

    Object doApply(ExtendedThreadContext tc, Object lhs, Object rhs);

    Object doApply(ExtendedThreadContext tc, Object lhs, MetaClass rhsMetaClass, Object rhs);

    Object doReverseApply(ExtendedThreadContext tc, Object lhs, Object rhs);

    Object doApply(ExtendedThreadContext tc, Object lhs, char rhs);

    Object doApply(ExtendedThreadContext tc, Object lhs, byte rhs);

    Object doApply(ExtendedThreadContext tc, Object lhs, short rhs);

    Object doApply(ExtendedThreadContext tc, Object lhs, int rhs);

    Object doApply(ExtendedThreadContext tc, Object lhs, long rhs);

    Object doApply(ExtendedThreadContext tc, Object lhs, float rhs);

    Object doApply(ExtendedThreadContext tc, Object lhs, double rhs);

    Object doApply(ExtendedThreadContext tc, Object lhs, BigInteger rhs);

    Object doApply(ExtendedThreadContext tc, Object lhs, BigDecimal rhs);

    Object doApply(ExtendedThreadContext tc, char lhs, Object rhs);

    Object doApply(ExtendedThreadContext tc, byte lhs, Object rhs);

    Object doApply(ExtendedThreadContext tc, short lhs, Object rhs);

    Object doApply(ExtendedThreadContext tc, int lhs, Object rhs);

    Object doApply(ExtendedThreadContext tc, long lhs, Object rhs);

    Object doApply(ExtendedThreadContext tc, float lhs, Object rhs);

    Object doApply(ExtendedThreadContext tc, double lhs, Object rhs);

    Object doApply(ExtendedThreadContext tc, BigInteger lhs, Object rhs);

    Object doApply(ExtendedThreadContext tc, BigDecimal lhs, Object rhs);
}
