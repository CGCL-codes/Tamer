package org.suse.ui.figure;

import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.PointList;

public class SelfReferencingTransitionFigure extends PolylineConnection {

    public SelfReferencingTransitionFigure() {
        System.out.println("creating self ref transition.");
        createConnection();
        createDecoration();
    }

    public void setPoints(PointList points) {
        System.out.println("Setting points");
        super.setPoints(points);
    }

    private void createConnection() {
        PointList points = new PointList(4);
        points.addPoint(0, 10);
        points.addPoint(-10, 10);
        points.addPoint(-10, -10);
        points.addPoint(0, 0);
        setPoints(points);
    }

    private void createDecoration() {
        PolygonDecoration arrow = new PolygonDecoration();
        arrow.setTemplate(PolygonDecoration.TRIANGLE_TIP);
        arrow.setScale(5, 2.5);
        setTargetDecoration(arrow);
    }
}
