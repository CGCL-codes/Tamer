package pl.xperios.rdk.server.common;

import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * Abstract class of @see pl.xperios.rdk.server.modules.ServerModule
 * @author Kosmo
 */
public class AbstractServerModule implements ServerModule {

    /**
     * Executed during Guice configuration. Allow to add custom bindings.
     * @param binder injection binder
     * @param config basic configuration
     */
    @Override
    public void configureGuice(Binder binder, ConfigurationService config) throws Exception {
    }

    /**
     * Executed during Guice configuration. Allow to add custom guice modules.
     * @param config basic configuration
     * @return guice module or null
     */
    @Override
    public Module[] configureGuice(ConfigurationService config) throws Exception {
        return null;
    }

    /**
     * Executed during ORM configuration. Allow to add custom ORM mappers or settings.
     * @param ormConfig ORM config
     * @param config basic configuration
     */
    @Override
    public void configureORM(OrmConfig ormConfig, ConfigurationService config) throws Exception {
    }

    /**
     * Executed during logger configuration. Allow to add custom loggers.
     * @param config basic configuration
     */
    @Override
    public void configureLogger(ConfigurationService config) throws Exception {
    }

    /**
     * Executed at the end of system startup. Allow to execute custom code.
     * @param injector application injector
     */
    @Override
    public void executeCustomCode(Injector injector) throws Exception {
    }
}
