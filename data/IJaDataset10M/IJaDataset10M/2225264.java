package org.skuebeck.ooc;

import java.lang.reflect.Proxy;
import org.skuebeck.ooc.annotations.Concurrent;

public final class ConcurrentObjects {

    private static ConcurrentObjectConfig commonConfig = new ConcurrentObjectConfig();

    public static ConcurrentObjectConfig getCommonConfig() {
        try {
            return (ConcurrentObjectConfig) (commonConfig.clone());
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void setCommonConfig(ConcurrentObjectConfig commonConfig) {
        ConcurrentObjects.commonConfig = commonConfig;
    }

    public static <T> T makeConcurrent(T object) {
        return makeConcurrent(object, commonConfig);
    }

    @SuppressWarnings("unchecked")
    public static <T> T makeConcurrent(T object, ConcurrentObjectConfig config) {
        return (T) Proxy.newProxyInstance(object.getClass().getClassLoader(), Classes.allInterfacesOf(object), new ConcurrentObjectInvocationHandler(object, config));
    }

    public static <T> T newInstance(Class<T> theClass, ConcurrentObjectConfig config) {
        try {
            return makeConcurrent(theClass.newInstance(), config);
        } catch (InstantiationException e) {
            InstantiationError f = new InstantiationError(e.getMessage());
            f.initCause(e);
            throw f;
        } catch (IllegalAccessException e) {
            IllegalAccessError f = new IllegalAccessError(e.getMessage());
            f.initCause(e);
            throw f;
        }
    }

    public static <T> T newInstance(Class<T> theClass) {
        return newInstance(theClass, commonConfig);
    }

    public static boolean isConcurrent(Object object) {
        if (!Proxy.isProxyClass(object.getClass())) {
            return false;
        }
        return Classes.atLeastOneInterfaceIsAnnotadedWith(object, Concurrent.class);
    }
}
