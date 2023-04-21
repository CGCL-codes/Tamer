package com.taliasplayground.lang.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import com.taliasplayground.lang.Assert;
import com.taliasplayground.lang.ClassUtils;

/**
 * @author David M. Sledge
 *
 */
public abstract class MethodUtils {

    /**
     * <p>
     * Return an accessible method (that is, one that can be invoked via
     * reflection) that the specified Method implements. If no such method can
     * be found, return <code>null</code>.
     * </p>
     * 
     * @param method
     *            The method that we wish to call
     * @return The accessible method
     */
    public static Invokable getAccessibleMethod(Method method) {
        if (!Modifier.isPublic(method.getModifiers())) {
            return null;
        }
        Class<?> cls = method.getDeclaringClass();
        if (AccessibleUtils.isAccessible(cls) && !method.isSynthetic()) {
            return new Invokable(method);
        }
        if (Modifier.isStatic(method.getModifiers())) {
            return null;
        }
        String name = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        while (true) {
            for (Class<?> intf : cls.getInterfaces()) {
                if (AccessibleUtils.isAccessible(intf)) {
                    try {
                        return new Invokable(intf.getMethod(name, parameterTypes));
                    } catch (NoSuchMethodException e) {
                    }
                }
            }
            cls = cls.getSuperclass();
            if (cls == null) {
                return null;
            }
            if (AccessibleUtils.isAccessible(cls)) {
                try {
                    method = cls.getMethod(name, parameterTypes);
                    if (Modifier.isPublic(method.getModifiers()) && !method.isSynthetic()) {
                        return new Invokable(method);
                    }
                } catch (NoSuchMethodException e) {
                }
            }
        }
    }

