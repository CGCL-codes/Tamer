package org.opencms.db;

import org.opencms.file.CmsGroup;
import org.opencms.file.CmsResource;
import org.opencms.file.CmsUser;
import org.opencms.security.CmsPermissionSet;

/**
 * Describes the cache key generating methods.<p>
 * 
 * @author Carsten Weinholz 
 * 
 * @version $Revision: 1.12 $
 * 
 * @since 6.0.0
 */
public interface I_CmsCacheKey {

    /**
     * Returns the cache key for the group users cache.<p>
     * 
     * @param prefix to distinguish keys additionally
     * @param context the context
     * @param group the group
     * 
     * @return a cache key that is unique for the set of parameters
     */
    String getCacheKeyForGroupUsers(String prefix, CmsDbContext context, CmsGroup group);

    /**
     * Returns the cache key for the user groups cache.<p>
     * 
     * @param prefix to distinguish keys additionally
     * @param context the context
     * @param user the user
     * 
     * @return a cache key that is unique for the set of parameters
     */
    String getCacheKeyForUserGroups(String prefix, CmsDbContext context, CmsUser user);

    /**
     * Returns the cache key for the permission cache.<p>
     * 
     * @param prefix to distinguish keys additionally
     * @param context the context
     * @param resource the resource
     * @param requiredPermissions the permissions to check
     * 
     * @return a cache key that is unique for the set of parameters
     */
    String getCacheKeyForUserPermissions(String prefix, CmsDbContext context, CmsResource resource, CmsPermissionSet requiredPermissions);
}
