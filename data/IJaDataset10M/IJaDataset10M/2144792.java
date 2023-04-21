package net.java.dev.joode.collision.convex.gauss;

import java.util.*;
import javax.vecmath.Vector3f;
import net.java.dev.joode.collision.convex.bsp.BSPShape;
import net.java.dev.joode.collision.convex.bsp.Plane;

public class GaussMapRegion<V, E, F> implements BSPShape {

    public F data;

    public final List<GaussMapEdge<V, E, F>> edges = new ArrayList<GaussMapEdge<V, E, F>>();

    public final List<GaussMapVertex<V, E, F>> verteces = new ArrayList<GaussMapVertex<V, E, F>>();

    List<Vector3f> points = new ArrayList<Vector3f>();

    private int getPlaneIndex;

    boolean[] edgeTestResults;

    public GaussMapRegion(F data) {
        this.data = data;
    }

    private GaussMapRegion(List<GaussMapEdge<V, E, F>> edges, List<GaussMapVertex<V, E, F>> verteces, List<Vector3f> points, int getPlaneIndex, F data, boolean[] edgeTestResults) {
        this.edges.addAll(edges);
        this.verteces.addAll(verteces);
        this.points.addAll(points);
        this.getPlaneIndex = getPlaneIndex;
        this.data = data;
        this.edgeTestResults = edgeTestResults;
    }

    /**
	 * uses the setup edges to construct the spatial bounds
	 * 
	 */
    public void initialize() {
        points.clear();
        GaussMapEdge current = edges.get(0);
        GaussMapVertex start = edges.get(0).v1;
        GaussMapVertex next = edges.get(0).v2;
        addPoint(start);
        boolean found = false;
        while (!found) {
            for (GaussMapEdge edge : edges) {
                if (edge == current) continue;
                if (edge.v1 == next) {
                    next = edge.v2;
                    addPoint(edge.v1);
                    if (next == start) {
                        found = true;
                        break;
                    }
                } else if (edge.v2 == next) {
                    next = edge.v1;
                    addPoint(edge.v2);
                    if (next == start) {
                        found = true;
                        break;
                    }
                }
            }
        }
        Vector3f validPoint = new Vector3f();
        for (Vector3f point : points) {
            validPoint.add(point);
        }
        validPoint.scale(1f / (float) points.size());
        edgeTestResults = new boolean[edges.size()];
        for (int i = 0; i < edgeTestResults.length; i++) {
            int classification = edges.get(i).getPlane().classify(validPoint);
            if (classification == Plane.COINCIDENT || classification == Plane.SPANNING) {
                System.out.println(this);
                System.out.println(edges.get(i).getPlane());
                System.out.println(validPoint);
                throw new RuntimeException("logic error");
            }
            if (classification == Plane.IN_BACK_OF) {
                edgeTestResults[i] = false;
            } else {
                edgeTestResults[i] = true;
            }
        }
    }

    private void addPoint(GaussMapVertex base) {
        Vector3f point = new Vector3f();
        base.get(point);
        points.add(point);
    }

    /**
	 * returns wheter the coord is indide this convex polyhedra
	 * (as defined by the veteces incase the polyhedra was further subdivided)
	 * @return
	 */
    public boolean isInside(Vector3f point) {
        for (int i = 0; i < edgeTestResults.length; i++) {
            int classification = edges.get(i).getPlane().classify(point);
            if (classification == Plane.COINCIDENT || classification == Plane.SPANNING || (edgeTestResults[i] && classification == Plane.IN_FRONT_OF) || (!edgeTestResults[i] && classification == Plane.IN_BACK_OF)) {
            } else {
                return false;
            }
        }
        System.out.println(point + " is inside " + this + " data: " + data);
        return true;
    }

    /**
	 * A CONVEX shape will have exactly two childen if split. We are rightly or
	 * wrongly assuming this face is convex (it should be, it is part of a
	 * convex polyhedra) front is pos 0 no coincident peices back in 2
	 */
    public void split(Plane part, BSPShape[] peices) {
        int count = points.size();
        List<Vector3f> outpts = new ArrayList<Vector3f>();
        List<Vector3f> inpts = new ArrayList<Vector3f>();
        float sideA, sideB;
        Vector3f ptA = points.get(count - 1);
        Vector3f ptB = null;
        sideA = part.dist(ptA);
        for (int i = 0; i < count; i++) {
            ptB = points.get(i);
            sideB = part.dist(ptB);
            if (sideB > 0) {
                if (sideA < 0) {
                    Vector3f intercept = part.intercept(ptA, ptB);
                    outpts.add(intercept);
                    inpts.add(intercept);
                }
                outpts.add(ptB);
            } else if (sideB < 0) {
                if (sideA > 0) {
                    Vector3f intercept = part.intercept(ptA, ptB);
                    outpts.add(intercept);
                    inpts.add(intercept);
                }
                inpts.add(ptB);
            } else {
                outpts.add(ptB);
                inpts.add(ptB);
            }
            ptA = ptB;
            sideA = sideB;
        }
        peices[0] = new GaussMapRegion<V, E, F>(edges, verteces, outpts, getPlaneIndex, data, edgeTestResults);
        peices[2] = new GaussMapRegion<V, E, F>(edges, verteces, inpts, getPlaneIndex, data, edgeTestResults);
    }

    public int classify(Plane partition) {
        int side = COINCIDENT;
        int start = 0;
        while (!(side == IN_BACK_OF || side == IN_FRONT_OF)) {
            if (start == points.size()) break;
            Vector3f point = points.get(start++);
            side = partition.classify(point);
        }
        for (int i = start; i < points.size(); i++) {
            if (side == IN_BACK_OF) {
                if (partition.classify(points.get(i)) == IN_FRONT_OF) {
                    return SPANNING;
                } else if (partition.classify(points.get(i)) == IN_BACK_OF) {
                    return SPANNING;
                }
            }
        }
        return side;
    }

    public Plane getPlane() {
        getPlaneIndex = getPlaneIndex % edges.size();
        return edges.get(getPlaneIndex++).getPlane();
    }

    /**
	 * gets the unique set of vertices that surround this region
	 * @return
	 */
    public List<GaussMapVertex<V, E, F>> getVerteces() {
        if (verteces.size() == 0) {
            HashSet<GaussMapVertex<V, E, F>> vertexSet = new HashSet<GaussMapVertex<V, E, F>>();
            for (GaussMapEdge<V, E, F> edge : edges) {
                vertexSet.add(edge.v1);
                vertexSet.add(edge.v2);
            }
            verteces.addAll(vertexSet);
        }
        return verteces;
    }

    public String toString() {
        StringBuffer ret = new StringBuffer();
        for (Vector3f p : points) {
            ret.append(p);
            ret.append("-");
        }
        ret.deleteCharAt(ret.length() - 1);
        return ret.toString();
    }
}
