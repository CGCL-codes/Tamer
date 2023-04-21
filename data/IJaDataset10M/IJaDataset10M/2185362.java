package org.regilo.core.configuration;

import org.eclipse.ui.services.IServiceLocator;

public class ConfigurationServiceFactory extends org.eclipse.ui.services.AbstractServiceFactory {

    public ConfigurationServiceFactory() {
    }

    @Override
    public Object create(Class serviceInterface, IServiceLocator parentLocator, IServiceLocator locator) {
        Object parentService = parentLocator.getService(IConfigurationService.class);
        if (parentService == null) {
            return new ConfigurationService(locator);
        }
        return null;
    }
}
