package com.angel.architecture.services;

import java.util.Collection;
import com.angel.architecture.persistence.beans.ConfigurationParameter;
import com.angel.architecture.services.interfaces.GenericService;

/**
 *
 * @author William
 */
public interface InitializerConfigurationService extends GenericService {

    public boolean isInitialized();

    public Collection<ConfigurationParameter> initializeConfiguration();

    public Collection<ConfigurationParameter> reinitializeConfiguration();
}
