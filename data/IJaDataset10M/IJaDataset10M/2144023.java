package net.bull.javamelody;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Statistics;
import net.sf.ehcache.config.CacheConfiguration;

/**
 * Informations sur un cache de données.
 * L'état d'une instance est initialisé à son instanciation et non mutable;
 * il est donc de fait thread-safe.
 * Cet état est celui d'un cache à un instant t.
 * Les instances sont sérialisables pour pouvoir être transmises au serveur de collecte.
 * Pour l'instant seul ehcache est géré.
 * @author Emeric Vernat
 */
class CacheInformations implements Serializable {

    private static final long serialVersionUID = -3025833425994923286L;

    private static final boolean EHCACHE_AVAILABLE = isEhcacheAvailable();

    private static final boolean EHCACHE_1_6 = isEhcache16();

    private static final boolean EHCACHE_1_2 = isEhcache12();

    private static final boolean EHCACHE_1_2_X = isEhcache12x();

    private final String name;

    private final long inMemoryObjectCount;

    private final int inMemoryPercentUsed;

    private final long onDiskObjectCount;

    private final long inMemoryHits;

    private final long cacheHits;

    private final long cacheMisses;

    private final String configuration;

    CacheInformations(Ehcache cache) {
        super();
        assert cache != null;
        final Statistics statistics = cache.getStatistics();
        assert statistics != null;
        this.name = cache.getName();
        long tmpInMemoryObjectCount;
        long tmpOnDiskObjectCount;
        if (EHCACHE_1_6) {
            tmpInMemoryObjectCount = statistics.getMemoryStoreObjectCount();
            tmpOnDiskObjectCount = statistics.getDiskStoreObjectCount();
            assert statistics.getStatisticsAccuracy() == Statistics.STATISTICS_ACCURACY_BEST_EFFORT;
        } else {
            tmpInMemoryObjectCount = cache.getMemoryStoreSize();
            tmpOnDiskObjectCount = cache.getDiskStoreSize();
        }
        this.inMemoryObjectCount = tmpInMemoryObjectCount;
        this.onDiskObjectCount = tmpOnDiskObjectCount;
        long tmpInMemoryHits;
        long tmpCacheHits;
        long tmpCacheMisses;
        int tmpInMemoryPercentUsed;
        String tmpConfiguration;
        if (EHCACHE_1_2_X) {
            tmpInMemoryHits = invokeStatisticsMethod(statistics, "getInMemoryHits");
            tmpCacheHits = invokeStatisticsMethod(statistics, "getCacheHits");
            tmpCacheMisses = invokeStatisticsMethod(statistics, "getCacheMisses");
            tmpInMemoryPercentUsed = -1;
            tmpConfiguration = null;
        } else if (EHCACHE_1_2) {
            tmpInMemoryHits = -1;
            tmpCacheHits = -1;
            tmpCacheMisses = -1;
            tmpInMemoryPercentUsed = -1;
            tmpConfiguration = null;
        } else {
            tmpInMemoryHits = statistics.getInMemoryHits();
            tmpCacheHits = statistics.getCacheHits();
            tmpCacheMisses = statistics.getCacheMisses();
            final int maxElementsInMemory = cache.getCacheConfiguration().getMaxElementsInMemory();
            if (maxElementsInMemory == 0) {
                tmpInMemoryPercentUsed = -1;
            } else {
                tmpInMemoryPercentUsed = (int) (100 * inMemoryObjectCount / maxElementsInMemory);
            }
            tmpConfiguration = buildConfiguration(cache);
        }
        this.inMemoryHits = tmpInMemoryHits;
        this.cacheHits = tmpCacheHits;
        this.cacheMisses = tmpCacheMisses;
        this.inMemoryPercentUsed = tmpInMemoryPercentUsed;
        this.configuration = tmpConfiguration;
    }

