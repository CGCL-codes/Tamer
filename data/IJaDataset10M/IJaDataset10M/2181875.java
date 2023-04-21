package org.jboss.fastjaxb.util;

import java.lang.reflect.*;
import java.util.Collection;

/**
 * Type conversions and generic type manipulations
 *
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class Types {

    public static Class getTemplateParameterOfInterface(Class base, Class desiredInterface) {
        Object rtn = getSomething(base, desiredInterface);
        if (rtn != null && rtn instanceof Class) return (Class) rtn;
        return null;
    }

    private static Object getSomething(Class base, Class desiredInterface) {
        for (int i = 0; i < base.getInterfaces().length; i++) {
            Class intf = base.getInterfaces()[i];
            if (intf.equals(desiredInterface)) {
                Type generic = base.getGenericInterfaces()[i];
                if (generic instanceof ParameterizedType) {
                    ParameterizedType p = (ParameterizedType) generic;
                    Type type = p.getActualTypeArguments()[0];
                    Class rtn = getRawTypeNoException(type);
                    if (rtn != null) return rtn;
                    return type;
                } else {
                    return null;
                }
            }
        }
        if (base.getSuperclass() == null || base.getSuperclass().equals(Object.class)) return null;
        Object rtn = getSomething(base.getSuperclass(), desiredInterface);
        if (rtn == null || rtn instanceof Class) return rtn;
        if (!(rtn instanceof TypeVariable)) return null;
        String name = ((TypeVariable) rtn).getName();
        int index = -1;
        TypeVariable[] variables = base.getSuperclass().getTypeParameters();
        if (variables == null || variables.length < 1) return null;
        for (int i = 0; i < variables.length; i++) {
            if (variables[i].getName().equals(name)) index = i;
        }
        if (index == -1) return null;
        Type genericSuperclass = base.getGenericSuperclass();
        if (!(genericSuperclass instanceof ParameterizedType)) return null;
        ParameterizedType pt = (ParameterizedType) genericSuperclass;
        Type type = pt.getActualTypeArguments()[index];
        Class clazz = getRawTypeNoException(type);
        if (clazz != null) return clazz;
        return type;
    }

    /**
    * Given an interface Method, look in the implementing class for the method that implements the interface's method
    * to obtain generic type information.  This is useful for templatized interfaces like:
    * <p/>
    * <pre>
    * interface Foo<T> {
    *    @GET
    *    List&lt;T&gt; get();
    * }
    * </pre>
    *
    * @param clazz
    * @param method interface method
    * @return
    */
    public static Type getGenericReturnTypeOfGenericInterfaceMethod(Class clazz, Method method) {
        if (!method.getDeclaringClass().isInterface()) return method.getGenericReturnType();
        try {
            Method tmp = clazz.getMethod(method.getName(), method.getParameterTypes());
            return tmp.getGenericReturnType();
        } catch (NoSuchMethodException e) {
        }
        return method.getGenericReturnType();
    }

    /**
    * Given an interface Method, look in the implementing class for the method that implements the interface's method
    * to obtain generic type information.  This is useful for templatized interfaces like:
    * <p/>
    * <pre>
    * interface Foo<T> {
    *    @GET
    *    List&lt;T&gt; get();
    * }
    * </pre>
    *
    * @param clazz
    * @param method interface method
    * @return
    */
    public static Type[] getGenericParameterTypesOfGenericInterfaceMethod(Class clazz, Method method) {
        if (!method.getDeclaringClass().isInterface()) return method.getGenericParameterTypes();
        try {
            Method tmp = clazz.getMethod(method.getName(), method.getParameterTypes());
            return tmp.getGenericParameterTypes();
        } catch (NoSuchMethodException e) {
        }
        return method.getGenericParameterTypes();
    }

    public static Class<?> getRawType(Type type) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            return (Class<?>) rawType;
        } else if (type instanceof GenericArrayType) {
            final GenericArrayType genericArrayType = (GenericArrayType) type;
            final Class<?> componentRawType = getRawType(genericArrayType.getGenericComponentType());
            return Array.newInstance(componentRawType, 0).getClass();
        }
        throw new RuntimeException("Unable to determine base class from Type");
    }

    public static Class<?> getRawTypeNoException(Type type) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            return (Class<?>) rawType;
        } else if (type instanceof GenericArrayType) {
            final GenericArrayType genericArrayType = (GenericArrayType) type;
            final Class<?> componentRawType = getRawType(genericArrayType.getGenericComponentType());
            return Array.newInstance(componentRawType, 0).getClass();
        }
        return null;
    }

    /**
    * Returns the type argument from a parameterized type
    *
    * @param genericType
    * @return null if there is no type parameter
    */
    public static Class<?> getTypeArgument(Type genericType) {
        if (!(genericType instanceof ParameterizedType)) return null;
        ParameterizedType parameterizedType = (ParameterizedType) genericType;
        Class<?> typeArg = (Class<?>) parameterizedType.getActualTypeArguments()[0];
        return typeArg;
    }

    public static class TypeInfo {

        private Class<?> type;

        private Type genericType;

        public TypeInfo(Class<?> type, Type genericType) {
            this.type = type;
            this.genericType = genericType;
        }

        public Class<?> getType() {
            return type;
        }

        public Type getGenericType() {
            return genericType;
        }
    }

    public static Class getCollectionBaseType(Class type, Type genericType) {
        if (genericType instanceof ParameterizedType) {
            if (!Collection.class.isAssignableFrom(type)) return null;
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            Type componentGenericType = parameterizedType.getActualTypeArguments()[0];
            return getRawType(componentGenericType);
        } else if (genericType instanceof GenericArrayType) {
            final GenericArrayType genericArrayType = (GenericArrayType) genericType;
            Type componentGenericType = genericArrayType.getGenericComponentType();
            return getRawType(componentGenericType);
        } else if (type.isArray()) {
            return type.getComponentType();
        }
        return null;
    }

    public static Class getMapKeyType(Type genericType) {
        if (genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            Type componentGenericType = parameterizedType.getActualTypeArguments()[0];
            return getRawType(componentGenericType);
        }
        return null;
    }

    public static Class getMapValueType(Type genericType) {
        if (genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            Type componentGenericType = parameterizedType.getActualTypeArguments()[1];
            return getRawType(componentGenericType);
        }
        return null;
    }
}
