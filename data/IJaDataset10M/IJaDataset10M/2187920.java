package com.liferay.portal.ejb;

import com.dotmarketing.business.Cachable;
import com.dotmarketing.business.CacheLocator;
import com.dotmarketing.business.DotCacheAdministrator;
import com.dotmarketing.business.DotCacheException;
import com.dotmarketing.util.Logger;
import com.liferay.portal.model.PasswordTracker;
import com.liferay.util.Validator;

/**
 * <a href="PasswordTrackerPool.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.8 $
 *
 */
public class PasswordTrackerPool implements Cachable {

    public static void clear() {
        _getInstance()._clear();
    }

    public static PasswordTracker get(String passwordTrackerId) {
        return _getInstance()._get(passwordTrackerId);
    }

    public static PasswordTracker put(String passwordTrackerId, PasswordTracker obj) {
        return _getInstance()._put(passwordTrackerId, obj);
    }

    public static PasswordTracker remove(String passwordTrackerId) {
        return _getInstance()._remove(passwordTrackerId);
    }

    private static PasswordTrackerPool _getInstance() {
        if (_instance == null) {
            synchronized (PasswordTrackerPool.class) {
                if (_instance == null) {
                    _instance = new PasswordTrackerPool();
                }
            }
        }
        return _instance;
    }

    private PasswordTrackerPool() {
        _cacheable = PasswordTracker.CACHEABLE;
        _cache = CacheLocator.getCacheAdministrator();
    }

    private void _clear() {
        _cache.flushGroup(primaryGroup);
    }

    private PasswordTracker _get(String passwordTrackerId) {
        if (!_cacheable) {
            return null;
        } else if (passwordTrackerId == null) {
            return null;
        } else {
            PasswordTracker obj = null;
            String key = passwordTrackerId.toString();
            if (Validator.isNull(key)) {
                return null;
            }
            try {
                obj = (PasswordTracker) _cache.get(key, primaryGroup);
            } catch (DotCacheException e) {
                Logger.debug(this, "Cache Entry not found", e);
            }
            return obj;
        }
    }

    private PasswordTracker _put(String passwordTrackerId, PasswordTracker obj) {
        if (!_cacheable) {
            return obj;
        } else if (passwordTrackerId == null) {
            return obj;
        } else {
            String key = passwordTrackerId.toString();
            if (Validator.isNotNull(key)) {
                _cache.remove(key, primaryGroup);
                _cache.put(key, obj, primaryGroup);
            }
            return obj;
        }
    }

    private PasswordTracker _remove(String passwordTrackerId) {
        if (!_cacheable) {
            return null;
        } else if (passwordTrackerId == null) {
            return null;
        } else {
            PasswordTracker obj = null;
            String key = passwordTrackerId.toString();
            if (Validator.isNull(key)) {
                return null;
            }
            try {
                obj = (PasswordTracker) _cache.get(key, primaryGroup);
            } catch (DotCacheException e) {
                Logger.debug(this, "Cache Entry not found", e);
            }
            _cache.remove(key, primaryGroup);
            return obj;
        }
    }

    private static String primaryGroup = "PasswordTrackerPool";

    private static String[] groupNames = { primaryGroup };

    public String getPrimaryGroup() {
        return primaryGroup;
    }

    public String[] getGroups() {
        return groupNames;
    }

    private DotCacheAdministrator _cache = CacheLocator.getCacheAdministrator();

    private static PasswordTrackerPool _instance;

    private boolean _cacheable;
}