    private static long invokeStatisticsMethod(Statistics statistics, String methodName) {
        try {
            final Number result = (Number) Statistics.class.getMethod(methodName, (Class<?>[]) null).invoke(statistics, (Object[]) null);
            return result.longValue();
        } catch (final NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        } catch (final InvocationTargetException e) {
            throw new IllegalStateException(e.getCause());
        } catch (final IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    private static boolean isEhcacheAvailable() {
        try {
            Class.forName("net.sf.ehcache.Cache");
            return true;
        } catch (final ClassNotFoundException e) {
            return false;
        } catch (final NoClassDefFoundError e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    static List<CacheInformations> buildCacheInformationsList() {
        if (!EHCACHE_AVAILABLE) {
            return Collections.emptyList();
        }
        final List<CacheManager> allCacheManagers;
        try {
            allCacheManagers = new ArrayList<CacheManager>(CacheManager.ALL_CACHE_MANAGERS);
        } catch (final NoSuchFieldError e) {
            return Collections.emptyList();
        }
        final List<CacheInformations> result = new ArrayList<CacheInformations>();
        for (final CacheManager cacheManager : allCacheManagers) {
            final String[] cacheNames = cacheManager.getCacheNames();
            for (final String cacheName : cacheNames) {
                try {
                    result.add(new CacheInformations(cacheManager.getEhcache(cacheName)));
                } catch (final Exception e) {
                    LOG.debug(e.toString(), e);
                }
            }
        }
        return result;
    }

    private static boolean isEhcache16() {
        try {
            Class.forName("net.sf.ehcache.Statistics");
            Statistics.class.getMethod("getMemoryStoreObjectCount");
            return true;
        } catch (final ClassNotFoundException e) {
            return false;
        } catch (final NoSuchMethodException e) {
            return false;
        }
    }

    private static boolean isEhcache12() {
        try {
            Class.forName("net.sf.ehcache.Ehcache");
            Ehcache.class.getMethod("getCacheConfiguration");
            return false;
        } catch (final ClassNotFoundException e) {
            return false;
        } catch (final NoClassDefFoundError e) {
            return false;
        } catch (final NoSuchMethodException e) {
            return true;
        }
    }

    private static boolean isEhcache12x() {
        try {
            Class.forName("net.sf.ehcache.Statistics");
            return isEhcache12();
        } catch (final ClassNotFoundException e) {
            return false;
        }
    }

    private static String buildConfiguration(Ehcache cache) {
        final StringBuilder sb = new StringBuilder();
        final CacheConfiguration configuration = cache.getCacheConfiguration();
        sb.append("ehcache [maxElementsInMemory = ").append(configuration.getMaxElementsInMemory());
        final boolean overflowToDisk = configuration.isOverflowToDisk();
        sb.append(", overflowToDisk = ").append(overflowToDisk);
        if (overflowToDisk) {
            sb.append(", maxElementsOnDisk = ").append(configuration.getMaxElementsOnDisk());
        }
        final boolean eternal = configuration.isEternal();
        sb.append(", eternal = ").append(eternal);
        if (!eternal) {
            sb.append(", timeToLiveSeconds = ").append(configuration.getTimeToLiveSeconds());
            sb.append(", timeToIdleSeconds = ").append(configuration.getTimeToIdleSeconds());
            sb.append(", memoryStoreEvictionPolicy = ").append(configuration.getMemoryStoreEvictionPolicy());
        }
        sb.append(", diskPersistent = ").append(configuration.isDiskPersistent());
        sb.append(']');
        return sb.toString();
    }

    String getName() {
        return name;
    }

    long getInMemoryObjectCount() {
        return inMemoryObjectCount;
    }

    long getInMemoryPercentUsed() {
        return inMemoryPercentUsed;
    }

    long getOnDiskObjectCount() {
        return onDiskObjectCount;
    }

    int getInMemoryHitsRatio() {
        if (cacheHits == 0) {
            return -1;
        }
        return (int) (100 * inMemoryHits / cacheHits);
    }

    int getHitsRatio() {
        final long accessCount = cacheHits + cacheMisses;
        if (accessCount == 0) {
            return -1;
        }
        return (int) (100 * cacheHits / accessCount);
    }

    String getConfiguration() {
        return configuration;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return getClass().getSimpleName() + "[name=" + getName() + ", inMemoryObjectCount=" + getInMemoryObjectCount() + ", inMemoryPercentUsed=" + getInMemoryPercentUsed() + ", onDiskObjectCount=" + getOnDiskObjectCount() + ", inMemoryHitsRatio=" + getInMemoryHitsRatio() + ", hitsRatio=" + getHitsRatio() + ']';
    }
}
