package org.springmodules.cache.provider.jcs;

import org.apache.commons.lang.StringUtils;
import org.springmodules.cache.provider.CacheProfileValidator;

/**
 * <p>
 * Validates the properties of a <code>{@link JcsProfile}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 458 $ $Date: 2006-02-23 23:52:57 -0500 (Thu, 23 Feb 2006) $
 */
public class JcsProfileValidator implements CacheProfileValidator {

    /**
   * Constructor.
   */
    public JcsProfileValidator() {
        super();
    }

    /**
   * Validates the specified cache name.
   * 
   * @param cacheName
   *          the cache name to validate.
   * 
   * @throws IllegalArgumentException
   *           if the cache name is empty or <code>null</code>.
   */
    protected final void validateCacheName(String cacheName) {
        if (StringUtils.isEmpty(cacheName)) {
            throw new IllegalArgumentException("Cache name should not be empty");
        }
    }

    /**
   * @see CacheProfileValidator#validateCacheProfile(Object)
   * @see #validateCacheName(String)
   * 
   * @throws IllegalArgumentException
   *           if the cache profile is not an instance of
   *           <code>JcsProfile</code>.
   */
    public final void validateCacheProfile(Object cacheProfile) {
        if (cacheProfile instanceof JcsProfile) {
            JcsProfile jcsCacheProfile = (JcsProfile) cacheProfile;
            String cacheName = jcsCacheProfile.getCacheName();
            this.validateCacheName(cacheName);
        } else {
            throw new IllegalArgumentException("The cache profile should be an instance of 'JcsProfile'");
        }
    }
}
