package server;

import java.util.HashMap;
import client.ProxyFactoryProvider.ProxyFactory.ProxyInvocationHandler;

public interface Response {

    public SimpleUUID getObjectId();

    public String getServiceName();

    public Object unpack(HashMap<Class<?>, String> servicesMap, ProxyInvocationHandler pIH) throws Throwable;
}
