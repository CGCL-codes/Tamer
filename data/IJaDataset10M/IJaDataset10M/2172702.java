package org.jcvi.assembly.coverage;

import java.util.List;
import org.jcvi.common.util.Range;

public interface CoverageMap<T extends CoverageRegion<?>> extends Iterable<T> {

    int getNumberOfRegions();

    T getRegion(int i);

    List<T> getRegions();

    boolean isEmpty();

    List<T> getRegionsWithin(Range range);

    List<T> getRegionsWhichIntersect(Range range);

    T getRegionWhichCovers(long consensusIndex);

    int getRegionIndexWhichCovers(long consensusIndex);

    double getAverageCoverage();

    int getMaxCoverage();

    int getMinCoverage();

    long getLength();

    int getNumberOfRegionsWithCoverage(int coverageDepth);

    int getNumberOfRegionsWithAtLeastCoverage(int coverageDepth);

    long getLengthOfRegionsWithCoverage(int coverageDepth);

    long getLengthOfRegionsWithAtLeastCoverage(int coverageDepth);
}
