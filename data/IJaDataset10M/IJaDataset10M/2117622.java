package org.mca.qmass.cache.hibernate.factory;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.QueryResultsRegion;
import org.hibernate.cache.Timestamper;
import org.hibernate.cache.TimestampsRegion;
import org.mca.qmass.cache.DefaultQCache;
import org.mca.qmass.cache.QCache;
import org.mca.qmass.core.QMass;
import java.io.Serializable;
import java.util.Map;

/**
 * User: malpay
 * Date: 04.May.2011
 * Time: 10:11:48
 */
public class GeneralRegion extends Region implements TimestampsRegion, QueryResultsRegion {

    public GeneralRegion(String regionName, QMass qmass) {
        super(regionName, new DefaultQCache(regionName, qmass, null, null));
    }

    @Override
    public Object get(Object key) throws CacheException {
        return qCache.getSilently((Serializable) key);
    }

    @Override
    public void put(Object key, Object val) throws CacheException {
        qCache.put((Serializable) key, (Serializable) val);
    }

    @Override
    public void evict(Object key) throws CacheException {
        qCache.remove((Serializable) key);
    }

    @Override
    public void evictAll() throws CacheException {
        qCache.clear();
    }
}
