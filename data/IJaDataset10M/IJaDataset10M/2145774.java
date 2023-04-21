package com.vividsolutions.jtstest.function;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.geom.util.LinearComponentExtracter;
import com.vividsolutions.jts.noding.snapround.GeometryNoder;
import com.vividsolutions.jts.operation.polygonize.Polygonizer;

public class PolygonOverlayFunctions {

    public static Geometry overlaySnapRounded(Geometry g1, Geometry g2, double precisionTol) {
        PrecisionModel pm = new PrecisionModel(precisionTol);
        GeometryFactory geomFact = g1.getFactory();
        List lines = LinearComponentExtracter.getLines(g1);
        if (g2 != null) LinearComponentExtracter.getLines(g2, lines);
        List nodedLinework = new GeometryNoder(pm).node(lines);
        Geometry nodedDedupedLinework = geomFact.buildGeometry(nodedLinework).union();
        Polygonizer polygonizer = new Polygonizer();
        polygonizer.add(nodedDedupedLinework);
        Collection polys = polygonizer.getPolygons();
        Polygon[] polyArray = GeometryFactory.toPolygonArray(polys);
        return geomFact.createGeometryCollection(polyArray);
    }
}