    public static Invokable getAccessibleMethod(Class<?> cls, String name, Class<?>... types) {
        try {
            Method method = cls.getMethod(name, types);
            return AccessibleUtils.isAccessible(method) ? new Invokable(method) : null;
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    /**
     * <p>
     * Return an accessible method (that is, one that can be invoked via
     * reflection) with given name and parameters. If no such method can be
     * found, return <code>null</code>. This is just a convenient wrapper for
     * {@link #getAccessibleMethod(Method method)}.
     * </p>
     * 
     * @param cls
     *            get method from this class
     * @param name
     *            get method with this name
     * @param parameterTypes
     *            with these parameters types
     * @return The accessible method
     */
    public static Invokable[] getAccessibleMethods(Class<?> cls, boolean mustBeStatic) {
        Method[] methods = cls.getMethods();
        Set<Invokable> accessMethods = new HashSet<Invokable>(methods.length);
        for (Method method : methods) {
            if (!mustBeStatic || Modifier.isStatic(method.getModifiers())) {
                Invokable invokable = getAccessibleMethod(method);
                if (invokable != null) {
                    accessMethods.add(invokable);
                }
            }
        }
        return accessMethods.toArray(new Invokable[accessMethods.size()]);
    }

    public static Invokable getOverridableMethod(Class<?> cls, String name, Class<?>... types) {
        if (!AccessibleUtils.isAccessible(cls) || Modifier.isFinal(cls.getModifiers())) {
            return null;
        }
        Method method;
        try {
            method = cls.getDeclaredMethod(name, types);
            if (!cls.isInterface()) {
                int mod = method.getModifiers();
                if (Modifier.isFinal(mod) || Modifier.isStatic(mod) || !Modifier.isPublic(mod) && !Modifier.isProtected(mod)) {
                    return null;
                }
            }
            if (!method.isSynthetic()) {
                return new Invokable(method);
            }
        } catch (NoSuchMethodException e) {
        }
        while (true) {
            for (Class<?> intf : cls.getInterfaces()) {
                try {
                    method = intf.getDeclaredMethod(name, types);
                    if (!method.isSynthetic()) {
                        return new Invokable(method);
                    }
                } catch (NoSuchMethodException e) {
                }
            }
            cls = cls.getSuperclass();
            if (cls == null) {
                return null;
            }
            try {
                method = cls.getDeclaredMethod(name, types);
                int mod = method.getModifiers();
                if (Modifier.isFinal(mod) || Modifier.isStatic(mod) || !Modifier.isPublic(mod) && !Modifier.isProtected(mod)) {
                    return null;
                }
                if (!method.isSynthetic()) {
                    return new Invokable(method);
                }
            } catch (NoSuchMethodException e) {
            }
        }
    }

    /**
     * <p>
     * Get the specified method that supports the supplied arguments and
     * package the arguments so that they can be passed into the invocation.
     * </p>
     * @param targetClass
     *     the class containing the method.
     * @param name
     *     the name of the method.
     * @param mustBeStatic
     *     indicates whether or not the method must be static.
     * @param args
     *     specifies the arguments that the method must support.
     * @param types
     * @return
     */
    public static Invokable getMatchingAccessibleMethod(Class<?> targetClass, String name, boolean mustBeStatic, Class<?>... types) {
        Assert.notNullArg(targetClass, "'targetClass' may not be null");
        Assert.notNullArg(name, "'name' may not be null");
        if (types == null) {
            types = new Class<?>[0];
        }
        Invokable invokable = null;
        boolean isDeprecated = true;
        CastDifference minCastDiff = null;
        Invokable[] candidates = MethodUtils.getAccessibleMethods(targetClass, mustBeStatic);
        for (Invokable candidate : candidates) {
            if (!candidate.getName().equals(name)) {
                continue;
            }
            boolean candidateIsDeprecated = candidate.isAnnotationPresent(Deprecated.class);
            Class<?>[] candidateTypes = candidate.getParameterTypes();
            if (!candidateIsDeprecated || invokable == null || isDeprecated) {
                CastDifference castDiff = TypeUtils.getCastDifferences(types, candidateTypes, candidate.isVarArgs(), isDeprecated && !candidateIsDeprecated ? null : minCastDiff);
                if (castDiff != null) {
                    minCastDiff = castDiff;
                    invokable = candidate;
                    isDeprecated = candidateIsDeprecated;
                }
            }
        }
        if (minCastDiff == null) {
            return null;
        }
        return invokable;
    }

    /**
     * <p>
     * Get the specified method that supports the supplied arguments and
     * package the arguments so that they can be passed into the invocation.
     * </p>
     * @param targetClass
     *     the class containing the method.
     * @param name
     *     the name of the method.
     * @param mustBeStatic
     *     indicates whether or not the method must be static.
     * @param args
     *     specifies the arguments that the method must support.
     * @param explicitTypes
     * @return
     */
    public static Invokable getMatchingAccessibleMethod(Class<?> targetClass, String name, boolean mustBeStatic, Object[] args, Class<?>[] explicitTypes) {
        Assert.notNullArg(targetClass, "'targetClass' may not be null");
        Assert.notNullArg(name, "'name' may not be null");
        if (args == null) {
            args = new Object[0];
        }
        if (explicitTypes == null) {
            explicitTypes = new Class<?>[0];
        }
        Invokable invokable = null;
        boolean isDeprecated = true;
        CastDifference minCastDiff = null;
        Invokable[] candidates = getAccessibleMethods(targetClass, mustBeStatic);
        for (Invokable candidate : candidates) {
            if (!candidate.getName().equals(name)) {
                continue;
            }
            boolean candidateIsDeprecated = candidate.isAnnotationPresent(Deprecated.class);
            Class<?>[] candidateTypes = candidate.getParameterTypes();
            int index = 0;
            for (Class<?> type : explicitTypes) {
                if (type != null && (index >= candidateTypes.length || !type.equals(candidateTypes[index]))) {
                    break;
                }
                index++;
            }
            if (index == explicitTypes.length && (!candidateIsDeprecated || invokable == null || isDeprecated)) {
                CastDifference castDiff = TypeUtils.getCastDifferencesOfObjects(args, candidateTypes, candidate.isVarArgs(), isDeprecated && !candidateIsDeprecated ? null : minCastDiff);
                if (castDiff != null) {
                    minCastDiff = castDiff;
                    invokable = candidate;
                    isDeprecated = candidateIsDeprecated;
                }
            }
        }
        if (minCastDiff == null) {
            return null;
        }
        return invokable;
    }

    /**
     * <p>
     * Get the specified method that supports the supplied arguments and
     * package the arguments so that they can be passed into the invocation.
     * </p>
     * @param targetClass
     *     the class containing the method.
     * @param name
     *     the name of the method.
     * @param mustBeStatic
     *     indicates whether or not the method must be static.
     * @param args
     *     specifies the arguments that the method must support.
     * @return
     */
    public static Invokable getMatchingAccessibleMethod(Class<?> targetClass, String name, boolean mustBeStatic, Object... args) {
        return getMatchingAccessibleMethod(targetClass, name, mustBeStatic, args, null);
    }

    /**
     * <p>
     * </p>
     * @param obj
     * @param name
     * @param types
     * @param args
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static Object invokeAccessibleMethod(Object obj, String name, Class<?>[] types, Object[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Invokable invokable = getAccessibleMethod(obj.getClass(), name, types);
        if (invokable == null) {
            StringBuilder strBuf = new StringBuilder("Could not find method named '").append(name).append("' in ").append(obj.getClass()).append(" to support the following arguments:  ").append(args).append("; of the following types:  ").append(types);
            throw new NoSuchMethodException(strBuf.toString());
        }
        try {
            return invokable.invoke(obj, args);
        } catch (InstantiationException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * <p>
     * </p>
     * @param object
     * @param methodName
     * @param args
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     */
    public static Object invokeAccessibleMethod(Object object, String name, Object[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Class<?>[] types = null;
        if (args != null) {
            Assert.noNullValuesArg(args, "args may not contain null values");
            types = new Class<?>[args.length];
            for (int i = 0; i < args.length; i++) {
                types[i] = args[i].getClass();
            }
        }
        return invokeAccessibleMethod(object, name, types, args);
    }

    /**
     * <p>
     * </p>
     * @param obj
     * @param name
     * @param types
     * @param args
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static Object invokeAccessibleMethod(Class<?> cls, String name, Class<?>[] types, Object[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Invokable invokable = getAccessibleMethod(cls, name, types);
        if (invokable == null) {
            StringBuilder strBuf = new StringBuilder("Could not find method named '").append(name).append("' in ").append(cls).append(" to support the following arguments:  ").append(args).append("; of the following types:  ").append(types);
            throw new NoSuchMethodException(strBuf.toString());
        }
        try {
            return invokable.invoke(null, args);
        } catch (InstantiationException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * <p>
     * </p>
     * @param cls
     * @param name
     * @param args
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     */
    public static Object invokeAccessibleMethod(Class<?> cls, String name, Object[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Class<?>[] types = null;
        if (args != null) {
            Assert.noNullValuesArg(args, "args may not contain null values");
            types = new Class<?>[args.length];
            for (int i = 0; i < args.length; i++) {
                types[i] = args[i].getClass();
            }
        }
        return invokeAccessibleMethod(cls, name, types, args);
    }

    /**
     * <p>
     * Find and invoke the method for the specified arguments.
     * </p>
     * @return the object (possibly <code>null</code>) returned by the method
     *     invocation, or <code>null</code> if the method has a void return type.
     * @throws InstantiationException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @see #getMatchingAccessibleMethod(Class, String, boolean, Object..., Class[])
     * @see Method#invoke(Object, Object...)
     */
    public static Object invokeMatchingAccessibleMethod(Object object, String name, Object[] args, Class<?>[] explicitTypes) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Invokable invokable = getMatchingAccessibleMethod(object.getClass(), name, false, args, explicitTypes);
        if (invokable == null) {
            StringBuilder strBuf = new StringBuilder("Could not find method named '").append(name).append("' in ").append(object.getClass()).append(" to support the following arguments:  ").append(args).append("; of the following types:  ").append(explicitTypes);
            throw new NoSuchMethodException(strBuf.toString());
        }
        try {
            return invokable.invoke(object, args);
        } catch (InstantiationException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * <p>
     * Find and invoke the method for the specified arguments.
     * </p>
     * @return the object (possibly <code>null</code>) returned by the method
     *     invocation, or <code>null</code> if the method has a void return type.
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @see #getMatchingAccessibleMethod(Class, String, boolean, Object..., Class[])
     * @see Method#invoke(Object, Object...)
     */
    public static Object invokeMatchingAccessibleMethod(Object obj, String name, Object... args) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return invokeMatchingAccessibleMethod(obj, name, args, null);
    }

    /**
     * <p>
     * Find and invoke the method for the specified arguments.
     * </p>
     * @param targetClass
     * @param name
     * @param args
     * @param explicitTypes
     * @return the object (possibly <code>null</code>) returned by the method
     *     invocation, or <code>null</code> if the method has a void return type.
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @see #getMatchingAccessibleMethod(Class, String, boolean, Object..., Class[])
     * @see Method#invoke(Object, Object...)
     */
    public static Object invokeMatchingAccessibleMethod(Class<?> targetClass, String name, Object[] args, Class<?>[] explicitTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Invokable invokable = getMatchingAccessibleMethod(targetClass, name, true, args, explicitTypes);
        if (invokable == null) {
            StringBuilder strBuf = new StringBuilder("Could not find static method named '").append(name).append("' in ").append(targetClass).append(" to support the following arguments:  ").append(Arrays.deepToString(args)).append("; of the following types:  ").append(Arrays.deepToString(explicitTypes));
            throw new NoSuchMethodException(strBuf.toString());
        }
        try {
            return invokable.invoke(null, args);
        } catch (InstantiationException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * <p>
     * Find and invoke the method for the specified arguments.
     * </p>
     * @param targetClass
     * @param name
     * @param args
     * @return the object (possibly <code>null</code>) returned by the method
     *     invocation, or <code>null</code> if the method has a void return type.
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @see #getMatchingAccessibleMethod(Class, String, boolean, Object..., Class[])
     * @see Method#invoke(Object, Object...)
     */
    public static Object invokeMatchingAccessibleMethod(Class<?> targetClass, String name, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return invokeMatchingAccessibleMethod(targetClass, name, args, null);
    }

    /**
     * <p>
     * Find and invoke the method for the specified arguments.
     * </p>
     * @param qualifiedStaticMethodName
     * @param args
     * @param explicitTypes
     * @return the object (possibly <code>null</code>) returned by the method
     *     invocation, or <code>null</code> if the method has a void return type.
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @see #getMatchingAccessibleMethod(Class, String, boolean, Object..., Class[])
     * @see Method#invoke(Object, Object...)
     */
    public static Object invokeMatchingAccessibleMethod(String qualifiedStaticMethodName, Object[] args, Class<?>[] explicitTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        int lastDotIndex = qualifiedStaticMethodName.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == qualifiedStaticMethodName.length()) {
            throw new IllegalArgumentException("'qualifiedStaticMethodName'" + " must be a fully qualified class plus method name: e.g." + " 'example.MyExampleClass.myExampleMethod'");
        }
        return invokeMatchingAccessibleMethod(ClassUtils.getClass(MethodUtils.class, qualifiedStaticMethodName.substring(0, lastDotIndex)), qualifiedStaticMethodName.substring(lastDotIndex + 1), args, explicitTypes);
    }

    /**
     * <p>
     * Find and invoke the method for the specified arguments.
     * </p>
     * @param qualifiedStaticMethodName
     * @param args
     * @return the object (possibly <code>null</code>) returned by the method
     *     invocation, or <code>null</code> if the method has a void return type.
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @see #getMatchingAccessibleMethod(Class, String, boolean, Object..., Class[])
     * @see Method#invoke(Object, Object...)
     */
    public static Object invokeMatchingAccessibleMethod(String qualifiedStaticMethodName, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        int lastDotIndex = qualifiedStaticMethodName.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == qualifiedStaticMethodName.length()) {
            throw new IllegalArgumentException("'qualifiedStaticMethodName'" + " must be a fully qualified class plus method name: e.g." + " 'example.MyExampleClass.myExampleMethod'");
        }
        return invokeMatchingAccessibleMethod(ClassUtils.getClass(MethodUtils.class, qualifiedStaticMethodName.substring(0, lastDotIndex)), qualifiedStaticMethodName.substring(lastDotIndex + 1), args);
    }
}
