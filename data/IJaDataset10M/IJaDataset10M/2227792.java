package com.iver.cit.gvsig.fmap.core;

import com.vividsolutions.jts.geom.Geometry;

/**
 * A Geometry with the M coordinate. It contains an FshapeM
 * that is the object that contains the geometric position.
 * @author Jorge Piera LLodr� (jorge.piera@iver.es)
 */
public class FGeometryM extends FGeometry implements IGeometryM {

    private static final long serialVersionUID = -7259723180192528478L;

    public FGeometryM(FShapeM shp) {
        super(shp);
    }

    public double[] getMs() {
        return ((FShapeM) getInternalShape()).getMs();
    }

    public void setMAt(int i, double value) {
        ((FShapeM) getInternalShape()).setMAt(i, value);
    }

    public boolean isDecreasing() {
        return ((FShapeM) getInternalShape()).isDecreasing();
    }

    public void revertMs() {
        ((FShapeM) getInternalShape()).revertMs();
    }

    public Geometry toJTSGeometry() {
        return super.toJTSGeometry();
    }

    public String toText() {
        return ((FShapeM) getInternalShape()).toText();
    }

    public IGeometry cloneGeometry() {
        return new FGeometryM((FShapeM) super.cloneGeometry().getInternalShape());
    }
}
