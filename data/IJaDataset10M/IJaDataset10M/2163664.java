package org.limmen.crs.api;

import java.util.Iterator;
import java.util.ServiceLoader;

public class ConfigurationServiceFactory {

    private static ConfigurationServiceFactory current = new ConfigurationServiceFactory();

    public static ConfigurationServiceFactory getInstance() {
        return current;
    }

    private final ConfigurationManagementService configurationManagementService;

    private final ConfigurationService configurationService;

    private ConfigurationServiceFactory() {
        super();
        ServiceLoader<ConfigurationService> services = ServiceLoader.load(ConfigurationService.class);
        Iterator<ConfigurationService> servicesIterator = services.iterator();
        configurationService = servicesIterator.next();
        if (!(configurationService instanceof ConfigurationManagementService)) {
            ServiceLoader<ConfigurationManagementService> managementServices = ServiceLoader.load(ConfigurationManagementService.class);
            Iterator<ConfigurationManagementService> managementServicesIterator = managementServices.iterator();
            configurationManagementService = managementServicesIterator.next();
        } else {
            configurationManagementService = (ConfigurationManagementService) configurationService;
        }
    }

    public ConfigurationManagementService getConfigurationManagementService() {
        return configurationManagementService;
    }

    public ConfigurationService getConfigurationService() {
        return configurationService;
    }
}
