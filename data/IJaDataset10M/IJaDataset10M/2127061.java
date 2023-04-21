package com.heliofrota;

import org.mentawai.core.MultiApplicationManager;

/**
 * MultiApplicationManager class.
 * @author Helio Frota
 *
 */
public class AppManager extends MultiApplicationManager {

    /**
     * {@inheritDoc}
     */
    public void registerManagers() {
        register(ConfigManager.class);
        register(MscrumManager.class);
    }
}
