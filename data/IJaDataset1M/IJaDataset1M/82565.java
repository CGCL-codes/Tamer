package org.opene.client.proxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import org.opentools.proxies.Proxy;

/**
 * ProxyFactory for JDK 1.2.
 * 
 * @author Aaron Mulder (ammulder@alumni.princeton.edu)
 */
public class Jdk12ProxyFactory implements ProxyFactory {

    /**
     * Paramaters for the contstructor of the proxy class.
     */
    private static final Class[] constructorParams = { org.opentools.proxies.InvocationHandler.class };

    /**
     * Prepare the factory for use.  Called once when the ProxyFactory 
     * is instaniated.
     * 
     * @param props
     */
    public void init(Properties props) {
    }

    /**
     * Returns the invocation handler for the specified proxy instance.
     */
    public InvocationHandler getInvocationHandler(Object proxy) throws IllegalArgumentException {
        Jdk12InvocationHandler handler = (Jdk12InvocationHandler) Proxy.getInvocationHandler(proxy);
        if (handler == null) return null;
        return handler.getInvocationHandler();
    }

    /**
     * Sets the invocation handler for the specified proxy instance.
     */
    public Object setInvocationHandler(Object proxy, InvocationHandler handler) throws IllegalArgumentException {
        Jdk12InvocationHandler jdk12 = (Jdk12InvocationHandler) Proxy.getInvocationHandler(proxy);
        if (jdk12 == null) throw new IllegalArgumentException("Proxy " + proxy + " unknown!");
        return jdk12.setInvocationHandler(handler);
    }

    /**
     * Returns the java.lang.Class object for a proxy class given a class loader
     * and an array of interfaces.
     * 
     * @param interfce
     * @return 
     * @exception IllegalArgumentException
     */
    public Class getProxyClass(Class interfce) throws IllegalArgumentException {
        return Proxy.getProxyClass(interfce.getClassLoader(), new Class[] { interfce });
    }

    /**
     * Returns the java.lang.Class object for a proxy class given a class loader 
     * and an array of interfaces.
     * 
     * @param interfaces
     * @return 
     * @exception IllegalArgumentException
     */
    public Class getProxyClass(Class[] interfaces) throws IllegalArgumentException {
        if (interfaces.length < 1) {
            throw new IllegalArgumentException("There must be at least one interface to implement.");
        }
        return Proxy.getProxyClass(interfaces[0].getClassLoader(), interfaces);
    }

    /**
     * Returns true if and only if the specified class was dynamically generated 
     * to be a proxy class using the getProxyClass method or the newProxyInstance
     * method.
     * 
     * @param cl
     * @return 
     */
    public boolean isProxyClass(Class cl) {
        return Proxy.isProxyClass(cl);
    }

    /**
     * Creates a new proxy instance using the handler of the proxy passed in.
     * 
     * @param proxyClass
     * @return 
     * @exception IllegalArgumentException
     */
    public Object newProxyInstance(Class proxyClass) throws IllegalArgumentException {
        if (!Proxy.isProxyClass(proxyClass)) {
            throw new IllegalArgumentException("This class is not a proxy.");
        }
        try {
            Constructor cons = proxyClass.getConstructor(constructorParams);
            return cons.newInstance(new Object[] { new Jdk12InvocationHandler() });
        } catch (NoSuchMethodException e) {
            throw new InternalError(e.toString());
        } catch (IllegalAccessException e) {
            throw new InternalError(e.toString());
        } catch (InstantiationException e) {
            throw new InternalError(e.toString());
        } catch (InvocationTargetException e) {
            throw new InternalError(e.toString());
        }
    }

    /**
     * Returns an instance of a proxy class for the specified interface that 
     * dispatches method invocations to the specified invocation handler.
     * 
     * @param interfce
     * @param h
     * @return 
     * @exception IllegalArgumentException
     */
    public Object newProxyInstance(Class interfce, InvocationHandler h) throws IllegalArgumentException {
        Jdk12InvocationHandler handler = new Jdk12InvocationHandler(h);
        return Proxy.newProxyInstance(interfce.getClassLoader(), new Class[] { interfce }, handler);
    }

    /**
     * Returns an instance of a proxy class for the specified interface that 
     * dispatches method invocations to the specified invocation handler.
     * 
     * @param interfaces
     * @param h
     * @return 
     * @exception IllegalArgumentException
     */
    public Object newProxyInstance(Class[] interfaces, InvocationHandler h) throws IllegalArgumentException {
        if (interfaces.length < 1) {
            throw new IllegalArgumentException("There must be at least one interface to implement.");
        }
        Jdk12InvocationHandler handler = new Jdk12InvocationHandler(h);
        return Proxy.newProxyInstance(interfaces[0].getClassLoader(), interfaces, handler);
    }
}
