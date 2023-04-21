package uk.co.wilson.ng.runtime.metaclass;

import java.math.BigDecimal;
import java.math.BigInteger;
import ng.runtime.CompiledNgObject;
import ng.runtime.threadcontext.Callable;
import ng.runtime.threadcontext.ExtendedThreadContext;

/**
 * @author John
 * 
 */
public class CompiledNgObjectMethodContainer implements Callable {

    final int methodNumber;

    /**
   * @param methodNumber
   */
    public CompiledNgObjectMethodContainer(final int methodNumber) {
        this.methodNumber = methodNumber;
    }

    public Object doCall(final ExtendedThreadContext tc, final Object instance, final Object[] params) throws Throwable {
        return ((CompiledNgObject) instance).ng$Call(tc, this.methodNumber, params);
    }

    public Object doCallQuick(final ExtendedThreadContext tc, final Object instance) throws Throwable {
        return ((CompiledNgObject) instance).ng$Call(tc, this.methodNumber);
    }

    public Object doCallQuick(final ExtendedThreadContext tc, final Object instance, final boolean p1) throws Throwable {
        return ((CompiledNgObject) instance).ng$Call(tc, this.methodNumber, p1);
    }

    public Object doCallQuick(final ExtendedThreadContext tc, final Object instance, final char p1) throws Throwable {
        return ((CompiledNgObject) instance).ng$Call(tc, this.methodNumber, p1);
    }

    public Object doCallQuick(final ExtendedThreadContext tc, final Object instance, final byte p1) throws Throwable {
        return ((CompiledNgObject) instance).ng$Call(tc, this.methodNumber, p1);
    }

    public Object doCallQuick(final ExtendedThreadContext tc, final Object instance, final short p1) throws Throwable {
        return ((CompiledNgObject) instance).ng$Call(tc, this.methodNumber, p1);
    }

    public Object doCallQuick(final ExtendedThreadContext tc, final Object instance, final int p1) throws Throwable {
        return ((CompiledNgObject) instance).ng$Call(tc, this.methodNumber, p1);
    }

    public Object doCallQuick(final ExtendedThreadContext tc, final Object instance, final long p1) throws Throwable {
        return ((CompiledNgObject) instance).ng$Call(tc, this.methodNumber, p1);
    }

    public Object doCallQuick(final ExtendedThreadContext tc, final Object instance, final float p1) throws Throwable {
        return ((CompiledNgObject) instance).ng$Call(tc, this.methodNumber, p1);
    }

    public Object doCallQuick(final ExtendedThreadContext tc, final Object instance, final double p1) throws Throwable {
        return ((CompiledNgObject) instance).ng$Call(tc, this.methodNumber, p1);
    }

    public Object doCallQuick(final ExtendedThreadContext tc, final Object instance, final BigInteger p1) throws Throwable {
        return ((CompiledNgObject) instance).ng$Call(tc, this.methodNumber, p1);
    }

    public Object doCallQuick(final ExtendedThreadContext tc, final Object instance, final BigDecimal p1) throws Throwable {
        return ((CompiledNgObject) instance).ng$Call(tc, this.methodNumber, p1);
    }

    public Object doCallQuick(final ExtendedThreadContext tc, final Object instance, final String p1) throws Throwable {
        return ((CompiledNgObject) instance).ng$Call(tc, this.methodNumber, p1);
    }

    public Object doCallQuick(final ExtendedThreadContext tc, final Object instance, final Object p1) throws Throwable {
        return ((CompiledNgObject) instance).ng$Call(tc, this.methodNumber, p1);
    }

    public Object doCallQuick(final ExtendedThreadContext tc, final Object instance, final Object p1, final Object p2) throws Throwable {
        return ((CompiledNgObject) instance).ng$Call(tc, this.methodNumber, p1, p2);
    }

    public Object doCallQuick(final ExtendedThreadContext tc, final Object instance, final Object p1, final Object p2, final Object p3) throws Throwable {
        return ((CompiledNgObject) instance).ng$Call(tc, this.methodNumber, p1, p2, p3);
    }

    public Object doCallQuick(final ExtendedThreadContext tc, final Object instance, final Object p1, final Object p2, final Object p3, final Object p4) throws Throwable {
        return ((CompiledNgObject) instance).ng$Call(tc, this.methodNumber, p1, p2, p3, p4);
    }
}
