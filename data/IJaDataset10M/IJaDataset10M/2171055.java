package net.sf.coolrmi;

import java.lang.reflect.Proxy;

/**
 * Cool RMI client.
 * Can be used to create a client object that implements
 * the user defined communication interface and connects to the
 * specified server on method call.
 * @author rizsi
 *
 */
public class CoolRMIClient extends AbstractCoolRMI {

    /**
	 * Create a client object that is parametered with server's
	 * TCP address.
	 * This method does not connect to the server on this call.
	 * @param host
	 * @param port
	 */
    public CoolRMIClient(String host, int port) {
        super(host, port);
    }

    /**
	 * Create a client proxy of the specified service.
	 * The method will not connect to the server.
	 * <br/>
	 * The generated proxy object will connect and disconnect to the server on each query
	 * (method call).
	 * Invalid service name or incompatible interface problems will only be reported when using the interface.
	 * 
	 * User exceptions are passed from the server if occur.
	 * Communication related problems are thrown as CoolRMIException. It is a RuntimeException so users may not handle them.
	 * 
	 * @param classLoader The classloader used for message serialization. Must see CoolRMI and 
	 * the communication interface.
	 * @param iface The communication interface. Must be compatible (to serial version) with the
	 * one deployed on the server. The service name will be iface.getName()
	 * @return The client proxy to the given service. Will implement the passed interface
	 */
    public Object getSingleUseService(ClassLoader classLoader, Class<?> iface) {
        return getSingleUseService(classLoader, iface, iface.getName());
    }

    /**
	 * Create a client proxy of the specified service.
	 * The method will not connect to the server.
	 * <br/>
	 * The generated proxy object will connect and disconnect to the server on each query
	 * (method call).
	 * Invalid service name or incompatible interface problems will only be reported when using the interface.
	 * 
	 * User exceptions are passed from the server if occur.
	 * Communication related problems are thrown as CoolRMIException. It is a RuntimeException so users may not handle them.
	 * 
	 * @param classLoader The classloader used for message serialization. Must see CoolRMI and 
	 * the communication interface.
	 * @param iface The communication interface. Must be compatible (to serial version) with the
	 * one deployed on the server.
	 * @param serviceName The service name.
	 * @return The client proxy to the given service. Will implement the passed interface
	 */
    public Object getSingleUseService(ClassLoader classLoader, Class<?> iface, String serviceName) {
        return Proxy.newProxyInstance(classLoader, new Class<?>[] { iface }, new CoolRMIClientProxyHandler(this, iface, serviceName));
    }
}
