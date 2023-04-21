package de.hpi.eworld.core.extensionpoints;

import org.apache.log4j.Logger;
import org.java.plugin.Plugin;
import org.java.plugin.PluginLifecycleException;
import org.java.plugin.PluginManager;

/**
 * Superclass for some kind of Provider-class implementing a *Provider
 * interface.
 * 
 * @author anton.gulenko
 */
public abstract class AbstractProvider<T extends Plugin> {

    private final String id;

    public AbstractProvider(String id) {
        this.id = id;
    }

    @SuppressWarnings("unchecked")
    protected T getPlugin() {
        T plugin = null;
        try {
            plugin = (T) PluginManager.lookup(this).getPlugin(id);
        } catch (PluginLifecycleException e) {
            Logger.getLogger(getClass()).error("Error while initializing provider for plugin with id: " + id, e);
        }
        return plugin;
    }
}
