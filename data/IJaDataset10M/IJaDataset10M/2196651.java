package org.xith3d.spatial.clipping;

import java.util.List;
import org.openmali.spatial.bodies.Plane;
import org.openmali.vecmath2.Tuple3f;
import org.openmali.vecmath2.Vector3f;
import org.xith3d.utility.logging.X3DLog;

/**
 * Insert comment here.
 * 
 * @author David Yazel
 */
public class PolygonClipper {

    void clipLineToPlane(Tuple3f va, Tuple3f vb, Polygon newPolygon, Plane p) {
        float da = va.getX() * p.getNormal().getX() + va.getY() * p.getNormal().getY() + va.getZ() * p.getNormal().getZ() - p.getD();
        float db = vb.getX() * p.getNormal().getX() + vb.getY() * p.getNormal().getY() + vb.getZ() * p.getNormal().getZ() - p.getD();
        X3DLog.debug("distance from ", va, " to ", p, " is ", da);
        X3DLog.debug("distance from ", vb, " to ", p, " is ", db);
        if ((da >= 0) && (db >= 0)) {
            X3DLog.debug("both are positive, adding points ");
            if (!newPolygon.contains(va)) newPolygon.add(va);
            if (!newPolygon.contains(vb)) newPolygon.add(vb);
        } else if ((da < 0) && (db < 0)) {
            X3DLog.debug("both are negative, ignoring points ");
        } else if (da < 0) {
            X3DLog.debug("A is negative, adding point + intersection ");
            Vector3f intersect = new Vector3f();
            float s = da / (da - db);
            intersect.setX(va.getX() + s * (vb.getX() - va.getX()));
            intersect.setY(va.getY() + s * (vb.getY() - va.getY()));
            intersect.setZ(va.getZ() + s * (vb.getZ() - va.getZ()));
            if (!newPolygon.contains(intersect)) newPolygon.add(intersect);
            if (!newPolygon.contains(vb)) newPolygon.add(vb);
        } else if (db < 0) {
            X3DLog.debug("B is negative, adding point + intersection ");
            Vector3f intersect = new Vector3f();
            float s = da / (da - db);
            intersect.setX(va.getX() + s * (vb.getX() - va.getX()));
            intersect.setY(va.getY() + s * (vb.getY() - va.getY()));
            intersect.setZ(va.getZ() + s * (vb.getZ() - va.getZ()));
            X3DLog.debug(" i calc intersection is ", intersect);
            if (!newPolygon.contains(va)) newPolygon.add(va);
            if (!newPolygon.contains(intersect)) newPolygon.add(intersect);
        }
        X3DLog.debug("");
    }

    Polygon clipToPlane(Polygon polygon, Plane p) {
        Polygon newPolygon = new Polygon();
        List<Tuple3f> vertices = polygon.getVertices();
        for (int i = 0; i < vertices.size(); i++) {
            Tuple3f a = vertices.get(i);
            Tuple3f b = vertices.get((i + 1) % vertices.size());
            clipLineToPlane(a, b, newPolygon, p);
        }
        return (newPolygon);
    }

    public Polygon clip(Polygon polygon, Plane[] planes) {
        for (int i = 0; i < planes.length; i++) {
            polygon = clipToPlane(polygon, planes[i]);
            if (polygon.getVertices().size() == 0) return (polygon);
        }
        return (polygon);
    }
}
