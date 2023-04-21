package org.ikasan.framework.module.container;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.ikasan.framework.module.Module;

/**
 * Default implementation for Module Container 
 * 
 * @author Ikasan Development Team
 */
public class ModuleContainerImpl implements ModuleContainer {

    /** Map of all loaded modules */
    protected Map<String, Module> modules = new LinkedHashMap<String, Module>();

    /**
     * Exposes all the loaded <code>Module</code>s
     * 
     * @return List of all loaded <code>Module</code>s
     */
    public List<Module> getModules() {
        return new ArrayList<Module>(modules.values());
    }

    /**
     * Returns a module by name
     * 
     * @param moduleName - The name of the module to get
     * @return Module
     */
    public Module getModule(String moduleName) {
        return modules.get(moduleName);
    }

    public void add(Module module) {
        modules.put(module.getName(), module);
    }
}
