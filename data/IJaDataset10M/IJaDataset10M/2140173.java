package com.vividsolutions.jts.triangulate;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * An interface for strategies for determining the location of split points on constraint segments.
 * The location of split points has a large effect on the performance and robustness of enforcing a
 * constrained Delaunay triangulation. Poorly chosen split points can cause repeated splitting,
 * especially at narrow constraint angles, since the split point will end up encroaching on the
 * segment containing the original encroaching point. With detailed knowledge of the geometry of the
 * constraints, it is sometimes possible to choose better locations for splitting.
 * 
 * @author mbdavis
 */
public interface ConstraintSplitPointFinder {

    /**
     * Finds a point at which to split an encroached segment to allow the original segment to appear
     * as edges in a constrained Delaunay triangulation.
     * 
     * @param seg the encroached segment
     * @param encroachPt the encroaching point
     * @return the point at which to split the encroached segment
     */
    Coordinate findSplitPoint(Segment seg, Coordinate encroachPt);
}
