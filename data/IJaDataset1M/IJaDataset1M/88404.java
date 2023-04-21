package com.dotmarketing.portlets.contentlet.business;

import com.dotmarketing.business.CacheLocator;
import com.dotmarketing.business.DotCacheAdministrator;
import com.dotmarketing.business.DotCacheException;
import com.dotmarketing.util.Logger;

/**
 * @author Jason Tesser
 * @since 1.6
 */
public class ContentletCacheImpl extends ContentletCache {

    private DotCacheAdministrator cache;

    private String primaryGroup = "ContentletCache";

    private String[] groupNames = { primaryGroup };

    public ContentletCacheImpl() {
        cache = CacheLocator.getCacheAdministrator();
    }

    @Override
    protected com.dotmarketing.portlets.contentlet.model.Contentlet add(String key, com.dotmarketing.portlets.contentlet.model.Contentlet content) {
        key = primaryGroup + key;
        try {
            if (cache.get(key, primaryGroup) != null) {
                cache.remove(key, primaryGroup);
            }
        } catch (DotCacheException e) {
            Logger.debug(this, "Cache Entry not found", e);
        }
        cache.put(key, content, primaryGroup);
        try {
            return (com.dotmarketing.portlets.contentlet.model.Contentlet) cache.get(key, primaryGroup);
        } catch (DotCacheException e) {
            Logger.warn(this, "Cache Entry not found after adding", e);
            return content;
        }
    }

    @Override
    protected com.dotmarketing.portlets.contentlet.model.Contentlet get(String key) {
        key = primaryGroup + key;
        com.dotmarketing.portlets.contentlet.model.Contentlet content = null;
        try {
            content = (com.dotmarketing.portlets.contentlet.model.Contentlet) cache.get(key, primaryGroup);
        } catch (DotCacheException e) {
            Logger.debug(this, "Cache Entry not found", e);
        }
        return content;
    }

    protected void clearCache() {
        cache.flushGroup(primaryGroup);
    }

    protected void remove(String key) {
        key = primaryGroup + key;
        try {
            if (cache.get(key, primaryGroup) != null) {
                cache.remove(key, primaryGroup);
            }
        } catch (com.dotmarketing.business.DotCacheException e) {
            Logger.debug(this, "Cache Entry not found", e);
        }
    }

    public String[] getGroups() {
        return groupNames;
    }

    public String getPrimaryGroup() {
        return primaryGroup;
    }
}
