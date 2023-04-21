package com.taliasplayground.lang.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import com.taliasplayground.lang.ArrayUtils;
import com.taliasplayground.lang.Assert;

/**
 * <p>
 * <code>Invokable</code> provides information about, and access to, a single
 * invokable (defined as either a constructor or a method) for a class.
 * Moreover, this class is an opaque wrapper for either a {@link Constructor}
 * object or a {@link Method} object. It exposes the methods
 * <code>Constructor</code> and <code>Method</code> have in common, and contains
 * proxy methods for other similar aspects between <code>Method</code>s and
 * <code>Constructor</code>s. Additionally, {@link #isMethod()} is supplied to
 * specify what the underlying object is, and {@link #getRawInvokable()} returns
 * the underlying <code>Method</code> or <code>Constructor</code>.
 * </p>
 * 
 * @author David M. Sledge
 * @since 3.0
 * @version $Id$
 */
public final class Invokable {

    /** contained Method for this Invokable */
    private final Method method;

    /** contained Constructor for this Invokable */
    private final Constructor<?> ctor;

    private final boolean needsEnclosingInstance;

    private final Class<?>[] parameterTypes;

    private final Type[] genericParameterTypes;

    private final Class<?> invokedFromObjectType;

    /**
     * <p>
     * Constructor for wrapping a {@link Constructor} in an Invokable.
     * </p>
     * 
     * @param ctor
     *            <code>Constructor</code> to envelop
     * @throws NullPointerException
     *             if <code>ctor</code> is <code>null</code>
     */
    public Invokable(Constructor<?> ctor) {
        Assert.notNullArg(ctor, "method may not be null");
        this.ctor = ctor;
        method = null;
        Class<?> declaringClass = ctor.getDeclaringClass();
        needsEnclosingInstance = declaringClass.getEnclosingClass() != null && !Modifier.isStatic(declaringClass.getModifiers());
        if (needsEnclosingInstance) {
            Class<?>[] types = ctor.getParameterTypes();
            parameterTypes = Arrays.copyOfRange(types, 1, types.length);
            invokedFromObjectType = types[0];
            Type[] genericTypes = ctor.getGenericParameterTypes();
            genericParameterTypes = Arrays.copyOfRange(genericTypes, 1, genericTypes.length);
        } else {
            parameterTypes = ctor.getParameterTypes();
            genericParameterTypes = ctor.getGenericParameterTypes();
            invokedFromObjectType = null;
        }
    }

    /**
     * <p>
     * Constructor for wrapping a {@link Method} in an Invokable.
     * </p>
     * 
     * @param method
     *            <code>Method</code> to envelop
     * @throws NullPointerException
     *             if <code>method</code> is <code>null</code>
     */
    public Invokable(Method method) {
        Assert.notNullArg(method, "method may not be null");
        ctor = null;
        this.method = method;
        needsEnclosingInstance = false;
        parameterTypes = method.getParameterTypes();
        genericParameterTypes = method.getGenericParameterTypes();
        invokedFromObjectType = Modifier.isStatic(method.getModifiers()) ? null : method.getDeclaringClass();
    }

    /**
     * Returns <code>true</code> if this is a wrapper for a {@link Method} and
     * <code>false</code> for a {@link Constructor}
     */
    public boolean isMethod() {
        return ctor == null;
    }

    /**
     * @return the underlying {@link Constructor} or {@link Method} object
     *         enveloped by this <code>Invokable</code> object.
     */
    public AccessibleObject getRawInvokable() {
        return ctor == null ? method : ctor;
    }

