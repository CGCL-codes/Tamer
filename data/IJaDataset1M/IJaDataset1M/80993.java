package com.objectparadigms.framework.servicefactory;

import java.util.HashMap;
import java.util.Map;
import com.objectparadigms.framework.exception.SystemException;
import com.objectparadigms.framework.servicefactory.config.ServiceConfig;
import com.objectparadigms.framework.servicefactory.config.ServiceFactoryConfig;

/**
 * <a href="ServiceFactory.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Michael C. Han
 * @version $Revision: 1.4 $
 *
 */
public class ServiceFactory {

    public static final String LOG_CATEGORY = ServiceFactory.class.getName();

    private static ServiceFactory instance;

    private Map instanceCache;

    private ServiceFactoryConfig configs;

    private ServiceFactory() {
        configs = ServiceFactoryConfig.getInstance();
        instanceCache = new HashMap();
    }

    public static synchronized ServiceFactory getInstance() {
        if (instance == null) {
            instance = new ServiceFactory();
        }
        return instance;
    }

    public Object getService(String serviceName) throws SystemException {
        ServiceConfig config = configs.get(serviceName);
        Object service = null;
        if (config.isCached()) {
            synchronized (instanceCache) {
                service = instanceCache.get(serviceName);
                if (service == null) {
                    service = _createService(config);
                    service = instanceCache.put(serviceName, service);
                }
                return service;
            }
        }
        return _createService(config);
    }

    private Object _createService(ServiceConfig config) throws SystemException {
        try {
            return config.getImpl().newInstance();
        } catch (Exception e) {
            throw new SystemException("Unable to instantiate service.", e);
        }
    }
}
