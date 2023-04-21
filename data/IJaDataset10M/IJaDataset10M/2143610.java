package fi.vtt.noen.mfw.osmo;

import fi.vtt.noen.mfw.bundle.common.BaseActivator;
import fi.vtt.noen.mfw.bundle.common.Logger;
import fi.vtt.noen.mfw.bundle.server.plugins.persistence.PersistencePlugin;
import fi.vtt.noen.mfw.bundle.server.plugins.persistence.PersistencePluginImpl;
import org.osgi.framework.BundleContext;

/**
 * @author Teemu Kanstren
 */
public class InMemoryPersistencePluginActivator extends BaseActivator {

    private InMemoryPersistencePlugin persistence;

    public InMemoryPersistencePluginActivator() {
        super(null);
    }

    public InMemoryPersistencePlugin getPersistence() {
        return persistence;
    }

    @Override
    public void start(BundleContext bc) throws Exception {
        persistence = new InMemoryPersistencePlugin();
        registerPlugin(bc, persistence, null, PersistencePlugin.class.getName());
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
    }
}
