package org.regilo.core.configuration;

public interface IConfigurationService {

    public ConfigurationElement getConfigurationElement(String scope, String property);

    public boolean hasScope(String scope);
}
