package com.dinstone.ut.faststub;

import com.dinstone.ut.faststub.internal.ProxyFactory;

/**
 * The stub factory to generate Stub instance.The stub type either Interface
 * type or Class type.
 * 
 * @author dinstone
 * 
 */
public class FastStub {

    /**
     * Creating stub instance.
     * 
     * @param <T>
     *            stub type, Interface or non final Class
     * @param toStub
     *            required stub class
     * @return stub instance
     */
    public static <T> T createStub(final Class<T> toStub) {
        return createStub(toStub, null);
    }

    /**
     * Creating stub instance, specify a custom method interceptor.
     * 
     * @param <T>
     *            stub type, Interface or non final Class
     * @param toStub
     *            required stub class
     * @param interceptor
     *            method interceptor to intercept the target method invocation
     * @return stub instance
     */
    public static <T> T createStub(final Class<T> toStub, MethodInterceptor interceptor) {
        ProxyFactory factory = new ProxyFactory();
        return factory.createProxyInstance(toStub, interceptor);
    }
}
