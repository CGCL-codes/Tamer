package com.iver.cit.gvsig.fmap.core.adapter;

import java.awt.geom.Point2D;
import com.iver.cit.gvsig.fmap.core.GeneralPathX;

/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public class RectangleAdapter extends PolygonAdapter {

    /**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     */
    public void obtainShape(Point2D p) {
        Point2D[] points = getPoints();
        GeneralPathX elShape = new GeneralPathX(GeneralPathX.WIND_EVEN_ODD, points.length);
        if (points.length > 0) {
            elShape.moveTo(((Point2D) points[0]).getX(), ((Point2D) points[0]).getY());
        }
        if (points.length > 0) {
            elShape.lineTo(p.getX(), ((Point2D) points[0]).getY());
            elShape.lineTo(p.getX(), p.getY());
            elShape.lineTo(((Point2D) points[0]).getX(), p.getY());
            elShape.lineTo(((Point2D) points[0]).getX(), ((Point2D) points[0]).getY());
        }
        setGPX(elShape);
    }
}
