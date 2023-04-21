package net.sf.jga.fn.property;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.sf.jga.fn.BinaryFunctor;
import net.sf.jga.fn.UnaryFunctor;
import net.sf.jga.fn.Generator;
import net.sf.jga.fn.adaptor.Identity;

/**
 * Static factory methods for the functors in the Property package.
 * <p>
 * Copyright &copy; 2006  David A. Hall
 * @author <a href="mailto:davidahall@users.sf.net">David A. Hall</a>
 */
public final class PropertyFunctors {

    /**
     */
    public static <T1, T2> BinaryFunctor<T1, T2, Object[]> arrayBinary() {
        return new ArrayBinary<T1, T2>();
    }

    /**
     */
    public static <T> UnaryFunctor<T, Object[]> arrayUnary() {
        return new ArrayUnary<T>();
    }

    /**
     */
    public static <T, R> UnaryFunctor<T, R> cast(Class<R> r) {
        return new Cast<T, R>(r);
    }

    /**
     */
    public static <T, V> UnaryFunctor<T, Boolean> compareProperty(Class<T> t, String name, V value) {
        return new CompareProperty<T, V>(t, name, value);
    }

    /**
     */
    public static <T, V> UnaryFunctor<T, Boolean> compareProperty(Class<T> t, String name, BinaryFunctor<V, V, Boolean> pred, V value) {
        return new CompareProperty<T, V>(t, name, pred, value);
    }

    /**
     */
    public static <R> UnaryFunctor<Object[], R> construct(Constructor<R> ctor) {
        return new Construct<R>(ctor);
    }

    /**
     */
    public static <R> UnaryFunctor<Object[], R> construct(Class<R> r, Class<?>... argclasses) {
        return new Construct<R>(r, argclasses);
    }

    /**
     */
    public static <R> Generator<R> constructDefault(Class<R> r) {
        return new ConstructDefault<R>(r);
    }

    /**
     */
    public static <T, R> UnaryFunctor<T, R> constructUnary(Class<R> r, Class<T> t) {
        return new ConstructUnary<T, R>(t, r);
    }

    /**
     */
    public static <T, R> UnaryFunctor<T, R> getField(Class<T> t, String name) {
        return new GetField<T, R>(t, name);
    }

    /**
     */
    public static <T, R> UnaryFunctor<T, R> getField(Class<T> t, Field field) {
        return new GetField<T, R>(t, field);
    }

    /**
     */
    public static <T, R> UnaryFunctor<T, R> getField(Field field) {
        return new GetField<T, R>(field);
    }

    /**
     */
    public static <T, R> UnaryFunctor<T, R> getProperty(Class<T> t, String name) {
        return new GetProperty<T, R>(t, name);
    }

    /**
     */
    public static <T, R> UnaryFunctor<T, R> getProperty(Class<T> t, String name, Class<R> hint) {
        return new GetProperty<T, R>(t, name);
    }

    /**
     */
    public static <T> UnaryFunctor<T, Boolean> instanceOf(Class<?> type) {
        return new InstanceOf<T>(type);
    }

    /**
     */
    public static <T1, T2, R> BinaryFunctor<T1, Object[], R> invokeMethod(Class<T1> t1, String name, Class<T2> t2) {
        return new InvokeMethod<T1, R>(t1, name, t2);
    }

    /**
     */
    public static <T1, T2, R> BinaryFunctor<T1, T2, R> invokeOneArgMethod(Class<T1> t1, String name, Class<T2> t2) {
        return new InvokeMethod<T1, R>(t1, name, t2).distribute(new Identity<T1>(), new ArrayUnary<T2>());
    }

    /**
     */
    public static <T, R> BinaryFunctor<T, Object[], R> invokeMethod(Method method) {
        return new InvokeMethod<T, R>(method);
    }

    /**
     */
    public static <T, R> BinaryFunctor<T, Object[], R> invokeMethod(Class<T> t, Method method) {
        return new InvokeMethod<T, R>(t, method);
    }

    /**
     */
    public static <T, R> BinaryFunctor<T, Object[], R> invokeMethod(Class<T> t, String name, Class<?>... argtypes) {
        return new InvokeMethod<T, R>(t, name, argtypes);
    }

    /**
     */
    public static <T, R> UnaryFunctor<T, R> invokeNoArgMethod(Class<T> t, Method method) {
        return new InvokeNoArgMethod<T, R>(t, method);
    }

    /**
     */
    public static <T, R> UnaryFunctor<T, R> invokeNoArgMethod(Method method) {
        return new InvokeNoArgMethod<T, R>(method);
    }

    /**
     */
    public static <T, R> UnaryFunctor<T, R> invokeNoArgMethod(Class<T> t, String name) {
        return new InvokeNoArgMethod<T, R>(t, name);
    }

    /**
     */
    public static <T1, T2> BinaryFunctor<T1, T2, T2> setField(Field field) {
        return new SetField<T1, T2>(field);
    }

    /**
     */
    public static <T1, T2> BinaryFunctor<T1, T2, T2> setField(Class<T1> t1, Field field) {
        return new SetField<T1, T2>(t1, field);
    }

    /**
     */
    public static <T1, T2> BinaryFunctor<T1, T2, T2> setField(Class<T1> t1, Field field, Class<T2> t2) {
        return new SetField<T1, T2>(t1, field, t2);
    }

    /**
     */
    public static <T1, T2> BinaryFunctor<T1, T2, T2> setField(Class<T1> t1, String name, Class<T2> t2) {
        return new SetField<T1, T2>(t1, name, t2);
    }

    /**
     */
    public static <T1, T2> BinaryFunctor<T1, T2, T2> setProperty(Class<T1> t1, String name, Class<T2> t2) {
        return new SetProperty<T1, T2>(t1, name, t2);
    }
}
