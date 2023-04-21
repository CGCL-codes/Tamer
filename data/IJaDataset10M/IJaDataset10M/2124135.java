package org.piuframework.service.impl;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import org.piuframework.context.ApplicationContext;
import org.piuframework.context.ApplicationContextException;
import org.piuframework.context.ServiceContext;
import org.piuframework.service.InstantiationException;
import org.piuframework.service.ApplicationContextAware;
import org.piuframework.service.Interceptor;
import org.piuframework.service.ServiceCreateException;
import org.piuframework.service.ServiceException;
import org.piuframework.service.ServiceInvocationHandler;
import org.piuframework.service.ServiceLifecycleHandler;
import org.piuframework.service.config.InvocationHandlerConfig;
import org.piuframework.service.config.LifecycleHandlerConfig;
import org.piuframework.service.config.ServiceConfig;

/**
 * Internal class that manages service objects and creates dynamic proxies for service objects.
 *
 * @author Dirk Mascher
 */
class ServiceObjectContainer implements Serializable {

    private static final long serialVersionUID = 5017002570199064748L;

    private ApplicationContext context;

    private Map interceptorMap;

    private Map invocationHandlerMap;

    private Map lifecycleHandlerMap;

    private ServiceProxyCache statelessServiceProxyCache;

    ServiceObjectContainer(ApplicationContext context) throws ApplicationContextException, ServiceException {
        this.context = context;
        interceptorMap = new InterceptorMap(context);
        invocationHandlerMap = new ServiceInvocationHandlerMap(context);
        lifecycleHandlerMap = new ServiceLifecycleHandlerMap(context);
        statelessServiceProxyCache = new ServiceProxyCache();
    }

    private ServiceContext getServiceContext() throws ApplicationContextException {
        return (ServiceContext) context.getSubContext(ServiceContext.NAME);
    }

    private ServiceFactoryInternal getServiceFactory() throws ApplicationContextException {
        return (ServiceFactoryInternal) getServiceContext().getServiceFactory();
    }

    /** 
     * Internal helper method. Delegates to service factory in current context.
     */
    private LifecycleHandlerConfig getLifecycleHandlerConfig(Class serviceInterface, String flavor) {
        return getServiceFactory().getLifecycleHandlerConfig(serviceInterface, flavor);
    }

    /** 
     * Internal helper method. Delegates to service factory in current context.
     */
    private InvocationHandlerConfig getInvocationHandlerConfig(Class serviceInterface, String flavor) {
        return getServiceFactory().getInvocationHandlerConfig(serviceInterface, flavor);
    }

    /** 
     * Internal helper method. Delegates to service factory in current context.
     */
    private ServiceConfig getServiceConfig(Class serviceInterface) {
        return getServiceFactory().getServiceConfig(serviceInterface);
    }

    /**
     * Returns interceptor by name.
     * @param name Symbolic name of the interceptor as defined in container configuration.
     * @return Interceptor instance
     */
    Interceptor getInterceptor(String name) {
        return (Interceptor) interceptorMap.get(name);
    }

    /**
     * Returns lifecycle handler
     * @param name Symbolic name of the lifecycle handler as defined in container configuration.
     * @return lifecycle handler instance
     */
    ServiceLifecycleHandler getLifecycleHandler(LifecycleHandlerConfig config) throws InstantiationException {
        ServiceLifecycleHandler lifecycleHandler = null;
        if (config.getName() != null) {
            lifecycleHandler = (ServiceLifecycleHandler) lifecycleHandlerMap.get(config.getName());
        } else {
            lifecycleHandler = (ServiceLifecycleHandler) InternalObjectFactory.createInternalObject(config.getClassName(), context);
        }
        return lifecycleHandler;
    }

    /**
     * Returns service invocation handler
     * @return Invocation handler instance
     */
    ServiceInvocationHandler getInvocationHandler(InvocationHandlerConfig config) throws InstantiationException {
        ServiceInvocationHandler invocationHandler = null;
        if (config.getName() != null) {
            invocationHandler = (ServiceInvocationHandler) invocationHandlerMap.get(config.getName());
        } else {
            invocationHandler = (ServiceInvocationHandler) InternalObjectFactory.createInternalObject(config.getClassName(), context);
        }
        return invocationHandler;
    }

