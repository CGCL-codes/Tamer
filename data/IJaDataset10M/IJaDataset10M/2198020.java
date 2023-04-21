package net.medienablage.service.impl;

import net.medienablage.service.IMedienService;

public class MedienServiceFactory {

    public static IMedienService service;

    public static final int CLIENT_VERSION = 0;

    private static final int SERVER_VERSION = 1;

    private static int version = CLIENT_VERSION;

    public static IMedienService getServiceInstance() {
        if (service == null) {
            switch(version) {
                case CLIENT_VERSION:
                    return service = new MedienServiceImpl();
                case SERVER_VERSION:
            }
        }
        return service;
    }

    public static IMedienService getService(String serviceClass) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class<IMedienService> classOfService = (Class<IMedienService>) Class.forName(serviceClass);
        return classOfService.newInstance();
    }

    public static int getVersion() {
        return version;
    }

    public static void setVersion(int version) {
        version = version;
    }
}