    /**
     * <p>
     * Returns an array of {@link Class} objects that represent the formal
     * parameter types, in declaration order, of the invokable represented by
     * this <code>Invokable</code> object. Returns an array of length 0 if the
     * invokable takes no parameters.
     * </p>
     * 
     * <p>
     * If the underlying object represents a constructor whose declaring class
     * is an inner class in a non-static context, unlike
     * {@link Constructor#getParameterTypes()}, the returned array will only
     * include the parameters explicitly specified in the source code. The
     * <code>Class</code> object representing the enclosing type will not be
     * included as the first argument, unless specified in the source code.
     * </p>
     * 
     * @return an array of <code>Class</code>es that represent the formal
     *         parameter types of the underlying method, in declaration order
     */
    public Class<?>[] getParameterTypes() {
        return parameterTypes.clone();
    }

    /**
     * <p>
     * Returns an array of {@link Type} objects that represent the formal
     * parameter types, in declaration order, of the invokable represented by
     * this <code>Invokable</code> object. Returns an array of length 0 if the
     * invokable takes no parameters.
     * </p>
     * 
     * <p>
     * If the underlying object represents a constructor whose declaring class
     * is an inner class in a non-static context, unlike
     * {@link Constructor#getGenericParameterTypes()}, the returned array will
     * only include the parameters explicitly specified in the source code. The
     * <code>Type</code> object representing the enclosing type will not be
     * included as the first argument, unless specified in the source code.
     * </p>
     * 
     * @return an array of <code>Type</code>s that represent the formal
     *         parameter types of the underlying method, in declaration order
     */
    public Type[] getGenericParameterTypes() {
        return genericParameterTypes.clone();
    }

    /**
     * <p>
     * Calls and returns the result of {@link Method#getParameterAnnotations()}
     * if the underlying object is a {@link Method}, and
     * {@link Constructor#getParameterAnnotations()} if the underlying object is
     * a {@link Constructor}.
     * </p>
     * 
     * @return an array of arrays that represent the annotations on the formal
     *         parameters, in declaration order, of the invokable represented by
     *         this <code>Invokable</code> object
     */
    public Annotation[][] getParameterAnnotations() {
        return ctor == null ? method.getParameterAnnotations() : ctor.getParameterAnnotations();
    }

    /**
     * <p>
     * Calls and returns the result of {@link Method#getParameterAnnotations()}
     * if the underlying object is a {@link Method}, and
     * {@link Constructor#getParameterAnnotations()} if the underlying object is
     * a {@link Constructor}.
     * </p>
     * 
     * @return an array of arrays that represent the annotations on the formal
     *         parameters, in declaration order, of the invokable represented by
     *         this <code>Invokable</code> object
     */
    public Class<?> getReturnType() {
        return ctor == null ? method.getReturnType() : ctor.getDeclaringClass();
    }

    /**
     * <p>
     * Calls and returns the result of {@link Method#getExceptionTypes()} if the
     * underlying object is a {@link Method}, and
     * {@link Constructor#getExceptionTypes()} if the underlying object is a
     * {@link Constructor}.
     * </p>
     * 
     * @return the exception types declared as being thrown by the invokable
     *         this object represents
     */
    public Class<?>[] getExceptionTypes() {
        return ctor == null ? method.getExceptionTypes() : ctor.getExceptionTypes();
    }

    /**
     * <p>
     * Calls and returns the result of {@link Method#getGenericExceptionTypes()}
     * if the underlying object is a {@link Method}, and
     * {@link Constructor#getGenericExceptionTypes()} if the underlying object
     * is a {@link Constructor}.
     * </p>
     * 
     * @return an array of Types that represent the exception types thrown by
     *         the underlying invokable
     */
    public Type[] getGenericExceptionTypes() {
        return ctor == null ? method.getGenericExceptionTypes() : ctor.getGenericExceptionTypes();
    }

    /**
     * <p>
     * Returns the {@link Class} object representing the class of the instance
     * from which this invokable must be invoked.
     * 
     * <p>
     * If the underlying object represents a non-static method, the result of
     * {@link Method#getDeclaringClass()} is returned. If the underlying object
     * represents a constructor whose declaring class is an inner class in a
     * non-static context. The first {@link Class} object in the array returned
     * by {@link Constructor#getParameterTypes()} is returned. <code>null</code>
     * is returned in all other cases.
     * </p>
     * 
     * @return
     */
    public Class<?> getInvokedFromObjectType() {
        return invokedFromObjectType;
    }

