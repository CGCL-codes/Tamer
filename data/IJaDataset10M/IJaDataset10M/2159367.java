package de.grogra.vecmath.geom;

/**
 * An instance of this class represents the union of a list of volumes
 * as defined by Constructive Solid Geometry.
 * 
 * @author Ole Kniemeyer
 */
public class CSGUnion extends UnionBase {

    /**
	 * Creates a new <code>CSGUnion</code> whose list of volumes
	 * is empty.
	 */
    public CSGUnion() {
    }

    /**
	 * Creates a new <code>CSGUnion</code> whose list of volumes
	 * is set to <code>[a, b]</code>.
	 */
    public CSGUnion(Volume a, Volume b) {
        volumes.add(a);
        volumes.add(b);
    }

    public boolean computeIntersections(Line line, int which, IntersectionList list, Intersection excludeStart, Intersection excludeEnd) {
        return computeIntersections(line, which == Intersection.ALL, list, excludeStart, excludeEnd, 1);
    }

    @Override
    public Volume operator$or(Volume v) {
        CSGUnion c = new CSGUnion();
        c.volumes.addAll(volumes);
        c.volumes.add(v);
        return c;
    }
}
