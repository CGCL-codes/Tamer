package com.volantis.osgi.jdo;

import org.jpox.PersistenceManagerFactoryImpl;
import org.osgi.framework.Bundle;
import javax.jdo.PersistenceManagerFactory;
import java.util.Map;

public class OSGiHelper {

    public static PersistenceManagerFactory getPersistenceManagerFactory(Bundle bundle, Map properties) {
        ClassLoader oldLoader = pushContextClassLoader(bundle);
        try {
            PersistenceManagerFactory factory = PersistenceManagerFactoryImpl.getPersistenceManagerFactory(properties);
            return new OSGiPersistenceManagerFactory(bundle, factory);
        } finally {
            popContextClassLoader(oldLoader);
        }
    }

    public static void popContextClassLoader(ClassLoader oldLoader) {
        Thread thread = Thread.currentThread();
        thread.setContextClassLoader(oldLoader);
    }

    public static ClassLoader pushContextClassLoader(Bundle bundle) {
        Thread thread = Thread.currentThread();
        ClassLoader oldLoader = thread.getContextClassLoader();
        ClassLoader jpoxLoader = OSGiHelper.class.getClassLoader();
        ClassLoader newLoader;
        if (bundle != null) {
            ClassLoader callingLoader = new BundleClassLoader(jpoxLoader, bundle);
            newLoader = new DuplexClassLoader(callingLoader, oldLoader);
        } else {
            newLoader = new DuplexClassLoader(jpoxLoader, oldLoader);
        }
        thread.setContextClassLoader(newLoader);
        return oldLoader;
    }
}
