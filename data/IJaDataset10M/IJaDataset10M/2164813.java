package furniture.items3d.model.tessellation;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.round;
import static java.lang.Math.sin;
import java.util.ArrayList;
import java.util.List;
import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import org.eclipse.swt.graphics.RGB;
import furniture.items3d.bounds.Bounds3d;
import furniture.render3d.PointSet;
import furniture.render3d.Polygon3d;
import furniture.render3d.Triangle;

/**
 * @author Grill Balazs (balage.g@gmail.com)
 *
 */
public class Helper {

    /**
	 * approximation level of circles
	 */
    private static int circle_level = 32;

    /**
	 * Modify base color based on the given normal vector
	 * @param base
	 * @param n
	 * @return
	 */
    public static RGB shade(RGB base, Vector3d n, Vector3d light) {
        light.normalize();
        double bright = n.dot(light);
        double f = (0.7 + 0.5 * bright);
        int r = min(255, (int) round(base.red * f));
        int g = min(255, (int) round(base.green * f));
        int b = min(255, (int) round(base.blue * f));
        RGB result = new RGB(r, g, b);
        return result;
    }

    /**
	 * return the translation of the given pointset. The pointset is
	 * translated by dir*scale.
	 * @param what
	 * @param dir
	 * @param scale
	 * @return
	 */
    public static Polygon3d translate(Polygon3d what, Vector3d dir, double scale) {
        Polygon3d result = what.copy();
        Vector3d d = new Vector3d(dir);
        d.scale(scale);
        result.translate(d);
        return result;
    }

    /**
	 * Calculate the normal vector of a Triangle
	 * @param t
	 * @return
	 */
    public static Vector3d triangleNorm(Triangle t) {
        Vector3d v1 = new Vector3d(t.p3);
        Vector3d v2 = new Vector3d(t.p2);
        v1.sub(t.p1);
        v2.sub(t.p1);
        Vector3d n = new Vector3d(0, 0, 0);
        n.cross(v1, v2);
        n.normalize();
        return n;
    }

    /**
	 * Calculate a point which is p+v*l
	 * @param p
	 * @param v
	 * @param l
	 * @return
	 */
    public static Point3d direct(Point3d p, Vector3d v, double l) {
        Vector3d d = new Vector3d(v);
        d.scale(l);
        d.add(p);
        return new Point3d(d);
    }

    /**
	 * Return v1*c+v2*c
	 * @param v1
	 * @param v2
	 * @param c1
	 * @param c2
	 * @return
	 */
    public static Vector3d weightsum(Vector3d v1, Vector3d v2, double c1, double c2) {
        Vector3d result = new Vector3d(v1);
        result.scale(c1);
        Vector3d o = new Vector3d(v2);
        o.scale(c2);
        result.add(o);
        return result;
    }

    /**
	 * Calculate points of a circle
	 * @param p center point of circle
	 * @param n normal-vector of circle
	 * @param r radius of circle
	 * @return
	 * @note this method assumes that n.y == 0.0
	 */
    public static PointSet circle(Point3d p, Vector3d n, double r) {
        PointSet result = new PointSet();
        Vector3d v = new Vector3d(0, 1, 0);
        Vector3d w = new Vector3d();
        w.cross(v, n);
        int k = circle_level;
        double alpha0 = 2 * PI / k;
        for (int i = 0; i < k; i++) {
            double alpha = alpha0 * i;
            Vector3d d = weightsum(v, w, cos(alpha), sin(alpha));
            d.scale(r);
            Point3d p0 = new Point3d(p);
            p0.add(d);
            result.addPoint(p0);
        }
        return result;
    }

    /**
	 * Calculate points of a rectangle
	 * @param p center point of rectangle
	 * @param n normal-vector of rectangle
	 * @param wd width of rectangle
	 * @param hd height of rectangle
	 * @return
	 * @note this method assumes that n.y == 0.0
	 */
    public static PointSet rectangle(Point3d p, Vector3d n, double wd, double hd) {
        PointSet result = new PointSet();
        Vector3d v = new Vector3d(0, 1, 0);
        Vector3d w = new Vector3d();
        w.cross(v, n);
        v.scale(hd / 2);
        w.scale(wd / 2);
        Vector3d d;
        d = new Vector3d(p);
        d.add(v);
        d.sub(w);
        result.addPoint(new Point3d(d));
        d = new Vector3d(p);
        d.add(v);
        d.add(w);
        result.addPoint(new Point3d(d));
        d = new Vector3d(p);
        d.sub(v);
        d.add(w);
        result.addPoint(new Point3d(d));
        d = new Vector3d(p);
        d.sub(v);
        d.sub(w);
        result.addPoint(new Point3d(d));
        return result;
    }

    /**
	 * Calculate the bounding parameters of the line given by the
	 * equation p+v*t = 0
	 * @param p
	 * @param v
	 * @param bounds
	 * @return the vector of (tmin,tmax), which determines the
	 * section of line in the given bounds.
	 */
    public static Point2d linebounds(Point3d p, Vector3d v, Bounds3d bounds) {
        Vector3d min = bounds.min;
        Vector3d max = bounds.max;
        List<Double> mins = new ArrayList<Double>();
        List<Double> maxs = new ArrayList<Double>();
        Double m = line1d(p.x, v.x, min.x);
        if (m != null) mins.add(m);
        m = line1d(p.y, v.y, min.y);
        if (m != null) mins.add(m);
        m = line1d(p.z, v.z, min.z);
        if (m != null) mins.add(m);
        m = line1d(p.x, v.x, max.x);
        if (m != null) maxs.add(m);
        m = line1d(p.y, v.y, max.y);
        if (m != null) maxs.add(m);
        m = line1d(p.z, v.z, max.z);
        if (m != null) maxs.add(m);
        Point2d r = new Point2d(0, 0);
        if (mins.size() > 0) r.x = mins.get(0);
        for (Double q : mins) r.x = max(r.x, q);
        if (maxs.size() > 0) r.y = maxs.get(0);
        for (Double q : maxs) r.y = min(r.y, q);
        return r;
    }

    /**
	 * Calculates the t parameter, where p+v*t = target
	 * @param p
	 * @param v
	 * @param target
	 * @return the value of t, or if v==0 (and therefore not t
	 * could be given) returns null.
	 */
    public static Double line1d(double p, double v, double target) {
        if (target == p) return 0.0;
        if (v == 0) return null;
        return (target - p) / v;
    }
}