    /**
     * Returns a proxy for the service identified by the specified interface and flavor.
     * If the service is registered as stateful the method will always create a new service object and a new proxy.
     * If the service is registsred as stateless a new service object (and proxy) will only be created if it
     * hasn't been created before (singleton semantic).
     * 
     * @param serviceInterface The interface that identifies the service
     * @param flavor The flavor of the service (may be null).
     * @param createArgs Optional array with create arguments (only for stateful services allowed)
     * @return Proxy object for the service
     * @throws ServiceException
     */
    Object getServiceProxy(Class serviceInterface, String flavor, Object[] createArgs) throws ServiceException {
        Object proxy = null;
        if (isServiceStateful(serviceInterface)) {
            Object serviceObject = createServiceObject(serviceInterface, flavor, createArgs);
            proxy = createServiceProxy(serviceInterface, flavor, serviceObject);
        } else {
            if (createArgs != null) {
                throw new ServiceCreateException("create arguments not allowed for stateless service", serviceInterface, flavor);
            }
            proxy = statelessServiceProxyCache.get(serviceInterface, flavor);
            if (proxy == null) {
                synchronized (statelessServiceProxyCache) {
                    if (proxy == null) {
                        Object serviceObject = createServiceObject(serviceInterface, flavor, null);
                        proxy = createServiceProxy(serviceInterface, flavor, serviceObject);
                        statelessServiceProxyCache.put(serviceInterface, flavor, proxy);
                    }
                }
            }
        }
        return proxy;
    }

    /**
     * Only needed for stateful services. Detaches the proxy from the service object and removes the service object
     * using its create strategy implementation. 
     * @param proxy The proxy to release
     * @return True, if the proxy was released. False, otherwise (proxy is stateless or was already released).
     */
    boolean releaseServiceProxy(Object proxy) {
        boolean released = false;
        if (proxy != null) {
            ProxyInvocationHandler invocationHandler = (ProxyInvocationHandler) Proxy.getInvocationHandler(proxy);
            if (isServiceStateful(invocationHandler.getServiceInterface())) {
                released = invocationHandler.removeServiceObject();
            }
        }
        return released;
    }

    /**
     * Creates a new service object instance for the specified service by using its defined lifecycle handler.
     * @param serviceInterface The interface that identifies the service
     * @param flavor The flavor of the service (may be null).
     * @param createArgs Array with arguments for constructor or create method (may be null).
     * @return The newly create service object instance
     * @throws ServiceCreateException Failed to create service object
     */
    Object createServiceObject(Class serviceInterface, String flavor, Object[] createArgs) throws ServiceCreateException {
        LifecycleHandlerConfig lifecycleHandlerConfig = getLifecycleHandlerConfig(serviceInterface, flavor);
        ServiceLifecycleHandler lifecycleHandler = getLifecycleHandler(lifecycleHandlerConfig);
        Class[] createSignature = null;
        ServiceConfig serviceConfig = getServiceConfig(serviceInterface);
        if (serviceConfig == null && createArgs != null) {
            throw new ServiceCreateException("stateful services must be registered", serviceInterface, flavor);
        } else {
            if (serviceConfig != null) {
                createSignature = serviceConfig.getCreateSignature();
            }
        }
        Object serviceObject = lifecycleHandler.createServiceObject(serviceInterface, flavor, createSignature, createArgs);
        propagateApplicationContext(serviceObject);
        return serviceObject;
    }

    /**
     * This method propagates the application context to the service object.
     * @param serviceObject The service object
     */
    void propagateApplicationContext(Object serviceObject) {
        if (serviceObject instanceof ApplicationContextAware) {
            ((ApplicationContextAware) serviceObject).setApplicationContext(context);
        }
    }

