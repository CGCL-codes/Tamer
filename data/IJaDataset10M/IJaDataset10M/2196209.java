package org.jmonit.spi;

/**
 * Components created by jMonit and implementing this interface will get
 * "injected" with the current pluginManager.
 * 
 * @author <a href="mailto:nicolas.deloof@gmail.com">Nicolas De Loof</a>
 */
public interface PluginManagerAware {

    /**
     * Inject the active plugin manager
     * 
     * @param manager the active plugin manager
     */
    void setPluginManager(PluginManager manager);
}
