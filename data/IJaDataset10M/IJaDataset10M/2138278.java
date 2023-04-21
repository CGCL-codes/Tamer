package com.iver.cit.gvsig.project.documents.view.snapping.snappers;

import java.awt.Graphics;
import java.awt.geom.Point2D;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.core.Handler;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.project.documents.view.snapping.AbstractSnapper;
import com.iver.cit.gvsig.project.documents.view.snapping.ISnapperVectorial;

public class FinalPointSnapper extends AbstractSnapper implements ISnapperVectorial {

    public Point2D getSnapPoint(Point2D point, IGeometry geom, double tolerance, Point2D lastPointEntered) {
        Point2D resul = null;
        Handler[] handlers = geom.getHandlers(IGeometry.SELECTHANDLER);
        double minDist = tolerance;
        for (int j = 0; j < handlers.length; j++) {
            Point2D handlerPoint = handlers[j].getPoint();
            double dist = handlerPoint.distance(point);
            if ((dist < minDist)) {
                resul = handlerPoint;
                minDist = dist;
            }
        }
        return resul;
    }

    public String getToolTipText() {
        return PluginServices.getText(this, "final_point");
    }

    public void draw(Graphics g, Point2D pPixels) {
        g.setColor(getColor());
        int half = getSizePixels() / 2;
        g.drawRect((int) (pPixels.getX() - half), (int) (pPixels.getY() - half), getSizePixels(), getSizePixels());
    }
}
