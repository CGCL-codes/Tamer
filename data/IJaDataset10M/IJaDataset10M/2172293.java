package com.jogamp.common.util;

import java.lang.reflect.*;
import com.jogamp.common.JogampRuntimeException;
import jogamp.common.Debug;

public final class ReflectionUtil {

    public static final boolean DEBUG = Debug.debug("ReflectionUtil");

    private static final Class[] zeroTypes = new Class[0];

    /**
     * Returns true only if the class could be loaded.
     */
    public static final boolean isClassAvailable(String clazzName, ClassLoader cl) {
        try {
            return null != Class.forName(clazzName, false, cl);
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * Loads and returns the class or null.
     * @see Class#forName(java.lang.String, boolean, java.lang.ClassLoader)
     */
    public static final Class getClass(String clazzName, boolean initialize, ClassLoader cl) throws JogampRuntimeException {
        try {
            return getClassImpl(clazzName, initialize, cl);
        } catch (ClassNotFoundException e) {
            throw new JogampRuntimeException(clazzName + " not available", e);
        }
    }

    private static Class getClassImpl(String clazzName, boolean initialize, ClassLoader cl) throws ClassNotFoundException {
        return Class.forName(clazzName, initialize, cl);
    }

    /**
     * @throws JogampRuntimeException if the constructor can not be delivered.
     */
    public static final Constructor getConstructor(String clazzName, Class[] cstrArgTypes, ClassLoader cl) throws JogampRuntimeException {
        try {
            return getConstructor(getClassImpl(clazzName, true, cl), cstrArgTypes);
        } catch (ClassNotFoundException ex) {
            throw new JogampRuntimeException(clazzName + " not available", ex);
        }
    }

    static final String asString(Class[] argTypes) {
        StringBuffer args = new StringBuffer();
        boolean coma = false;
        if (null != argTypes) {
            for (int i = 0; i < argTypes.length; i++) {
                if (coma) {
                    args.append(", ");
                }
                args.append(argTypes[i].getName());
                coma = true;
            }
        }
        return args.toString();
    }

    /**
     * @throws JogampRuntimeException if the constructor can not be delivered.
     */
    public static final Constructor getConstructor(Class clazz, Class... cstrArgTypes) throws JogampRuntimeException {
        try {
            if (null == cstrArgTypes) {
                cstrArgTypes = zeroTypes;
            }
            return clazz.getDeclaredConstructor(cstrArgTypes);
        } catch (NoSuchMethodException ex) {
            throw new JogampRuntimeException("Constructor: '" + clazz + "(" + asString(cstrArgTypes) + ")' not found", ex);
        }
    }

    public static final Constructor getConstructor(String clazzName, ClassLoader cl) throws JogampRuntimeException {
        return getConstructor(clazzName, null, cl);
    }

    /**
   * @throws JogampRuntimeException if the instance can not be created.
   */
    public static final Object createInstance(Constructor cstr, Object... cstrArgs) throws JogampRuntimeException, RuntimeException {
        try {
            return cstr.newInstance(cstrArgs);
        } catch (Exception e) {
            Throwable t = e;
            if (t instanceof InvocationTargetException) {
                t = ((InvocationTargetException) t).getTargetException();
            }
            if (t instanceof Error) {
                throw (Error) t;
            }
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            }
            throw new JogampRuntimeException("can not create instance of " + cstr.getName(), t);
        }
    }

    /**
   * @throws JogampRuntimeException if the instance can not be created.
   */
    public static final Object createInstance(Class clazz, Class[] cstrArgTypes, Object... cstrArgs) throws JogampRuntimeException, RuntimeException {
        return createInstance(getConstructor(clazz, cstrArgTypes), cstrArgs);
    }

    public static final Object createInstance(Class clazz, Object... cstrArgs) throws JogampRuntimeException, RuntimeException {
        Class[] cstrArgTypes = null;
        if (null != cstrArgs) {
            cstrArgTypes = new Class[cstrArgs.length];
            for (int i = 0; i < cstrArgs.length; i++) {
                cstrArgTypes[i] = cstrArgs[i].getClass();
            }
        }
        return createInstance(clazz, cstrArgTypes, cstrArgs);
    }

    public static final Object createInstance(String clazzName, Class[] cstrArgTypes, Object[] cstrArgs, ClassLoader cl) throws JogampRuntimeException, RuntimeException {
        try {
            return createInstance(getClassImpl(clazzName, true, cl), cstrArgTypes, cstrArgs);
        } catch (ClassNotFoundException ex) {
            throw new JogampRuntimeException(clazzName + " not available", ex);
        }
    }

    public static final Object createInstance(String clazzName, Object[] cstrArgs, ClassLoader cl) throws JogampRuntimeException, RuntimeException {
        Class[] cstrArgTypes = null;
        if (null != cstrArgs) {
            cstrArgTypes = new Class[cstrArgs.length];
            for (int i = 0; i < cstrArgs.length; i++) {
                cstrArgTypes[i] = cstrArgs[i].getClass();
            }
        }
        return createInstance(clazzName, cstrArgTypes, cstrArgs, cl);
    }

    public static final Object createInstance(String clazzName, ClassLoader cl) throws JogampRuntimeException, RuntimeException {
        return createInstance(clazzName, null, null, cl);
    }

    public static final boolean instanceOf(Object obj, String clazzName) {
        return instanceOf(obj.getClass(), clazzName);
    }

    public static final boolean instanceOf(Class clazz, String clazzName) {
        do {
            if (clazz.getName().equals(clazzName)) {
                return true;
            }
            clazz = clazz.getSuperclass();
        } while (clazz != null);
        return false;
    }

    public static final boolean implementationOf(Object obj, String faceName) {
        return implementationOf(obj.getClass(), faceName);
    }

    public static final boolean implementationOf(Class clazz, String faceName) {
        do {
            Class[] clazzes = clazz.getInterfaces();
            for (int i = clazzes.length - 1; i >= 0; i--) {
                Class face = clazzes[i];
                if (face.getName().equals(faceName)) {
                    return true;
                }
            }
            clazz = clazz.getSuperclass();
        } while (clazz != null);
        return false;
    }

    public static boolean isAWTComponent(Object target) {
        return instanceOf(target, "java.awt.Component");
    }

    public static boolean isAWTComponent(Class clazz) {
        return instanceOf(clazz, "java.awt.Component");
    }

    /**
   * @throws JogampRuntimeException if the Method can not be found.
   */
    public static final Method getMethod(Class clazz, String methodName, Class... argTypes) throws JogampRuntimeException, RuntimeException {
        try {
            return clazz.getDeclaredMethod(methodName, argTypes);
        } catch (NoSuchMethodException ex) {
            throw new JogampRuntimeException("Method: '" + clazz + "." + methodName + "(" + asString(argTypes) + ")' not found", ex);
        }
    }

    /**
   * @throws JogampRuntimeException if the Method can not be found.
   */
    public static final Method getMethod(String clazzName, String methodName, Class[] argTypes, ClassLoader cl) throws JogampRuntimeException, RuntimeException {
        try {
            return getMethod(getClassImpl(clazzName, true, cl), methodName, argTypes);
        } catch (ClassNotFoundException ex) {
            throw new JogampRuntimeException(clazzName + " not available", ex);
        }
    }

    /**
   * @param instance may be null in case of a static method
   * @param method the method to be called
   * @param args the method arguments
   * @return the methods result, maybe null if void
   * @throws JogampRuntimeException if call fails
   * @throws RuntimeException if call fails
   */
    public static final Object callMethod(Object instance, Method method, Object... args) throws JogampRuntimeException, RuntimeException {
        try {
            return method.invoke(instance, args);
        } catch (Exception e) {
            Throwable t = e;
            if (t instanceof InvocationTargetException) {
                t = ((InvocationTargetException) t).getTargetException();
            }
            if (t instanceof Error) {
                throw (Error) t;
            }
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            }
            throw new JogampRuntimeException("calling " + method + " failed", t);
        }
    }

    /**
   * @throws JogampRuntimeException if the instance can not be created.
   */
    public static final Object callStaticMethod(String clazzName, String methodName, Class[] argTypes, Object[] args, ClassLoader cl) throws JogampRuntimeException, RuntimeException {
        return callMethod(null, getMethod(clazzName, methodName, argTypes, cl), args);
    }
}
