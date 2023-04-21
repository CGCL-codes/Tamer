package org.geotools.caching.grid;

import junit.framework.Test;
import junit.framework.TestSuite;
import java.io.File;
import java.io.IOException;
import org.geotools.caching.AbstractFeatureCache;
import org.geotools.caching.FeatureCacheException;
import org.geotools.caching.spatialindex.store.DiskStorage;

public class DiskGridFeatureCacheTest extends GridFeatureCacheTest {

    public static Test suite() {
        return new TestSuite(DiskGridFeatureCacheTest.class);
    }

    @Override
    protected AbstractFeatureCache createInstance(int capacity) throws FeatureCacheException, IOException {
        DiskStorage storage = new DiskStorage(File.createTempFile("cache", ".tmp"), 1000);
        this.cache = new GridFeatureCache(ds.getFeatureSource(dataset.getSchema().getTypeName()), 100, capacity, storage);
        storage.setParent(this.cache.tracker);
        return this.cache;
    }
}
