package com.liferay.portal.ejb;

import com.dotmarketing.business.Cachable;
import com.dotmarketing.business.CacheLocator;
import com.dotmarketing.business.DotCacheAdministrator;
import com.dotmarketing.business.DotCacheException;
import com.dotmarketing.util.Logger;
import com.liferay.portal.model.Company;
import com.liferay.util.Validator;

/**
 * <a href="CompanyPool.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.61 $
 *
 */
public class CompanyPool implements Cachable {

    public static void clear() {
        _getInstance()._clear();
    }

    public static Company get(String companyId) {
        return _getInstance()._get(companyId);
    }

    public static Company put(String companyId, Company obj) {
        return _getInstance()._put(companyId, obj);
    }

    public static Company remove(String companyId) {
        return _getInstance()._remove(companyId);
    }

    private static CompanyPool _getInstance() {
        if (_instance == null) {
            synchronized (CompanyPool.class) {
                if (_instance == null) {
                    _instance = new CompanyPool();
                }
            }
        }
        return _instance;
    }

    private CompanyPool() {
        _cacheable = Company.CACHEABLE;
        _cache = CacheLocator.getCacheAdministrator();
    }

    private void _clear() {
        _cache.flushGroup(primaryGroup);
    }

    private Company _get(String companyId) {
        if (!_cacheable) {
            return null;
        } else if (companyId == null) {
            return null;
        } else {
            Company obj = null;
            String key = companyId.toString();
            if (Validator.isNull(key)) {
                return null;
            }
            try {
                obj = (Company) _cache.get(key, primaryGroup);
            } catch (DotCacheException e) {
                Logger.debug(this, "Cache Entry not found", e);
            }
            return obj;
        }
    }

    private Company _put(String companyId, Company obj) {
        if (!_cacheable) {
            return obj;
        } else if (companyId == null) {
            return obj;
        } else {
            String key = companyId.toString();
            if (Validator.isNotNull(key)) {
                _cache.remove(key, primaryGroup);
                _cache.put(key, obj, primaryGroup);
            }
            return obj;
        }
    }

    private Company _remove(String companyId) {
        if (!_cacheable) {
            return null;
        } else if (companyId == null) {
            return null;
        } else {
            Company obj = null;
            String key = companyId.toString();
            if (Validator.isNull(key)) {
                return null;
            }
            try {
                obj = (Company) _cache.get(key, primaryGroup);
            } catch (DotCacheException e) {
                Logger.debug(this, "Cache Entry not found", e);
            }
            _cache.remove(key, primaryGroup);
            return obj;
        }
    }

    private static CompanyPool _instance;

    private static String primaryGroup = "CompanyPool";

    private static String[] groupNames = { primaryGroup };

    public String getPrimaryGroup() {
        return primaryGroup;
    }

    public String[] getGroups() {
        return groupNames;
    }

    private DotCacheAdministrator _cache = CacheLocator.getCacheAdministrator();

    private boolean _cacheable;
}
