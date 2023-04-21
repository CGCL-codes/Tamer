package com.teletalk.jserver.util.exception;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 *  
 * @since 2.1
 */
public class ProxyExceptionTranslator implements InvocationHandler {

    private final Object target;

    private final InvocationExceptionTranslator methodInvocationExceptionTranslator;

    protected ProxyExceptionTranslator(final Object target, final InvocationExceptionTranslator methodInvocationExceptionTranslator) {
        this.target = target;
        this.methodInvocationExceptionTranslator = methodInvocationExceptionTranslator;
    }

    /**
    * 
    */
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        try {
            return method.invoke(target, args);
        } catch (Throwable exception) {
            throw this.methodInvocationExceptionTranslator.translate(this.target, method, args, exception);
        }
    }

    /**
    * 
    */
    public static Object createProxy(final Object target, final InvocationExceptionTranslator methodInvocationExceptionTranslator) {
        return createProxy(target, methodInvocationExceptionTranslator, new Class[] { target.getClass() }, target.getClass().getClassLoader());
    }

    /**
    * 
    */
    public static Object createProxy(final Object target, final InvocationExceptionTranslator methodInvocationExceptionTranslator, final Class intefaceClass) {
        return createProxy(target, methodInvocationExceptionTranslator, new Class[] { intefaceClass }, target.getClass().getClassLoader());
    }

    /**
    * 
    */
    public static Object createProxy(final Object target, final InvocationExceptionTranslator methodInvocationExceptionTranslator, final Class[] intefaceClasses) {
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), intefaceClasses, new ProxyExceptionTranslator(target, methodInvocationExceptionTranslator));
    }

    /**
    * 
    */
    public static Object createProxy(final Object target, final InvocationExceptionTranslator methodInvocationExceptionTranslator, final Class[] intefaceClasses, final ClassLoader classLoader) {
        return Proxy.newProxyInstance(classLoader, intefaceClasses, new ProxyExceptionTranslator(target, methodInvocationExceptionTranslator));
    }
}
