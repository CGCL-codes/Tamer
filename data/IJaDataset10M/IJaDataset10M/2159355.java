package com.dyuproject.protostuff.runtime;

import java.lang.reflect.Constructor;

/**
 * This class is expected not to load unless {@link RuntimeEnv} made sure that 
 * sun.reflect.ReflectionFactory is in the classpath.
 *
 * @author David Yu
 * @created Jul 8, 2011
 */
final class OnDemandSunReflectionFactory {

    private OnDemandSunReflectionFactory() {
    }

    @SuppressWarnings("unchecked")
    static <T> Constructor<T> getConstructor(Class<T> clazz, Constructor<Object> constructor) {
        return sun.reflect.ReflectionFactory.getReflectionFactory().newConstructorForSerialization(clazz, constructor);
    }
}
