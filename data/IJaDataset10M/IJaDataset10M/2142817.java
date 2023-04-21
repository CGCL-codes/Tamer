package jviz.spatialindex;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public interface ISpatialIndex {

    boolean insert(Object id, Rectangle2D.Float mbr);

    boolean delete(Object id);

    Object[] containmentQuery(Rectangle2D.Float query);

    Object[] intersectionQuery(Rectangle2D.Float query);

    Object[] intersectionQuery(Point2D.Float query);

    Object[] nearestNeighborQuery(Rectangle2D.Float query);

    Object[] nearestNeighborQuery(Rectangle2D.Float query, INearestNeighborComparator nnc);
}
