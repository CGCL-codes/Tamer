package com.iver.cit.gvsig.gui.graphictools;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.layers.FBitSet;
import com.iver.cit.gvsig.fmap.rendering.FGraphic;
import com.vividsolutions.jts.index.ItemVisitor;

/**
 * @author fjp
 *
 * @deprecated Use queryByRect
 */
public class VisitorSelectGraphicByPoint implements ItemVisitor {

    private Point2D mapPoint;

    private double tol;

    private FBitSet selection = new FBitSet();

    private int numReg;

    Rectangle2D recPoint;

    public VisitorSelectGraphicByPoint(Point2D mapPoint, double tolerance) {
        this.mapPoint = mapPoint;
        this.tol = tolerance;
        this.numReg = 0;
        recPoint = new Rectangle2D.Double(mapPoint.getX() - (tolerance / 2), mapPoint.getY() - (tolerance / 2), tolerance, tolerance);
    }

    public void visitItem(Object item) {
        FGraphic graf = (FGraphic) item;
        IGeometry geom = graf.getGeom();
        if (geom.intersects(recPoint)) {
            selection.set(numReg);
        }
        numReg++;
    }

    public FBitSet getSelection() {
        return selection;
    }
}
