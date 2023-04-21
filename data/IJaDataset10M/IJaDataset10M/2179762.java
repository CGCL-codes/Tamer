package de.hu.gralog.jgraph.cellview;

import java.awt.geom.Point2D;
import org.jgraph.graph.VertexView;

public interface VertexDisplayModeRenderer extends DisplayModeRenderer {

    public Point2D getPerimeterPoint(VertexView view, Point2D source, Point2D p);
}
