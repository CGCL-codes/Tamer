package com.ibm.tuningfork.core.figure.action;

import com.ibm.tuningfork.core.graphics.Area;
import com.ibm.tuningfork.core.graphics.Coord;

/**
 * An interface for a figure that supports stable zoom in/out around a point
 */
public interface IZoomPointFigure {

    public Area getZoomArea();

    public void zoomPoint(double scale, Coord point);
}
