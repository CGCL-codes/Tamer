package net.sourceforge.freejava.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Nullables {

    /**
     * @return <code>true</code> if both a and b are <code>null</code>, or <code>a.equals(b)</code>
     */
    public static boolean equals(Object a, Object b) {
        if (a == null || b == null) return a == b;
        return a.equals(b);
    }

    /**
     * @return <code>true</code> if both a and b are <code>null</code>, or <code>a.equals(b)</code>
     */
    public static boolean equalsIgnoreCase(String a, String b) {
        if (a == null || b == null) return a == b;
        return a.equalsIgnoreCase(b);
    }

    /**
     * @return <code>null</code> if <code>o</code> is <code>null</code>, or the return value of
     *         {@link Object#toString()}
     */
    public static String toString(Object o) {
        if (o == null) return null;
        return o.toString();
    }

    /**
     * @return 0 if <code>o</code> is <code>null</code>, or the return value of
     *         {@link Object#hashCode()}.
     */
    public static int hashCode(Object o) {
        if (o == null) return 0;
        return o.hashCode();
    }

    public static Class<?> getClass(Object o) {
        if (o == null) return null;
        return o.getClass();
    }

    public static Object clone(Cloneable o) {
        if (o == null) return null;
        try {
            Method cloneMethod = o.getClass().getMethod("clone");
            Object cloned = cloneMethod.invoke(cloneMethod, o);
            return cloned;
        } catch (Exception e) {
            throw new RuntimeException("Can't clone the object", e);
        }
    }

    static Object _invokeValue(Object o) throws NoSuchMethodException {
        try {
            Method valuef = o.getClass().getMethod("value");
            Object value = valuef.invoke(valuef, o);
            return value;
        } catch (NoSuchMethodException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Unexpected: value() method should not throw exception", e);
        }
    }

    /**
     * @return null if <code>annotation</code> is <code>null</code>.
     * @throws NullPointerException
     *             If <code>valueClass</code> is <code>null</code>.
     * @throws NoSuchMethodException
     *             If <code>value()</code> method doesn't exist in the annotation declaration.
     */
    public static <T> T getAnnotationValue(Annotation annotation, Class<T> valueClass) throws NoSuchMethodException {
        if (valueClass == null) throw new NullPointerException("valueClass");
        if (annotation == null) return null;
        Object value = _invokeValue(annotation);
        assert value != null : "null annotation value";
        return valueClass.cast(value);
    }

    /**
     * @return <code>defaultValue</code> if <code>annotation</code> is <code>null</code>.
     * @throws NullPointerException
     *             If <code>valueClass</code> is <code>null</code>.
     * @throws NoSuchMethodException
     *             If <code>value()</code> method doesn't exist in the annotation declaration.
     */
    public static <T> T getAnnotationValue(Annotation annotation, T defaultValue) throws NoSuchMethodException {
        if (defaultValue == null) throw new NullPointerException("defaultValue");
        @SuppressWarnings("unchecked") Class<T> valueClass = (Class<T>) defaultValue.getClass();
        return getAnnotationValue(annotation, valueClass);
    }

    /**
     * @return null if <code>annotation</code> is <code>null</code>.
     * @throws NoSuchMethodException
     *             If <code>value()</code> method doesn't exist in the annotation declaration.
     */
    public static Object getAnnotationValue(Annotation annotation) throws NoSuchMethodException {
        if (annotation == null) return null;
        Object value = _invokeValue(annotation);
        assert value != null : "null annotation value";
        return value;
    }

    private static class _NullInvocationHandler implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return null;
        }
    }

    ;

    static final _NullInvocationHandler _nih = new _NullInvocationHandler();

    /**
     * @return The existing annotation or a null-proxy which returns <code>null</code> for all
     *         annotation fields.
     */
    public static <A extends Annotation> A getAnnotation(AnnotatedElement annotatedElement, Class<A> annotationClass) {
        A annotation = annotatedElement.getAnnotation(annotationClass);
        if (annotation == null) {
            ClassLoader loader = annotationClass.getClassLoader();
            Object proxyInstance = Proxy.newProxyInstance(loader, new Class<?>[] { annotationClass }, _nih);
            annotation = annotationClass.cast(proxyInstance);
        }
        return annotation;
    }
}
