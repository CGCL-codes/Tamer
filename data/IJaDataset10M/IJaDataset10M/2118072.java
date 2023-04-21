package net.sf.proxybean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;

/**
 * A utility class for generating proxy bean instances backed by
 * {@link HashMap}s and with basic behaviours outlined by
 * {@link BasicBeanBehaviourDefinition} for given interfaces.
 * @author Tim Eagles
 *
 */
public final class ProxyBeanFactory {

    /**
     * Private utility class constructor.
     */
    private ProxyBeanFactory() {
    }

    /**
     * Creates a new proxy object instance with a
     * {@link MapBackedInvocationHandler} using a {@link HashMap} to store the
     * bean properties and a {@link BasicBeanBehaviourDefinition} to define
     * behaviours.
     * @param <T> the type to be returned (determined by toProxy).
     * @param toProxy the class to proxy.
     * @return a new proxy instance.
     */
    @SuppressWarnings("unchecked")
    public static <T> T createProxyBean(Class<T> toProxy) {
        InvocationHandler handler = new MapBackedInvocationHandler<T>(toProxy, new HashMap<String, Object>());
        return (T) Proxy.newProxyInstance(toProxy.getClassLoader(), new Class[] { toProxy }, handler);
    }
}