    /**
     * Creates a dynamic proxy for a service object.
     * If the service is registered as stateful the proxy object will be wrapped in a <code>ServiceProxy</code>
     * object and stored in a map using the actual dynamic proxy as the key. This wrapper object also has a 
     * reference to the lifecycle handler that was used to create the service object and a reference to the
     * invocation handler of the proxy (which internally keeps a reference to the actual service object). This is
     * needed so that a <code>releaseServiceProxy</code> invocation on the proxy can actually use the lifecycle
     * handler to gracefully remove the service object.
     * @param serviceInterface The interface that identifies the service
     * @param flavor The flavor of the service (may be null).
     * @param serviceObject The service object
     * @return The dynamic proxy object
     * @throws ServiceCreateException
     */
    Object createServiceProxy(Class serviceInterface, String flavor, Object serviceObject) throws ServiceCreateException {
        String serviceName = null;
        ServiceConfig serviceConfig = getServiceConfig(serviceInterface);
        if (serviceConfig != null) {
            serviceName = serviceConfig.getName();
        }
        InvocationHandlerConfig invocationHandlerConfig = getInvocationHandlerConfig(serviceInterface, flavor);
        ServiceInvocationHandler invocationHandler = getInvocationHandler(invocationHandlerConfig);
        LifecycleHandlerConfig lifecycleHandlerConfig = getLifecycleHandlerConfig(serviceInterface, flavor);
        ServiceLifecycleHandler lifecycleHandler = getLifecycleHandler(lifecycleHandlerConfig);
        ProxyInvocationHandler proxyInvocationHandler = new ProxyInvocationHandler(serviceName, serviceInterface, flavor, serviceObject, lifecycleHandler, invocationHandler);
        Object proxy = null;
        try {
            Class proxyClass = Proxy.getProxyClass(serviceInterface.getClassLoader(), new Class[] { serviceInterface });
            Constructor constructor = proxyClass.getConstructor(new Class[] { InvocationHandler.class });
            proxy = constructor.newInstance(new Object[] { proxyInvocationHandler });
        } catch (Throwable t) {
            throw new ServiceCreateException("failed to create proxy", serviceInterface, flavor, t);
        }
        return proxy;
    }

    /**
     * Internal helper method. Returns true if the specified service is registered as stateful.
     * @param serviceConfig The ServiceConfig object for the service
     * @return True, if service is statefull. False, if service is stateless.
     */
    private boolean isServiceStateful(ServiceConfig serviceConfig) {
        return serviceConfig == null ? false : serviceConfig.isStateful();
    }

    /**
     * Internal helper method. Returns true if the specified service is registered as stateful.
     * @param serviceInterface The interface that identifies the service
     * @return True, if service is statefull. False, if service is stateless.
     */
    private boolean isServiceStateful(Class serviceInterface) {
        return isServiceStateful(getServiceConfig(serviceInterface));
    }
}

/**
 * Internal class used to cache proxy objects for stateless services.
 *
 * @author Dirk Mascher
 */
class ServiceProxyCache implements Serializable {

    private static final long serialVersionUID = -8412213019270186244L;

    private Map cache = new HashMap();

    ServiceProxyCache() {
    }

    Object get(Class serviceInterface, String flavor) {
        return cache.get(getCompositeKey(serviceInterface, flavor));
    }

    void put(Class serviceInterface, String flavor, Object serviceObject) {
        cache.put(getCompositeKey(serviceInterface, flavor), serviceObject);
    }

    Object remove(Class serviceInterface, String flavor) {
        return cache.remove(getCompositeKey(serviceInterface, flavor));
    }

    /**
     * A service is uniquely identified by its interface and flavor
     * @param serviceInterface The service interface
     * @param flavor The service flavor (may be null).
     * @return Key used for the internal HashMap
     */
    private String getCompositeKey(Class serviceInterface, String flavor) {
        StringBuffer keybuf = new StringBuffer(serviceInterface.getName());
        if (flavor != null) {
            keybuf.append(":");
            keybuf.append(flavor);
        }
        return keybuf.toString();
    }
}
