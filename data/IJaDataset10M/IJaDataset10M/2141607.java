package net.sf.f2s.service;

import net.sf.f2s.service.config.ServiceConfiguration;

/**
 * This is an abstract {@link Service} implementation.
 * 
 * @author Rogiel
 * @version 1.0
 * @param <T>
 *            The {@link ServiceConfiguration} <b>interface</b> type used by the
 *            {@link Service}. Note that this <b>must</b> be an interface!s
 * @see ServiceConfiguration ServiceConfiguration for details on the configuration interface.
 */
public abstract class AbstractService<T extends ServiceConfiguration> implements Service {

    protected final T configuration;

    protected AbstractService(T configuration) {
        this.configuration = configuration;
    }

    @Override
    public T getServiceConfiguration() {
        return configuration;
    }
}
