package org.isakiev.wic.processor;

import java.util.ArrayList;
import java.util.List;
import org.isakiev.wic.filter.Filter;
import org.isakiev.wic.filter.FilterSet;
import org.isakiev.wic.geometry.ArraySurface;
import org.isakiev.wic.geometry.CoordinateMatrix;
import org.isakiev.wic.geometry.Region;
import org.isakiev.wic.geometry.Surface;
import org.isakiev.wic.geometry.SurfaceUtil;

public class WICProcessorPrevious extends AbstractProcessor {

    private CoordinateMatrix matrix;

    private int m;

    private FilterSet filterSet;

    public WICProcessorPrevious(CoordinateMatrix matrix, FilterSet filterSet) {
        this.matrix = matrix;
        this.filterSet = filterSet;
        this.m = Math.abs(matrix.getIntDeterminant());
        if (filterSet.getDecompositionFilters().size() != m) {
            new IllegalArgumentException("Filter set with " + m + " filters expected.");
        }
    }

    public List<Surface> decompose(Surface sourceSurface) {
        throw new UnsupportedOperationException();
    }

    public Surface reconstruct(List<Surface> decomposedSurfaces) {
        throw new UnsupportedOperationException();
    }

    public int getChannelsNumber() {
        return m;
    }

    public FilterSet getFilterSet() {
        return filterSet;
    }

    public CoordinateMatrix getMatrix() {
        return matrix;
    }
}
