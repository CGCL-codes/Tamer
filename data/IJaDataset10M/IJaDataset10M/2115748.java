package com.cosmos.acacia.service;

import com.cosmos.impl.acacia.service.ServiceManagerImpl;

/**
 *
 * @author Miro
 */
public class ServiceManager {

    private static ServiceManagerImpl instance;

    public static ServiceManagerImpl getInstance() {
        if (instance == null) {
            instance = new ServiceManagerImpl();
        }
        return instance;
    }

    public static void removeInstance() {
        instance = null;
    }

    public static Object getService(String serviceName) {
        return getInstance().getService(serviceName);
    }

    public static <T> T getRemoteService(Class<T> serviceInterface) {
        return getInstance().getRemoteService(serviceInterface);
    }

    public static <T> T getRemoteService(Class<T> serviceInterface, boolean checkPermissions) {
        ServiceManagerImpl serviceManagerImpl = getInstance();
        return serviceManagerImpl.getRemoteService(serviceInterface, checkPermissions);
    }
}
