package org.fao.waicent.xmap2D.coordsys;

public abstract class GeodeticSpatialReferenceSystem extends SpatialReferenceSystem {

    public GeodeticSpatialReferenceSystem() {
    }

    public GeodeticSpatialReferenceSystem(String s) {
        super(s);
    }

    public abstract GeographicCoordinateSystem getGeographicCoordinateSystem();
}
