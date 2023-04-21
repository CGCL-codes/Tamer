package com.intel.gpe.client2.common.gpe4gtk;

import com.intel.gpe.client2.common.configurators.SecurityManagerConfigurator;
import com.intel.gpe.client2.common.gpe4gtk.security.NonsecureManager;
import com.intel.gpe.client2.security.GPESecurityManager;

/**
 * 
 * @author Alexander Lukichev
 * @version $Id: NonsecureManagerConfiguratorImpl.java,v 1.1 2006/04/17 07:15:18 lukichev Exp $
 *
 */
public class NonsecureManagerConfiguratorImpl extends SecurityManagerConfigurator {

    public GPESecurityManager getSecurityManager() {
        return new NonsecureManager();
    }
}
