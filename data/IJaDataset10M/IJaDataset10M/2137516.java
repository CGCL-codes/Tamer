package com.vividsolutions.jump.qa.diff;

import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.index.*;
import com.vividsolutions.jts.index.quadtree.Quadtree;
import com.vividsolutions.jump.feature.*;
import java.util.*;
import com.vividsolutions.jump.util.CoordinateArrays;

public class SegmentIndex {

    private SpatialIndex segIndex = new Quadtree();

    private Envelope itemEnv = new Envelope();

    public SegmentIndex(FeatureCollection fc) {
        for (Iterator i = fc.iterator(); i.hasNext(); ) {
            Feature f = (Feature) i.next();
            add(f.getGeometry());
        }
    }

    public void add(Geometry geom) {
        add(CoordinateArrays.toCoordinateArrays(geom, false));
    }

    public void add(LineString line) {
        add(line.getCoordinates());
    }

    public void add(List coordArrays) {
        for (Iterator i = coordArrays.iterator(); i.hasNext(); ) {
            add((Coordinate[]) i.next());
        }
    }

    public void add(Coordinate[] coord) {
        for (int i = 0; i < coord.length - 1; i++) {
            LineSegment lineseg = new LineSegment(coord[i], coord[i + 1]);
            lineseg.normalize();
            itemEnv.init(lineseg.p0, lineseg.p1);
            segIndex.insert(itemEnv, lineseg);
        }
    }

    public List query(Envelope env) {
        return segIndex.query(env);
    }
}
