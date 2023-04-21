package com.eaio.plateau.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.rmi.Remote;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The RemoteProxy class caches {@link java.lang.reflect.Constructor} instances
 * of {@link java.lang.reflect.Proxy} classes for faster instantiation times.
 * 
 * @author <a href="mailto:jb@eaio.com">Johann Burkard</a>
 * @version $Id: RemoteProxy.java,v 1.1 2005/11/20 20:15:02 grnull Exp $
 */
public final class RemoteProxy {

    /**
  * The cached constructors.
  */
    private static final Map CONSTRUCTOR_CACHE = new HashMap();

    /**
  * No instances required.
  */
    private RemoteProxy() {
    }

    /**
  * Generate a new proxy class from the given {@link java.rmi.Remote} object and
  * the given {@link java.lang.reflect.InvocationHandler}.
  * 
  * @param obj the remote object
  * @param handler the handler
  * @return Remote an object implementing the Remote interface
  * @throws IllegalAccessException
  */
    public static Remote forObject(Remote obj, InvocationHandler handler) throws IllegalAccessException {
        Constructor c = (Constructor) CONSTRUCTOR_CACHE.get(obj.getClass());
        if (c != null) {
            try {
                return (Remote) c.newInstance(new Object[] { handler });
            } catch (InstantiationException ex) {
                throw new RuntimeException(ex.getLocalizedMessage(), ex);
            } catch (InvocationTargetException ex) {
                throw new RuntimeException(ex.getLocalizedMessage(), ex);
            }
        } else {
            Class[] interfaces = obj.getClass().getInterfaces();
            ArrayList l = new ArrayList(interfaces.length);
            for (int i = 0; i < interfaces.length; ++i) {
                if (Remote.class.isAssignableFrom(interfaces[i])) {
                    l.add(interfaces[i]);
                }
            }
            Class clazz = Proxy.getProxyClass(obj.getClass().getClassLoader(), (Class[]) l.toArray(new Class[l.size()]));
            try {
                c = clazz.getConstructor(new Class[] { InvocationHandler.class });
            } catch (NoSuchMethodException ex) {
            }
            CONSTRUCTOR_CACHE.put(obj.getClass(), c);
            return forObject(obj, handler);
        }
    }
}
