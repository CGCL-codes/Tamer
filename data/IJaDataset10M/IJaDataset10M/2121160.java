package org.jcvi.common.core.assembly.util.slice;

import org.jcvi.common.core.assembly.PlacedRead;
import org.jcvi.common.core.assembly.util.coverage.CoverageMap;
import org.jcvi.common.core.assembly.util.coverage.CoverageRegion;
import org.jcvi.common.core.symbol.qual.QualityDataStore;

public class LargeSliceMapFactory<P extends PlacedRead, R extends CoverageRegion<P>, M extends CoverageMap<R>> extends AbstractSliceMapFactory<P, R, M> {

    private final int cacheSize;

    public LargeSliceMapFactory(QualityValueStrategy qualityValueStrategy, int cacheSize) {
        super(qualityValueStrategy);
        this.cacheSize = cacheSize;
    }

    public LargeSliceMapFactory(QualityValueStrategy qualityValueStrategy) {
        this(qualityValueStrategy, LargeSliceMap.DEFAULT_CACHE_SIZE);
    }

    @Override
    protected SliceMap createNewSliceMap(M coverageMap, QualityDataStore qualityDataStore, QualityValueStrategy qualityValueStrategy) {
        return LargeSliceMap.<P, R, M>create(coverageMap, qualityDataStore, qualityValueStrategy, cacheSize);
    }
}
