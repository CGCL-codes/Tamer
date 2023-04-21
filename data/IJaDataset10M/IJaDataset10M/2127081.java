package org.openejb.client.proxy;

import java.util.Properties;

/**
 * Allows us to implement different versions of Proxies
 * 
 * @author <a href="mailto:david.blevins@visi.com">David Blevins</a>
 * @since 11/25/2001
 */
public interface ProxyFactory {

    /**
     * Prepares the ProxyFactory for use.  Called once right after
     * the ProxyFactory is instantiated.
     * 
     * @param props
     */
    public void init(Properties props);

    /**
     * Returns the invocation handler for the specified proxy instance.
     */
    public InvocationHandler getInvocationHandler(Object proxy) throws IllegalArgumentException;

    /**
     * Sets the invocation handler for the specified proxy instance.
     */
    public Object setInvocationHandler(Object proxy, InvocationHandler handler) throws IllegalArgumentException;

    /**
     * Returns the java.lang.Class object for a proxy class given a class loader 
     * and an array of interfaces.
     * 
     * @param interfce
     * @return 
     * @exception IllegalArgumentException
     */
    public Class getProxyClass(Class interfce) throws IllegalArgumentException;

    /**
     * Returns the java.lang.Class object for a proxy class given a class loader 
     * and an array of interfaces.
     * 
     * @param interfaces
     * @return 
     * @exception IllegalArgumentException
     */
    public Class getProxyClass(Class[] interfaces) throws IllegalArgumentException;

    /**
     * Returns true if and only if the specified class was dynamically generated 
     * to be a proxy class using the getProxyClass method or the newProxyInstance
     * method.
     * 
     * @param cl
     * @return 
     */
    public boolean isProxyClass(Class cl);

    /**
     * Returns an instance of a proxy class for the specified interface that 
     * dispatches method invocations to the specified invocation handler.
     * 
     * @param interfce
     * @param h
     * @return 
     * @exception IllegalArgumentException
     */
    public Object newProxyInstance(Class interfce, InvocationHandler h) throws IllegalArgumentException;

    /**
     * Returns an instance of a proxy class for the specified interface that 
     * dispatches method invocations to the specified invocation handler.
     * 
     * @param interfaces
     * @param h
     * @return 
     * @exception IllegalArgumentException
     */
    public Object newProxyInstance(Class[] interfaces, InvocationHandler h) throws IllegalArgumentException;

    /**
     * Returns a new proxy instance from the specified proxy class.  The
     * interface(s) implemented by the proxy instance are determined by
     * the proxy class.  The class name may or may not be meaningful,
     * depending on the implementation.
     * 
     * @param proxyClass
     * @return 
     * @exception java.lang.IllegalArgumentException
     *                   Occurs when the specified class is not a proxy class.
     * @exception IllegalArgumentException
     */
    public Object newProxyInstance(Class proxyClass) throws IllegalArgumentException;
}
