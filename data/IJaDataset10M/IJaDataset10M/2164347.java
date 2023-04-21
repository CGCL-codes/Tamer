package memops.general;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Util {

    public static final String BaseClass = "memops.api.Implementation.MemopsBaseClass";

    private static java.util.HashMap classMap = new java.util.HashMap();

    public static String getVarName(String tag) {
        return tag;
    }

    public static Class getClass(String className) throws ApiException {
        Class clazz = (Class) classMap.get(className);
        if (clazz == null) {
            try {
                clazz = Class.forName(className);
                classMap.put(className, clazz);
            } catch (ClassNotFoundException e) {
                throw new ApiException("Class '" + className + "' not found, check your CLASSPATH environment variable");
            }
        }
        return clazz;
    }

    public static Field getField(String className, String fieldName) throws ApiException {
        return getField(getClass(className), fieldName);
    }

    public static Field getField(Class clazz, String fieldName) throws ApiException {
        Field field = null;
        for (Class c = clazz; c != null; c = c.getSuperclass()) {
            try {
                field = c.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException e) {
            }
            if (c.getName().equals(BaseClass)) break;
        }
        if (field == null) throw new ApiException("Unknown field '" + fieldName + "' for class '" + clazz.getName() + "'");
        return field;
    }

    public static Object getFieldValue(Field field, Object object) throws ApiException {
        Object value = null;
        boolean isFieldAccessible = field.isAccessible();
        if (!isFieldAccessible) field.setAccessible(true);
        try {
            value = field.get(object);
        } catch (IllegalArgumentException e) {
            Throwable f = getExceptionCause(e);
            throw new ApiException("Illegal argument for field '" + field.getName() + "' in class '" + field.getDeclaringClass().getName() + "': " + f);
        } catch (IllegalAccessException e) {
            Throwable f = getExceptionCause(e);
            throw new ApiException("Illegal access for field '" + field.getName() + "' in class '" + field.getDeclaringClass().getName() + "': " + f);
        } finally {
            if (!isFieldAccessible) field.setAccessible(false);
        }
        return value;
    }

    public static void setFieldValue(Field field, Object object, Object value) throws ApiException {
        boolean isFieldAccessible = field.isAccessible();
        if (!isFieldAccessible) field.setAccessible(true);
        try {
            field.set(object, value);
        } catch (IllegalArgumentException e) {
            Throwable f = getExceptionCause(e);
            throw new ApiException("Illegal argument for field '" + field.getName() + "' in class '" + field.getDeclaringClass().getName() + "': " + f);
        } catch (IllegalAccessException e) {
            Throwable f = getExceptionCause(e);
            throw new ApiException("Illegal access for field '" + field.getName() + "' in class '" + field.getDeclaringClass().getName() + "': " + f);
        } finally {
            if (!isFieldAccessible) field.setAccessible(false);
        }
    }

    public static Method getMethod(String className, String methodName) throws ApiException {
        return getMethod(className, methodName, false);
    }

    public static Method getMethod(String className, String methodName, boolean getDeclared) throws ApiException {
        return getMethod(getClass(className), methodName, getDeclared);
    }

    public static Method getMethod(Class clazz, String methodName) throws ApiException {
        return getMethod(clazz, methodName, false);
    }

    public static Method getMethod(Class clazz, String methodName, boolean getDeclared) throws ApiException {
        Class[] parameterTypes = {};
        return getMethod(clazz, methodName, parameterTypes, getDeclared);
    }

    public static Method getMethod(Class clazz, String methodName, Class parameterType) throws ApiException {
        return getMethod(clazz, methodName, parameterType, false);
    }

    public static Method getMethod(Class clazz, String methodName, Class parameterType, boolean getDeclared) throws ApiException {
        Class[] parameterTypes = { parameterType };
        return getMethod(clazz, methodName, parameterTypes, getDeclared);
    }

    public static Method getMethod(Class clazz, String methodName, Class[] parameterTypes) throws ApiException {
        return getMethod(clazz, methodName, parameterTypes, false);
    }

    public static Method getMethod(Class clazz, String methodName, Class[] parameterTypes, boolean getDeclared) throws ApiException {
        Method method = null;
        for (Class c = clazz; c != null; c = c.getSuperclass()) {
            try {
                if (getDeclared) method = c.getDeclaredMethod(methodName, parameterTypes); else method = c.getMethod(methodName, parameterTypes);
                break;
            } catch (NoSuchMethodException e) {
            }
            if (c.getName().equals(BaseClass)) break;
        }
        if (method == null) {
            String s, t;
            if (parameterTypes.length == 0) {
                s = "no arguments";
            } else if (parameterTypes.length == 1) {
                s = "one argument of type '" + parameterTypes[0].getName() + "'";
            } else {
                s = "arguments of type ";
                for (int i = 0; i < parameterTypes.length; i++) {
                    if (i > 0) s = s + ", ";
                    s = s + "'" + parameterTypes[i].getName() + "'";
                }
            }
            if (getDeclared) t = "one"; else t = "public";
            throw new ApiException("Unknown method '" + methodName + "' for class '" + clazz.getName() + "': cound not find " + t + " with " + s);
        }
        return method;
    }

    public static Object invokeMethod(Method method) throws ApiException {
        Object object = null;
        return invokeMethod(method, object);
    }

    public static Object invokeMethod(Method method, Object object) throws ApiException {
        Object[] args = {};
        return invokeMethod(method, object, args);
    }

    public static Object invokeMethod(Method method, Object object, Object arg) throws ApiException {
        Object[] args = { arg };
        return invokeMethod(method, object, args);
    }

    public static Object invokeMethod(Method method, Object[] args) throws ApiException {
        return invokeMethod(method, null, args);
    }

    public static Object invokeMethod(Method method, Object object, Object[] args) throws ApiException {
        boolean isMethodAccessible = method.isAccessible();
        if (!isMethodAccessible) method.setAccessible(true);
        try {
            return method.invoke(object, args);
        } catch (InvocationTargetException e) {
            Throwable f = getExceptionCause(e.getTargetException());
            throw new ApiException("Illegal invocation for method '" + method.getName() + "' in class '" + method.getDeclaringClass().getName() + "': " + f);
        } catch (IllegalArgumentException e) {
            Throwable f = getExceptionCause(e);
            throw new ApiException("Illegal argument for method '" + method.getName() + "' in class '" + method.getDeclaringClass().getName() + "': " + f);
        } catch (IllegalAccessException e) {
            Throwable f = getExceptionCause(e);
            throw new ApiException("Illegal access for method '" + method.getName() + "' in class '" + method.getDeclaringClass().getName() + "': " + f);
        } finally {
            if (!isMethodAccessible) method.setAccessible(false);
        }
    }

    public static Constructor getConstructor(String className) throws ApiException {
        return getConstructor(className, false);
    }

    public static Constructor getConstructor(String className, boolean getDeclared) throws ApiException {
        return getConstructor(getClass(className), getDeclared);
    }

    public static Constructor getConstructor(Class clazz) throws ApiException {
        return getConstructor(clazz, false);
    }

    public static Constructor getConstructor(Class clazz, boolean getDeclared) throws ApiException {
        Class[] parameterTypes = {};
        return getConstructor(clazz, parameterTypes, getDeclared);
    }

    public static Constructor getConstructor(Class clazz, Class parameterType) throws ApiException {
        return getConstructor(clazz, parameterType, false);
    }

    public static Constructor getConstructor(Class clazz, Class parameterType, boolean getDeclared) throws ApiException {
        Class[] parameterTypes = { parameterType };
        return getConstructor(clazz, parameterTypes, getDeclared);
    }

    public static Constructor getConstructor(Class clazz, Class[] parameterTypes) throws ApiException {
        return getConstructor(clazz, parameterTypes, false);
    }

    public static Constructor getConstructor(Class clazz, Class[] parameterTypes, boolean getDeclared) throws ApiException {
        Constructor constructor = null;
        for (Class c = clazz; c != null; c = c.getSuperclass()) {
            try {
                if (getDeclared) constructor = c.getDeclaredConstructor(parameterTypes); else constructor = c.getConstructor(parameterTypes);
                break;
            } catch (NoSuchMethodException e) {
            }
            if (c.getName().equals(BaseClass)) break;
        }
        if (constructor == null) {
            String s, t;
            if (parameterTypes.length == 0) {
                s = "no arguments";
            } else if (parameterTypes.length == 1) {
                s = "one argument of type '" + parameterTypes[0].getName() + "'";
            } else {
                s = "arguments of type ";
                for (int i = 0; i < parameterTypes.length; i++) {
                    if (i > 0) s = s + ", ";
                    s = s + "'" + parameterTypes[i].getName() + "'";
                }
            }
            if (getDeclared) t = "one"; else t = "public";
            throw new ApiException("Unknown constructor for class '" + clazz.getName() + "': could not find " + t + " with " + s);
        }
        return constructor;
    }

    public static Object invokeConstructor(Constructor constructor) throws ApiException {
        Object[] args = {};
        return invokeConstructor(constructor, args);
    }

    public static Object invokeConstructor(Constructor constructor, Object arg) throws ApiException {
        Object[] args = { arg };
        return invokeConstructor(constructor, args);
    }

    public static Object invokeConstructor(Constructor constructor, Object[] args) throws ApiException {
        boolean isConstructorAccessible = constructor.isAccessible();
        if (!isConstructorAccessible) constructor.setAccessible(true);
        try {
            return constructor.newInstance(args);
        } catch (InvocationTargetException e) {
            Throwable f = getExceptionCause(e.getTargetException());
            throw new ApiException("Illegal invocation for constructor '" + constructor.getName() + "' in class '" + constructor.getDeclaringClass().getName() + "': " + f);
        } catch (IllegalArgumentException e) {
            Throwable f = getExceptionCause(e);
            throw new ApiException("Illegal argument for constructor '" + constructor.getName() + "' in class '" + constructor.getDeclaringClass().getName() + "': " + f);
        } catch (InstantiationException e) {
            Throwable f = getExceptionCause(e);
            throw new ApiException("Instantiation exception for constructor '" + constructor.getName() + "' in class '" + constructor.getDeclaringClass().getName() + "': " + f);
        } catch (IllegalAccessException e) {
            Throwable f = getExceptionCause(e);
            throw new ApiException("Illegal access for constructor '" + constructor.getName() + "' in class '" + constructor.getDeclaringClass().getName() + "': " + f);
        } finally {
            if (!isConstructorAccessible) constructor.setAccessible(false);
        }
    }

    private static Throwable getExceptionCause(Throwable e) {
        Throwable f = e.getCause();
        if (f == null) f = e;
        return f;
    }
}
