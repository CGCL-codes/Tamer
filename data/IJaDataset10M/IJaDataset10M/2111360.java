package purej.cache;

import java.util.Properties;
import purej.logging.Logger;
import purej.logging.LoggerFactory;

/**
 * ĳ���Ŵ���
 * @author leesangboo
 *
 */
public class CacheManager {

    private static final Logger log = LoggerFactory.getLogger(CacheManager.class, Logger.FRAMEWORK);

    private CacheAdministrator admin;

    private Properties prop;

    private static final int DEFAULT_REFRESH_PERIOD = 1000;

    public CacheManager() {
        admin = new CacheAdministrator();
    }

    public CacheManager(Properties p) {
        admin = new CacheAdministrator(p);
    }

    public CacheManager(String id) {
        loadPropsFromConfigXML(id);
        admin = new CacheAdministrator(prop);
    }

    private void loadPropsFromConfigXML(String id) {
        try {
        } catch (Exception e) {
            log.error("Failed load cache prorperties.");
        }
    }

    /**
     * ĳ������ ��ȯ�Ѵ�.
     * 
     * @param key
     * @return
     */
    public Object getFromCache(String key) {
        return getFromCache(key, true);
    }

    /**
     * 
     * ĳ������ ��ȯ�Ѵ�.
     * 
     * @param key
     * @param isLog
     * @return
     */
    public Object getFromCache(String key, boolean isLog) {
        if (isLog) log.info("Cache manager return  [" + key + "] from cache.");
        return getFromCache(key, DEFAULT_REFRESH_PERIOD);
    }

    /**
     * ĳ������ ��ȯ�Ѵ�.
     * 
     * @param key
     * @param refreshPeriod
     * @return
     */
    public Object getFromCache(String key, int refreshPeriod) {
        Object value;
        try {
            value = admin.getFromCache(key, refreshPeriod);
        } catch (purej.cache.base.NeedsRefreshException nre) {
            try {
                value = null;
                admin.putInCache(key, value);
            } catch (Exception ex) {
                log.error("Cache manager return cached value failed : key [" + key + "]");
                value = nre.getCacheContent();
                admin.cancelUpdate(key);
            }
        }
        return value;
    }

    /**
     * ĳ���� ���� ����
     * 
     * @param key
     * @param value
     */
    public void putInCache(String key, Object value) {
        admin.putInCache(key, value);
        log.info("Cache manager is caching : key [" + key + "]");
    }

    /**
     * �̹� Ű�� ���� ĳ���Ǿ� �ִ��� ��ȯ
     * 
     * @param key
     * @return
     */
    public boolean isCached(String key) {
        if (getFromCache(key, false) != null) {
            return true;
        } else {
            return false;
        }
    }

    public void flushCache() {
        admin.flushAll();
    }

    public CacheAdministrator getCacheAdministrator() {
        return admin;
    }
}
