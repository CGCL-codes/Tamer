package fi.foyt.hibernate.gae.cache;

import java.util.Properties;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.CacheDataDescription;
import org.hibernate.cache.spi.CollectionRegion;
import org.hibernate.cache.spi.EntityRegion;
import org.hibernate.cache.spi.QueryResultsRegion;
import org.hibernate.cache.spi.RegionFactory;
import org.hibernate.cache.spi.TimestampsRegion;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cfg.Settings;

public class GaeCacheRegionFactory implements RegionFactory {

    private static final long serialVersionUID = 1L;

    public GaeCacheRegionFactory(Properties properties) {
    }

    public void start(Settings settings, Properties properties) throws CacheException {
    }

    public void stop() {
    }

    public boolean isMinimalPutsEnabledByDefault() {
        return true;
    }

    public AccessType getDefaultAccessType() {
        return AccessType.READ_WRITE;
    }

    public long nextTimestamp() {
        return Timestamper.next();
    }

    public EntityRegion buildEntityRegion(String regionName, Properties properties, CacheDataDescription metadata) throws CacheException {
        return new GaeEntityRegion(regionName, properties, metadata);
    }

    public CollectionRegion buildCollectionRegion(String regionName, Properties properties, CacheDataDescription metadata) throws CacheException {
        return new GaeCollectionRegion(regionName, properties, metadata);
    }

    public QueryResultsRegion buildQueryResultsRegion(String regionName, Properties properties) throws CacheException {
        return new GaeQueryResultsRegion(regionName, properties);
    }

    public TimestampsRegion buildTimestampsRegion(String regionName, Properties properties) throws CacheException {
        return new GaeTimestampsRegion(regionName, properties);
    }
}
