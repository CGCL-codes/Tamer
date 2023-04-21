package org.apache.axis2.engine;

import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.context.ServiceContext;
import org.apache.axis2.context.ServiceGroupContext;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.AxisServiceGroup;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.service.Lifecycle;
import org.apache.axis2.util.Loader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

/**
 * If the service implementation has an init method with 1 or 2 message context as its parameters, then
 * the DependencyManager calls the init method with appropriate parameters.
 */
public class DependencyManager {

    private static final Log log = LogFactory.getLog(DependencyManager.class);

    public static final String SERVICE_INIT_METHOD = "init";

    public static final String SERVICE_DESTROY_METHOD = "destroy";

    /**
     * Initialize a new service object.  Essentially, check to see if the object wants to receive
     * an init() call - if so, call it.
     *
     * @param obj the service object
     * @param serviceContext the active ServiceContext
     * @throws AxisFault if there's a problem initializing
     * 
     * @deprecated please use initServiceObject()
     */
    public static void initServiceClass(Object obj, ServiceContext serviceContext) throws AxisFault {
        initServiceObject(obj, serviceContext);
    }

    /**
     * Initialize a new service object.  Essentially, check to see if the object wants to receive
     * an init() call - if so, call it.
     *
     * @param obj the service object
     * @param serviceContext the active ServiceContext
     * @throws AxisFault if there's a problem initializing
     */
    public static void initServiceObject(Object obj, ServiceContext serviceContext) throws AxisFault {
        if (obj instanceof Lifecycle) {
            ((Lifecycle) obj).init(serviceContext);
            return;
        }
        Class classToLoad = obj.getClass();
        Method method = null;
        try {
            method = classToLoad.getMethod(SERVICE_INIT_METHOD, new Class[] { ServiceContext.class });
        } catch (Exception e) {
        }
        if (method != null) {
            try {
                method.invoke(obj, new Object[] { serviceContext });
            } catch (IllegalAccessException e) {
                log.info("Exception trying to call " + SERVICE_INIT_METHOD, e);
            } catch (IllegalArgumentException e) {
                log.info("Exception trying to call " + SERVICE_INIT_METHOD, e);
            } catch (InvocationTargetException e) {
                log.info("Exception trying to call " + SERVICE_INIT_METHOD, e);
            }
        }
    }

    /**
     * To init all the services in application scope
     *
     * @param serviceGroupContext the ServiceGroupContext from which to extract all the services
     * @throws AxisFault if there's a problem initializing
     */
    public static void initService(ServiceGroupContext serviceGroupContext) throws AxisFault {
        AxisServiceGroup serviceGroup = serviceGroupContext.getDescription();
        Iterator<AxisService> serviceItr = serviceGroup.getServices();
        while (serviceItr.hasNext()) {
            AxisService axisService = (AxisService) serviceItr.next();
            ServiceContext serviceContext = serviceGroupContext.getServiceContext(axisService);
            AxisService service = serviceContext.getAxisService();
            ClassLoader classLoader = service.getClassLoader();
            Parameter implInfoParam = service.getParameter(Constants.SERVICE_CLASS);
            if (implInfoParam != null) {
                try {
                    Class implClass = Loader.loadClass(classLoader, ((String) implInfoParam.getValue()).trim());
                    Object serviceImpl = implClass.newInstance();
                    serviceContext.setProperty(ServiceContext.SERVICE_OBJECT, serviceImpl);
                    initServiceObject(serviceImpl, serviceContext);
                } catch (Exception e) {
                    AxisFault.makeFault(e);
                }
            }
        }
    }

    /**
     * Notify a service object that it's on death row.
     * @param serviceContext the active ServiceContext
     */
    public static void destroyServiceObject(ServiceContext serviceContext) {
        Object obj = serviceContext.getProperty(ServiceContext.SERVICE_OBJECT);
        if (obj != null) {
            if (obj instanceof Lifecycle) {
                ((Lifecycle) obj).destroy(serviceContext);
                return;
            }
            Class classToLoad = obj.getClass();
            Method method = null;
            try {
                method = classToLoad.getMethod(SERVICE_DESTROY_METHOD, new Class[] { ServiceContext.class });
            } catch (NoSuchMethodException e) {
            }
            if (method != null) {
                try {
                    method.invoke(obj, new Object[] { serviceContext });
                } catch (IllegalAccessException e) {
                    log.info("Exception trying to call " + SERVICE_DESTROY_METHOD, e);
                } catch (InvocationTargetException e) {
                    log.info("Exception trying to call " + SERVICE_DESTROY_METHOD, e);
                }
            }
        }
    }
}
