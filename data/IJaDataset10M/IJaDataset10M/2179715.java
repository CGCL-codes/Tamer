package com.google.gdt.eclipse.gph.install;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import java.util.Dictionary;

/**
 * A static utility class for bundle utilities.
 */
public class BundleUtilities {

    /**
   * @return whether the given bundle has all of its dependencies satisfied
   *         (including optional dependencies)
   */
    public static boolean areBundlesDependenciesSatisfied(String bundleId) {
        Bundle bundle = Platform.getBundle(bundleId);
        if (bundle == null) {
            return false;
        }
        @SuppressWarnings("unchecked") Dictionary<String, String> headers = bundle.getHeaders();
        String requiredBundles[] = headers.get("Require-Bundle").split(",");
        for (String requiredBundleId : requiredBundles) {
            String id = requiredBundleId.split(";")[0];
            if (!isBundleInstalled(id)) {
                return false;
            }
        }
        return true;
    }

    /**
   * @return whether all the given bundles are installed
   */
    public static boolean areBundlesInstalled(String[] bundleIds) {
        for (String bundleId : bundleIds) {
            if (!isBundleInstalled(bundleId)) {
                return false;
            }
        }
        return true;
    }

    /**
   * @return whether the given bundle is installed
   */
    public static boolean isBundleInstalled(String bundleId) {
        Bundle bundle = Platform.getBundle(bundleId);
        return bundle != null;
    }

    private BundleUtilities() {
    }
}
