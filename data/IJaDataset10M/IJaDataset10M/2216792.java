package cn.myapps.util.cache;

import java.util.HashMap;

public interface ICacheProvider {

    public static final String DEFAULT_CACHE_NAME = "DEFAULT_CACHE";

    public IMyCache getDefaultCache();

    public IMyCache createCache(java.lang.String name, int maxElementsInMemory, boolean overflowToDisk, boolean eternal, long timeToLiveSeconds, long timeToIdleSeconds);

    public IMyCache getCache(String name);

    public void clearCache(String name);

    public String[] getCacheNames();

    public void setClearedNames(HashMap clearedNames);

    public boolean clearByCacheName(String cacheName);

    /**
	 * 获取所有需要清除缓存的方法名称
	 * 
	 * @return 所有名称
	 */
    public String[] getClearedNames();
}
