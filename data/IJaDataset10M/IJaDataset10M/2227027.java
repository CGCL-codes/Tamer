package com.intel.gpe.client2.common.configurators;

import com.intel.gpe.client2.security.GPESecurityManager;

/**
 * 
 * The security manager configurator. See {@link com.intel.gpe.client2.security.GPESecurityManager}
 * for the list of security manager operations.
 * 
 * @author Alexander Lukichev
 * @version $Id: SecurityManagerConfigurator.java,v 1.4 2006/10/19 13:40:54 dizhigul Exp $
 *
 */
public abstract class SecurityManagerConfigurator extends BaseConfigurator {

    /**
     * Get the configured security manager.
     * 
     * @return the security manager
     */
    public abstract GPESecurityManager getSecurityManager();

    /**
     * Load the configurator from the class specified in the System property
     * "com.intel.gpe.client2.common.configurators.SecurityManagerConfigurator"
     * 
     * @return the security configurator
     * 
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    public static SecurityManagerConfigurator getConfigurator() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return load(SecurityManagerConfigurator.class.getName(), "com.intel.gpe.client2.common.gpe4gtk.SecurityManagerConfiguratorImpl");
    }
}