    /**
     * <p>
     * Invokes the underlying method or constructor.
     * </p>
     * 
     * @param object
     *            the object to invoke the method on if the underlying object is
     *            a {@link Method}. If the underlying object is a
     *            {@link Constructor} representing the constructor of a
     *            non-static inner class, then this parameter is an enclosing
     *            instance of the declaring class, and is prepended to the
     *            arguments array.
     * @param args
     *            the arguments to pass into the method or constructor
     * @return the result if this invocation
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    public Object invoke(Object object, Object... args) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (isVarArgs()) {
            Class<?>[] types = getParameterTypes();
            int varArgsPos = types.length - 1;
            if (types.length != args.length || !TypeUtils.isInstance(args[varArgsPos], types[varArgsPos])) {
                Object[] packagedArgs = new Object[types.length];
                System.arraycopy(args, 0, packagedArgs, 0, varArgsPos);
                Type componentType = TypeUtils.getComponentType(types[varArgsPos]);
                int varArgsCount = args.length - varArgsPos;
                Object array = Array.newInstance((Class<?>) componentType, varArgsCount);
                ArrayUtils.copyArray(args, varArgsPos, array, 0, varArgsCount);
                packagedArgs[varArgsPos] = array;
                args = packagedArgs;
            }
        }
        if (ctor == null) {
            return method.invoke(object, args);
        }
        if (needsEnclosingInstance) {
            Object[] moreArgs = new Object[args.length + 1];
            moreArgs[0] = object;
            System.arraycopy(args, 0, moreArgs, 1, args.length);
            args = moreArgs;
        }
        return ctor.newInstance(args);
    }

    /**
     * <p>
     * Returns <code>true</code> if this method was declared to take a variable
     * number of arguments; returns <code>false</code> otherwise.
     * </p>
     * 
     * @return <code>true</code> if and only if this method was declared to take
     *         a variable number of arguments.
     */
    public boolean isVarArgs() {
        return ctor == null ? method.isVarArgs() : ctor.isVarArgs();
    }

    /**
     * <p>
     * Calls and returns the result of {@link Method#toGenericString()} if the
     * underlying object is a {@link Method}, and
     * {@link Constructor#toGenericString()} if the underlying object is a
     * {@link Constructor}.
     * </p>
     * 
     * @return a string describing this invokable, include type parameters
     */
    public String toGenericString() {
        return ctor == null ? method.toGenericString() : ctor.toGenericString();
    }

    /**
     * <p>
     * Calls and returns the result of {@link Method#getDeclaringClass()} if the
     * underlying object is a {@link Method}, and
     * {@link Constructor#getDeclaringClass()} if the underlying object is a
     * {@link Constructor}.
     * </p>
     * 
     * @return an object representing the declaring class of the underlying
     *         member
     */
    public Class<?> getDeclaringClass() {
        return ctor == null ? method.getDeclaringClass() : ctor.getDeclaringClass();
    }

    /**
     * <p>
     * Calls and returns the result of {@link Method#getName()} if the
     * underlying object is a {@link Method}, and {@link Constructor#getName()}
     * if the underlying object is a {@link Constructor}.
     * </p>
     * 
     * @return the simple name of the underlying invokable
     */
    public String getName() {
        return ctor == null ? method.getName() : ctor.getName();
    }

    /**
     * <p>
     * Calls and returns the result of {@link Method#getModifiers()} if the
     * underlying object is a {@link Method}, and
     * {@link Constructor#getModifiers()} if the underlying object is a
     * {@link Constructor}.
     * </p>
     * 
     * @return the Java language modifiers for the underlying invokable
     */
    public int getModifiers() {
        return ctor == null ? method.getModifiers() : ctor.getModifiers();
    }

