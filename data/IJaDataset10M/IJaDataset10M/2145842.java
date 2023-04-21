package com.liusoft.dlog4j;

import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * DLOG4J�Ļ��������
 * @author Winter Lau
 */
public class DLOG_CacheManager {

    static final Log log = LogFactory.getLog(DLOG_CacheManager.class);

    public static CacheManager manager;

    static {
        try {
            manager = CacheManager.getInstance();
            if (manager == null) manager = CacheManager.create();
        } catch (CacheException e) {
            log.fatal("Initialize cache manager failed.", e);
        }
    }

    /**
	 * �ӻ����л�ȡ����
	 * @param cache_name
	 * @param key
	 * @return
	 */
    public static Serializable getObjectCached(String cache_name, Serializable key) {
        Cache cache = getCache(cache_name);
        if (cache != null) {
            try {
                Element elem = cache.get(key);
                if (elem != null && !cache.isExpired(elem)) return elem.getValue();
            } catch (Exception e) {
                log.error("Get cache(" + cache_name + ") of " + key + " failed.", e);
            }
        }
        return null;
    }

    /**
	 * �Ѷ�����뻺����
	 * @param cache_name
	 * @param key
	 * @param value
	 */
    public static synchronized void putObjectCached(String cache_name, Serializable key, Serializable value) {
        Cache cache = getCache(cache_name);
        if (cache != null) {
            try {
                cache.remove(key);
                Element elem = new Element(key, value);
                cache.put(elem);
            } catch (Exception e) {
                log.error("put cache(" + cache_name + ") of " + key + " failed.", e);
            }
        }
    }

    /**
	 * ��ȡָ����ƵĻ���
	 * @param arg0
	 * @return
	 * @throws IllegalStateException
	 */
    public static Cache getCache(String arg0) throws IllegalStateException {
        return manager.getCache(arg0);
    }

    /**
	 * ��ȡ�����е���Ϣ
	 * @param cache
	 * @param key
	 * @return
	 * @throws IllegalStateException
	 * @throws CacheException
	 */
    public static Element getElement(String cache, Serializable key) throws IllegalStateException, CacheException {
        Cache cCache = getCache(cache);
        return cCache.get(key);
    }

    /**
	 * ��ȡ�洢RSS��Ϣ�Ļ���
	 * @return
	 */
    public static Cache getRssCache() {
        if (manager != null) return manager.getCache("DLOG4J_channels");
        return null;
    }

    /**
	 * ��ȡRSS�����е�ĳ����Ϣ
	 * @param key
	 * @return
	 * @throws IllegalStateException
	 * @throws CacheException
	 */
    public static Element getRssElement(Serializable key) throws IllegalStateException, CacheException {
        Cache cache = getRssCache();
        return (cache != null) ? cache.get(key) : null;
    }

    /**
	 * ֹͣ���������
	 */
    public static void shutdown() {
        if (manager != null) manager.shutdown();
    }
}
