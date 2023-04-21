package org.npsnet.v.services.system;

import org.npsnet.v.kernel.Module;
import org.npsnet.v.kernel.ServiceProvider;

/**
 * The module upgrade service.
 *
 * @author Andrzej Kapolka
 */
public interface ModuleUpgradeProvider extends ServiceProvider {

    /**
     * Attempts to upgrade the specified module.
     *
     * @param module the module to upgrade
     * @return if the module was successfully upgraded, the new module;
     * otherwise, the module that was passed to the method
     */
    public Module attemptToUpgrade(Module module);
}