    /**
     * <p>
     * Returns <code>true</code> if this object's invokable is a synthetic
     * invokable; returns <code>false</code> otherwise.
     * </p>
     * 
     * @return <code>true</code> if and only if this method is a synthetic
     *         method as defined by the Java Language Specification.
     */
    public boolean isSynthetic() {
        return ctor == null ? method.isSynthetic() : ctor.isSynthetic();
    }

    /**
     * <p>
     * Calls and returns the result of {@link Method#getGenericReturnType()} if
     * the underlying object is a {@link Method}, and
     * {@link Constructor#getDeclaringClass()} if the underlying object is a
     * {@link Constructor}.
     * </p>
     * 
     * @return a {@link Type} object that represents the formal return type of
     *         the underlying method or the declaring class of the underlying
     *         constructor
     */
    public TypeVariable<?>[] getTypeParameters() {
        return ctor == null ? method.getTypeParameters() : ctor.getTypeParameters();
    }

    public void setAccessible(boolean flag) {
        if (ctor == null) {
            method.setAccessible(flag);
        } else {
            ctor.setAccessible(flag);
        }
    }

    public boolean isAccessible() {
        return ctor == null ? method.isAccessible() : ctor.isAccessible();
    }

    /**
     * <p>
     * Calls and returns the result of
     * {@link Method#getAnnotation(annotationClass)} if the underlying object is
     * a {@link Method}, and {@link Constructor#getAnnotation(annotationClass)}
     * if the underlying object is a {@link Constructor}.
     * </p>
     * 
     * @param annotationClass
     *            the {@link Class} object corresponding to the annotation type
     * @return the underlying object's annotation for the specified annotation
     *         type if present on this element, else <code>null</code>
     * @throws NullPointerException
     *             if the given annotation class is <code>null</code>
     */
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return ctor == null ? method.getAnnotation(annotationClass) : ctor.getAnnotation(annotationClass);
    }

    public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
        return ctor == null ? method.isAnnotationPresent(annotationClass) : ctor.isAnnotationPresent(annotationClass);
    }

    /**
     * <p>
     * Calls and returns the result of {@link Method#getAnnotations()} if the
     * underlying object is a {@link Method}, and
     * {@link Constructor#getAnnotations()} if the underlying object is a
     * {@link Constructor}.
     * </p>
     * 
     * @return All annotations present on the underlying object.
     */
    public Annotation[] getAnnotations() {
        return ctor == null ? method.getAnnotations() : ctor.getAnnotations();
    }

    /**
     * <p>
     * Calls and returns the result of {@link Method#getDeclaredAnnotations()}
     * if the underlying object is a {@link Method}, and
     * {@link Constructor#getDeclaredAnnotations()} if the underlying object is
     * a {@link Constructor}.
     * </p>
     * 
     * @return All annotations directly present on the underlying object.
     */
    public Annotation[] getDeclaredAnnotations() {
        return ctor == null ? method.getDeclaredAnnotations() : ctor.getDeclaredAnnotations();
    }

    /**
     * <p>
     * Returns true if and only if <code>obj</code> is an <code>Invokable</code>
     * instance, and its underlying {@link Method} or {@link Constructor} equals
     * this <code>Invokable</code>'s <code>Method</code> or
     * <code>Constructor</code>.
     * </p>
     * 
     * @param obj
     *            the reference object with which to compare
     */
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Invokable)) {
            return false;
        }
        Invokable invokable = (Invokable) obj;
        return ctor == null ? method.equals(invokable.method) : ctor.equals(invokable.ctor);
    }

    public int hashCode() {
        return ctor == null ? method.hashCode() : ctor.hashCode();
    }

    /**
     * <p>
     * Calls and returns the result of {@link Method#toString()} if the
     * underlying object is a {@link Method}, and {@link Constructor#toString()}
     * if the underlying object is a {@link Constructor}.
     * </p>
     * 
     * @return a string representation of the object.
     */
    public String toString() {
        return ctor == null ? method.toString() : ctor.toString();
    }
}
