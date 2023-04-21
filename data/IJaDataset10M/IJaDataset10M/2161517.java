package org.shapelogic.filter;

import org.shapelogic.polygon.IPoint2D;

/** Base class for all the spatially based point filters 
 * 
 * In order to get the cut off vale you need to know the bounding box for the polygon
 * This is only know when the filter is actually run. So this is set in the setup method.
 * 
 * The constraint should be between 0 and 1.
 * 
 * @author Sami Badawi
 *
 */
public abstract class PolygonSpatialPointFilter extends PolygonPointFilter {

    protected double _limit = Double.NaN;

    protected double _constraintNumber = Double.NaN;

    @Override
    public void setup() throws Exception {
        if (_constraint instanceof String) _constraintNumber = Double.parseDouble((String) _constraint); else if (_constraint instanceof Number) _constraintNumber = ((Number) _constraint).doubleValue();
        if (_constraintNumber != Double.NaN) _limit = coordinateChoser(getParent().getBBox().getDiagonalVector(_constraintNumber));
    }

    protected abstract double coordinateChoser(IPoint2D diagonalVector);
}
