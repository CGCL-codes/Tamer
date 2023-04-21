package object.solid;

import object.ray.Ray;
import object.util.Hit;
import util.math.algebra.Vector;
import util.math.transformation.Frame;

public class Plane extends Object3D {

    private Vector normal;

    public Plane(Vector p, Vector n) {
        normal = n.normalize();
        Vector u = new Vector(3);
        if (normal.get(0) == 0 && normal.get(1) == 0 && normal.get(2) == 0) {
            u.set(0, normal.get(1));
            u.set(1, -normal.get(0));
        } else {
            for (int i = 0; i < normal.getRows(); i++) {
                u.set(i, (normal.get(i) == 0) ? 1 : 0);
            }
        }
        u = u.normalize();
        Vector v = normal.cross(u).normalize();
        transform(new Frame(p, new Vector[] { u, v, normal }));
    }

    public Plane(Vector n) {
        this(new Vector(3), n);
    }

    @Override
    public boolean intersect(Ray ray, Hit hit, double eps) {
        Ray localRay = frameInv.transformRay(ray).normalize();
        if (localRay.getDirection().get(2) == 0) return false;
        double t = -localRay.getOrigin().get(2) / localRay.getDirection().get(2);
        if (t < eps) return false;
        double tw = t * frame.transformVector(localRay.getDirection()).norm();
        if (tw >= hit.getT()) {
            return false;
        }
        if (hit != null) {
            hit.setMaterial(getMaterial());
            hit.setNormal(normal);
            hit.setT(tw);
        }
        return true;
    }
}
