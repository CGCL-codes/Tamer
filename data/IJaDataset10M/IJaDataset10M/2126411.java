package com.mycila.plugin.spi.aop;

import java.lang.reflect.UndeclaredThrowableException;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class CglibUtils {

    static {
        try {
            CglibUtils.class.getClassLoader().loadClass("net.sf.cglib.reflect.FastClass");
        } catch (ClassNotFoundException ignored) {
            throw new AssertionError("CGLIB is missing in your classpath");
        }
    }

    private CglibUtils() {
    }

    private static final ClassLoader CGLIB_CLASS_LOADER = net.sf.cglib.proxy.Enhancer.class.getClassLoader();

    private static final String CGLIB_PACKAGE = net.sf.cglib.proxy.Enhancer.class.getName().replaceFirst("\\.cglib\\..*$", ".cglib");

    private static final WeakCache<ClassLoader, ClassLoader> CLASS_LOADER_CACHE = new WeakCache<ClassLoader, ClassLoader>(new WeakCache.Provider<ClassLoader, ClassLoader>() {

        @Override
        public ClassLoader get(ClassLoader key) {
            return new BridgeClassLoader(key, CGLIB_CLASS_LOADER, CGLIB_PACKAGE);
        }
    });

    private static final net.sf.cglib.core.NamingPolicy NAMING_POLICY = new net.sf.cglib.core.DefaultNamingPolicy() {

        @Override
        protected String getTag() {
            return "ByMycilaPlugin";
        }
    };

    private static final WeakCache<Class<?>, net.sf.cglib.reflect.FastClass> FAST_CLASS_CACHE = new WeakCache<Class<?>, net.sf.cglib.reflect.FastClass>(new WeakCache.Provider<Class<?>, net.sf.cglib.reflect.FastClass>() {

        @Override
        public net.sf.cglib.reflect.FastClass get(Class<?> type) {
            net.sf.cglib.reflect.FastClass.Generator generator = new net.sf.cglib.reflect.FastClass.Generator();
            generator.setType(type);
            generator.setClassLoader(getClassLoader(type));
            generator.setNamingPolicy(NAMING_POLICY);
            return generator.create();
        }
    });

    private static ClassLoader getClassLoader(Class<?> type) {
        ClassLoader delegate = canonicalize(type.getClassLoader());
        if (delegate == getSystemClassLoader()) return delegate;
        if (delegate instanceof BridgeClassLoader) return delegate;
        return CLASS_LOADER_CACHE.get(delegate);
    }

    public static net.sf.cglib.reflect.FastClass getFastClass(Class<?> c) {
        return FAST_CLASS_CACHE.get(c);
    }

    static net.sf.cglib.proxy.Enhancer newEnhancer(Class<?> type) {
        net.sf.cglib.proxy.Enhancer enhancer = new net.sf.cglib.proxy.Enhancer();
        enhancer.setSuperclass(type);
        enhancer.setUseFactory(false);
        enhancer.setClassLoader(getClassLoader(type));
        enhancer.setNamingPolicy(NAMING_POLICY);
        enhancer.setStrategy(new net.sf.cglib.transform.impl.UndeclaredThrowableStrategy(UndeclaredThrowableException.class));
        enhancer.setInterceptDuringConstruction(false);
        return enhancer;
    }

    private static ClassLoader canonicalize(ClassLoader classLoader) {
        return classLoader != null ? classLoader : getSystemClassLoader();
    }

    /**
     * Returns the system classloader, or {@code null} if we don't have
     * permission.
     */
    private static ClassLoader getSystemClassLoader() {
        try {
            return ClassLoader.getSystemClassLoader();
        } catch (SecurityException e) {
            throw new AssertionError("Cannot get System Classloader !");
        }
    }
}
