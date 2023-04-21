package org.mwanzia;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * Defines a Plugin to the Mwanzia framework. Plugins can contribute enhancements
 * to the generated JavaScript code and can also modify the request processing
 * behavior by registering an Interceptor.
 * </p>
 * 
 * @author percy
 * 
 */
public abstract class Plugin {

    /**
     * <p>
     * Hook for adding functionality to generated JavaScript classes (inside the
     * curly braces).
     * </p>
     * 
     * @param js
     */
    public void enhanceRemoteObject(PrettyPrinter js, Class clazz) {
    }

    /**
     * Hook for post-processing the class after it has been defined (i.e.
     * outside of the curly braces).
     * 
     * @param js
     * @param clazz
     * @param instanceMethods
     * @param staticMethods
     * @param transferableProperties
     */
    public void postProcessClass(PrettyPrinter js, Class clazz, Set<Method> instanceMethods, Set<Method> staticMethods, Set<String> transferableProperties) {
    }

    /**
     * Hook for adding information to the type descriptor for the given class.
     * 
     * @param js
     * @param clazz
     */
    public void addToTypeDescriptor(PrettyPrinter js, Class clazz) {
    }

    /**
     * A new Interceptor to use for the duration of the current method
     * invocation.
     * 
     * @return
     */
    public Interceptor buildInterceptor() {
        return new Interceptor();
    }

    /**
     * Hook for registering additional remote types with the Application that is
     * loading this Plugin.
     * 
     * @return
     */
    public List<Class> getRemoteTypes() {
        return new ArrayList<Class>();
    }
}
