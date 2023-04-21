package com.iver.cit.gvsig.fmap.operations.strategies;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import com.iver.cit.gvsig.exceptions.visitors.ProcessVisitorException;
import com.iver.cit.gvsig.exceptions.visitors.StartVisitorException;
import com.iver.cit.gvsig.exceptions.visitors.VisitorException;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.layers.FBitSet;
import com.iver.cit.gvsig.fmap.layers.FLayer;

/**
 * Query by point Visitor.
 *
 * @author Vicente Caballero Navarro
 */
public class QueryByPointVisitor implements FeatureVisitor {

    private Point2D point = null;

    private double tolerance;

    private FLayer layer = null;

    private FBitSet bitset = null;

    private Rectangle2D recPoint = null;

    /**
	 * Devuelve un FBitSet con los �ndices de los registros de la consulta.
	 *
	 * @return FBitSet con los �ndices de la consulta.
	 */
    public FBitSet getBitSet() {
        return bitset;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param layer DOCUMENT ME!
	 */
    public void setLayer(FLayer layer) {
        this.layer = layer;
    }

    /**
	 * Inserta la tolerancia que se aplica en la selecci�n.
	 *
	 * @param t tolerancia.
	 */
    public void setTolerance(double t) {
        tolerance = t;
    }

    /**
	 * Inserta el punto de consulta.
	 *
	 * @param p punto de consulta.
	 */
    public void setQueriedPoint(Point2D p) {
        point = p;
    }

    /**
	 * @see com.iver.cit.gvsig.fmap.operations.strategies.FeatureVisitor#visit(com.iver.cit.gvsig.fmap.core.IGeometry,
	 * 		int)
	 */
    public void visit(IGeometry g, int index) throws VisitorException, ProcessVisitorException {
        if (g == null) return;
        if (g.intersects(recPoint)) {
            bitset.set(index, true);
        } else {
            bitset.set(index, false);
        }
    }

    /**
	 * @see com.iver.cit.gvsig.fmap.operations.strategies.FeatureVisitor#stop()
	 */
    public void stop(FLayer layer) throws VisitorException {
    }

    /**
	 * @see com.iver.cit.gvsig.fmap.operations.strategies.FeatureVisitor#start()
	 */
    public boolean start(FLayer layer) throws StartVisitorException {
        bitset = new FBitSet();
        recPoint = new Rectangle2D.Double(point.getX() - (tolerance / 2), point.getY() - (tolerance / 2), tolerance, tolerance);
        return true;
    }

    public String getProcessDescription() {
        return "Selecting geometries that intersects a point";
    }
}
