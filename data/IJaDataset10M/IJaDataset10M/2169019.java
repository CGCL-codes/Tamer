package org.apache.axis2.rmi.client;

import org.apache.axis2.AxisFault;
import org.apache.axis2.rmi.Configurator;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProxyInvocationHandler implements InvocationHandler {

    private RMIClient rmiClient;

    public ProxyInvocationHandler(Class interfaceClass, Configurator configurator, String epr) throws AxisFault {
        this.rmiClient = new RMIClient(interfaceClass, configurator, epr);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return this.rmiClient.invokeMethod(method.getName(), args);
    }
}
