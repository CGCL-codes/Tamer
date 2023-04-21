package org.piuframework.service.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.piuframework.PiuFrameworkException;
import org.piuframework.config.ConfigProperties;
import org.piuframework.context.ApplicationContext;
import org.piuframework.context.ApplicationContextException;
import org.piuframework.context.ServiceContext;
import org.piuframework.service.ServiceException;
import org.piuframework.service.ServiceInvocationHandler;
import org.piuframework.service.ServiceInvokeException;
import org.piuframework.service.config.FlavorConfig;
import org.piuframework.service.config.InvocationHandlerConfig;
import org.piuframework.service.config.ServiceConfig;
import org.piuframework.service.impl.ServiceFactoryInternal;
import org.piuframework.util.ClassUtils;

/**
 * TODO
 * 
 * @author Dirk Mascher
 */
public abstract class AbstractInvocationHandler implements ServiceInvocationHandler {

    public static final String PROPERTIES_META_SUBST_PREFIX = "service";

    public static final String PROPERTY_SERVICE_NAME = "service.name";

    private static final Log log = LogFactory.getLog(AbstractInvocationHandler.class);

    protected ServiceContext serviceContext;

    protected String name;

    public AbstractInvocationHandler(ApplicationContext context, String name) throws ApplicationContextException {
        this.serviceContext = (ServiceContext) context.getSubContext(ServiceContext.NAME);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public final Object invoke(String serviceName, Class serviceInterface, String flavor, Object proxy, Object serviceObject, Method method, Object[] args) throws Throwable, ServiceInvokeException {
        Object result = null;
        try {
            if ("finalize".equals(method.getName())) {
                if (getServiceFactory().releaseService(proxy)) {
                    log.warn("service not explicity released (auto-release on proxy finalize), " + serviceInterface + (flavor != null ? ", flavor " + flavor : ""));
                }
            } else {
                result = invoke(serviceName, serviceInterface, flavor, serviceObject, method, args);
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Throwable t) {
            Throwable caughtException = t;
            t = PiuFrameworkException.getTargetException(t);
            Class businessExceptions[] = getBusinessExceptions(serviceInterface, method);
            for (int i = 0; i < businessExceptions.length; i++) {
                if (businessExceptions[i].isInstance(t)) {
                    throw t;
                }
            }
            if (caughtException instanceof InvocationTargetException) {
                handleRemoteExceptions(serviceInterface, flavor, method.getName(), t);
            }
            throw new ServiceInvokeException(serviceInterface, flavor, method.getName(), t);
        }
        return result;
    }

    protected abstract Object invoke(String serviceName, Class serviceInterface, String flavor, Object serviceObject, Method method, Object[] args) throws Throwable;

    private Class[] getBusinessExceptions(Class serviceInterface, Method method) throws ServiceException {
        try {
            method = serviceInterface.getMethod(method.getName(), method.getParameterTypes());
        } catch (NoSuchMethodException e) {
            log.error("internal error");
            throw new ServiceException("proxy method not found in service interface, " + serviceInterface + ", method " + method.getName(), e);
        }
        return method.getExceptionTypes();
    }

    /** 
     * Recursivly swallows all RemoteException wrapper objects. The "remoting" aspect should be totally transparent
     * for the caller. All the caller should receive is the root cause exception. 
     * 
     * @param serviceInterface Class object of the service interface
     * @param flavor Flavor of the service
     * @param methodName Name of the invoked service method
     * @param targetException Caught exception
     * 
     * @throws ServiceException Exception that carries the root cause exception object
     */
    protected void handleRemoteExceptions(Class serviceInterface, String flavor, String methodName, Throwable targetException) throws ServiceException {
        if (targetException instanceof RemoteException) {
            log.debug("remote exception caught, extracting cause ...");
            handleRemoteExceptions(serviceInterface, flavor, methodName, targetException.getCause());
        } else if (targetException instanceof ServiceException) {
            throw (ServiceException) targetException;
        } else {
            throw new ServiceInvokeException(serviceInterface, flavor, methodName, targetException);
        }
    }

    protected Object getProperty(Class serviceInterface, String flavor, String key, Object defaultValue) {
        Object value = null;
        ConfigProperties properties = getProperties(serviceInterface, flavor);
        if (properties != null) {
            value = properties.getProperty(key, defaultValue);
        }
        if (value instanceof String) {
            String serviceName = null;
            ServiceConfig serviceConfig = getServiceConfig(serviceInterface);
            if (serviceConfig == null) {
                serviceName = ClassUtils.getShortName(serviceInterface);
            } else {
                serviceName = serviceConfig.getName();
            }
            Map substMap = new HashMap();
            substMap.put("${" + PROPERTY_SERVICE_NAME + "}", serviceName);
            value = ClassUtils.substClassProperties(PROPERTIES_META_SUBST_PREFIX, serviceInterface, (String) value, substMap);
        }
        if (value == null) {
            value = defaultValue;
        }
        return value;
    }

    protected Object getProperty(Class serviceInterface, String flavor, String key) {
        return getProperty(serviceInterface, flavor, key, null);
    }

    protected ServiceConfig getServiceConfig(Class serviceInterface) {
        return getServiceFactory().getServiceConfig(serviceInterface);
    }

    private ConfigProperties getProperties(Class serviceInterface, String flavor) {
        ConfigProperties properties = null;
        FlavorConfig flavorConfig = getServiceFactory().getFlavorConfig(serviceInterface, flavor);
        if (flavorConfig != null) {
            properties = flavorConfig.getInvocationHandlerProperties();
        }
        if (properties == null) {
            InvocationHandlerConfig defaultInvocationHandlerConfig = getServiceFactory().getDefaultInvocationHandlerConfig();
            if (defaultInvocationHandlerConfig != null) {
                properties = defaultInvocationHandlerConfig.getProperties();
            }
        }
        return properties;
    }

    private ServiceFactoryInternal getServiceFactory() throws ApplicationContextException {
        return (ServiceFactoryInternal) serviceContext.getServiceFactory();
    }

    public String toString() {
        return new ToStringBuilder(this).append("name", name).toString();
    }
}
