package org.icenigrid.gridsam.core.support;

import org.apache.hivemind.Registry;
import org.icenigrid.gridsam.core.plugin.manager.ResourceRegistry;

/**
 * <strong>Purpose:</strong><br>
 * 
 *
 * @version 1.0.1 2008-7-10<br>
 * @author Liu Jie, Yongqiang Zou<br>
 *
 */
public class InitRegistry {

    public static Registry oRegistry;

    public static Registry init(String[] args) {
        oRegistry = new ResourceRegistry(args);
        return oRegistry;
    }

    /**
	 * @return the oRegistry
	 */
    public static Registry getRegistry() {
        return oRegistry;
    }

    /**
	 * @param registry the oRegistry to set
	 */
    public static void setRegistry(Registry registry) {
        oRegistry = registry;
    }
}
