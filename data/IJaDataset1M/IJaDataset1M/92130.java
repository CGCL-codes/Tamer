package onepoint.persistence.hibernate.cache;

import com.opensymphony.oscache.base.CacheEntry;
import com.opensymphony.oscache.base.Config;
import onepoint.log.XLog;
import onepoint.log.XLogFactory;
import org.hibernate.cache.Cache;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.CacheProvider;
import org.hibernate.cache.Timestamper;
import org.hibernate.util.PropertiesHelper;
import org.hibernate.util.StringHelper;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Support for OpenSymphony OSCache. This implementation assumes
 * that identifiers have well-behaved <tt>toString()</tt> methods.
 *
 * @author horia.chiorean
 */
public class OpOSCacheProvider implements CacheProvider {

    /**
    * Class used for logging.
    */
    private static final XLog logger = XLogFactory.getLogger(OpOSCacheProvider.class, true);

    /**
    * The <tt>OSCache</tt> refresh period property suffix.
    */
    public static final String OSCACHE_REFRESH_PERIOD = "refresh.period";

    /**
    * The <tt>OSCache</tt> CRON expression property suffix.
    */
    public static final String OSCACHE_CRON = "cron";

    /**
    * The cache properties.
    */
    static Properties cacheProperties = null;

    /**
    * Returns the os cache configuration properties.
    * @return a <code>Properties</code> object representing the configuration options for os-cache.
    */
    public static Properties getCacheProperties() {
        return cacheProperties;
    }

    /**
    * Initializes the cache properties.
    * @param hibernateProperties a <code>Properties</code> object representing the properties of the session factory.
    */
    private static void initCacheConfiguration(Properties hibernateProperties) {
        if (cacheProperties == null) {
            try {
                cacheProperties = new Properties();
                InputStream input = OpOSCacheProvider.class.getResourceAsStream("oscache.properties");
                cacheProperties.load(input);
                Object capacity = hibernateProperties.get(OpOSCache.OSCACHE_CAPACITY);
                if (capacity != null) {
                    cacheProperties.put(OpOSCache.OSCACHE_CAPACITY, capacity);
                }
            } catch (IOException e) {
                logger.error("Cannot load properties file", e);
            }
        }
    }

    /**
    * Builds a new {@link org.hibernate.cache.Cache} instance, and gets it's properties from the OSCache {@link Config}
    * which reads the properties file (<code>oscache.properties</code>) from the classpath.
    * If the file cannot be found or loaded, an the defaults are used.
    *
    * @param region
    * @param properties
    * @return
    * @throws org.hibernate.cache.CacheException
    *
    */
    public Cache buildCache(String region, Properties properties) throws CacheException {
        initCacheConfiguration(properties);
        int refreshPeriod = PropertiesHelper.getInt(StringHelper.qualify(region, OSCACHE_REFRESH_PERIOD), cacheProperties, CacheEntry.INDEFINITE_EXPIRY);
        String cron = cacheProperties.getProperty(StringHelper.qualify(region, OSCACHE_CRON));
        return new OpOSCache(refreshPeriod, cron, region);
    }

    public long nextTimestamp() {
        return Timestamper.next();
    }

    public boolean isMinimalPutsEnabledByDefault() {
        return false;
    }

    /**
    * Callback to perform any necessary cleanup of the underlying cache implementation
    * during SessionFactory.close().
    */
    public void stop() {
    }

    /**
    * Callback to perform any necessary initialization of the underlying cache implementation
    * during SessionFactory construction.
    *
    * @param properties current configuration settings.
    */
    public void start(Properties properties) throws CacheException {
    }
}
