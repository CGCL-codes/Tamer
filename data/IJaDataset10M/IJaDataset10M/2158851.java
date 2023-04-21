package com.volantis.osgi.cm;

import org.osgi.framework.Bundle;
import org.osgi.service.cm.ConfigurationPermission;

/**
 * Provides helper methods for checking security.
 */
public class SecurityHelper {

    /**
     * The permission to check.
     */
    public static final ConfigurationPermission CONFIGURATION_PERMISSION = new ConfigurationPermission("*", ConfigurationPermission.CONFIGURE);

    /**
     * Check to see if the specified bundle has permission to configure other
     * bundles.
     *
     * @param bundle The bundle to check.
     * @return True if it has, false otherwise.
     */
    public static boolean hasConfigurePermission(Bundle bundle) {
        return bundle.hasPermission(CONFIGURATION_PERMISSION);
    }

    /**
     * Ensure that the bundle has permission to configure other bundles.
     *
     * @param bundle The bundle to check.
     * @throws SecurityException If the bundle does not have permission.
     */
    public static void ensureHasConfigurePermission(Bundle bundle) {
        if (!hasConfigurePermission(bundle)) {
            throw new SecurityException("Bundle '" + bundle.getLocation() + "' does not have permission to manage " + "configuration for other bundles");
        }
    }
}
