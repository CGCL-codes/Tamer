package org.impalaframework.module.loader;

import java.util.Map;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.spi.ModuleLoader;
import org.impalaframework.registry.Registry;
import org.impalaframework.registry.RegistrySupport;
import org.springframework.util.Assert;

/**
 * Holds a mapping of {@link ModuleLoader} instances to module types, as
 * determined using the {@link ModuleDefinition#getType()} call.
 * 
 * @author Phil Zoio
 */
public class ModuleLoaderRegistry extends RegistrySupport implements Registry<ModuleLoader> {

    public ModuleLoader getModuleLoader(String key) {
        return getModuleLoader(key, true);
    }

    public ModuleLoader getModuleLoader(String key, boolean failIfNotFound) {
        return super.getEntry(key, ModuleLoader.class, failIfNotFound);
    }

    public void addItem(String key, ModuleLoader moduleLoader) {
        super.addRegistryItem(key, moduleLoader);
    }

    public boolean hasModuleLoader(String key) {
        Assert.notNull(key, "type cannot be null");
        return (getModuleLoader(key, false) != null);
    }

    public void setModuleLoaders(Map<String, ModuleLoader> moduleLoaders) {
        super.setEntries(moduleLoaders);
    }
}
