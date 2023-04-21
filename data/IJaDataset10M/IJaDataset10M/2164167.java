package org.scopemvc.util.security;

import org.scopemvc.core.ResourceProvider;
import org.scopemvc.core.security.Permissions;

/**
 * Applications that don't need permission validation can use this
 * implementation. It grants all permission checks.
 *
 * @author Patrik Nordwall
 * @version $Revision: 1.4 $
 * @created December 3, 2002
 */
public class GrantAllPermissionResources implements Permissions {

    /**
     * Set the resource provider owning this resource
     *
     * @param inProvider The resource provider
     */
    public void setResourceProvider(ResourceProvider inProvider) {
    }

    /**
     * @param resourceName TODO: Describe the Parameter
     * @return always true
     */
    public boolean checkPermission(String resourceName) {
        return true;
    }
}
