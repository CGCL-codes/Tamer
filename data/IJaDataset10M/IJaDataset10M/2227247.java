package org.xactor.remoting;

import java.lang.reflect.Proxy;
import org.jboss.remoting.InvokerLocator;

/**
 * Utility class for creating remote proxies, converting them to strings and
 * converting strings to remote proxies.
 *
 * @author <a href="mailto:reverbel@ime.usp.br">Francisco Reverbel</a>
 * @version $Revision: 37634 $ 
 */
public class RemoteProxy {

    /**
    * Create a remote proxy given a DTM interface, a target object id and
    * an array of <code>InvokerLocator</code>s.
    *  
    * @param interf   the DTM interface to be implemented by the proxy
    * @param oid      the id of the remote object proxified 
    * @param locators the array of invoker locators
    * @return         a newly created proxy
    */
    public static Object create(Class interf, long oid, InvokerLocator[] locators) {
        try {
            ClientInvocationHandler handler = new ClientInvocationHandler(interf, oid, locators);
            return Proxy.newProxyInstance(interf.getClassLoader(), new Class[] { interf }, handler);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
    * Converts a DTM proxy to String
    * 
    * @param p  a DTM proxy
    * @return   the string representation of the proxy
    */
    public static String toString(Proxy p) {
        ClientInvocationHandler handler = (ClientInvocationHandler) Proxy.getInvocationHandler(p);
        return handler.toString();
    }

    /**
    * Converts a stringfied DTM proxy back into a proxy instance. 
    *  
    * @param s  the string representation of a DTM proxy
    * @return   a DTM proxy
    * @throws Exception
    */
    public static Object fromString(String s) throws Exception {
        ClientInvocationHandler handler = ClientInvocationHandler.fromString(s);
        Class interf = handler.getClientInterface();
        return Proxy.newProxyInstance(interf.getClassLoader(), new Class[] { interf }, handler);
    }
}
