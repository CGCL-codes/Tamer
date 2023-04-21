package padrmi;

import java.lang.reflect.*;
import java.lang.reflect.Proxy;
import java.net.*;

/**
 * Creates proxies (also known as stubs), user friendly interface to the remote
 * object. Proxy imitates local object and passes the calls to the underlying
 * server. See: Java Design Patterns: proxy pattern.
 * 
 * @see java.lang.reflect.Proxy
 */
public class ProxyFactory {

    /**
	 * Server that is used by instance of factory.
	 */
    private Server server;

    /**
	 * Creates proxy factory on a specified server.
	 * 
	 * @param server
	 *            the server factory will be using.
	 */
    ProxyFactory(Server server) {
        this.server = server;
    }

    /**
	 * Creates a proxy object implementing given interface, for remote object
	 * pointed by the URL.
	 * 
	 * @param objectURL
	 *            object to which the calls will be relayed
	 * @param iface
	 *            interface the proxy will conform to
	 * @return proxy object
	 * @throws MalformedURLException
	 *             if the <code>objectURL</code> is invalid
	 */
    public PpRemote createProxy(URL objectURL, Class<?> iface) throws MalformedURLException {
        InvocationHandler handler = new InvocationHandlerImpl(objectURL);
        URL resourceURL = new URL(objectURL, ".");
        PpRemote proxy = (PpRemote) Proxy.newProxyInstance(URLClassLoaderFactory.getURLClassLoader(resourceURL), new Class[] { iface }, handler);
        return proxy;
    }

    /**
	 * Private handler for PadRMI Protocol remote objects. New instance is
	 * created for each proxy.
	 * 
	 * @see TimeoutInvocator
	 */
    private class InvocationHandlerImpl implements InvocationHandler {

        /**
		 * URL of object.
		 */
        private URL objectURL;

        /**
		 * Creates new handler for object.
		 * 
		 * @param url
		 *            URL of object
		 */
        public InvocationHandlerImpl(URL url) {
            this.objectURL = url;
        }

        /**
		 * Handles calls performed on proxy object and passes them to the local
		 * server using <code>TimeoutInvocator</code>.
		 * 
		 * @see TimeoutInvocator#invoke(URL, Object[], long)
		 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object,
		 *      java.lang.reflect.Method, java.lang.Object[])
		 */
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            try {
                URL methodURL = new URL(objectURL + "/" + method.getName());
                if (method.isAnnotationPresent(Timeout.class)) {
                    return server.getTimeoutInvocator().invoke(methodURL, args, method.getAnnotation(Timeout.class).value());
                } else {
                    return server.getTimeoutInvocator().invoke(methodURL, args);
                }
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
        }
    }
}
