package com.sitechasia.webx.core.cache.ehcache;

import java.net.URL;
import java.util.Properties;
import net.sf.ehcache.CacheManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.sitechasia.webx.core.cache.AbstractCacheProvider;
import com.sitechasia.webx.core.cache.CacheException;
import com.sitechasia.webx.core.cache.ICache;
import com.sitechasia.webx.core.cache.ICacheProvider;

/**
 * TODO 类说明:
 * 
 * @author Administrator
 * @version 1.0
 * @see
 */
public class EhCacheProvider extends AbstractCacheProvider implements ICacheProvider {

    private static final Log logger = LogFactory.getLog(EhCacheProvider.class);

    private CacheManager manager = null;

    @Override
    protected ICache buildCache(String strategy) {
        try {
            net.sf.ehcache.Cache cache = manager.getCache(strategy);
            if (cache == null) {
                logger.warn("没有找到该策略的配置：" + strategy);
                return null;
            } else {
                return new EhCache(strategy, cache);
            }
        } catch (net.sf.ehcache.CacheException e) {
            throw new CacheException(e);
        }
    }

    @Override
    protected ICache getDefaultCache() {
        String default_strategy = DEFAULT_REGION_NAME;
        try {
            net.sf.ehcache.Cache cache = manager.getCache(default_strategy);
            if (cache == null) {
                manager.addCache(default_strategy);
                cache = manager.getCache(default_strategy);
                logger.debug("started EHCache region: " + default_strategy);
            }
            return new EhCache(default_strategy, cache);
        } catch (net.sf.ehcache.CacheException e) {
            throw new CacheException(e);
        }
    }

    protected void start(Properties properties) throws CacheException {
        if (manager != null) {
            logger.warn("重复调用start方法");
            return;
        }
        try {
            String configurationResourceName = null;
            if (properties != null) {
            }
            if (configurationResourceName == null || "".equals(configurationResourceName)) {
                manager = new CacheManager();
            } else {
            }
        } catch (net.sf.ehcache.CacheException e) {
            throw new CacheException("缓存启动发生错误", e);
        }
    }

    public void stop() throws CacheException {
        if (manager != null) {
            manager.shutdown();
            manager = null;
        }
    }
}
