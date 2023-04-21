package net.phys2d.raw.shapes;

import net.phys2d.math.MathUtil;
import net.phys2d.math.ROVector2f;
import net.phys2d.math.Vector2f;

/**
 * Class representing a convex and closed polygon as a list of vertices
 * in counterclockwise order. Convexity is maintained by a check in the
 * constructor after which the polygon becomes immutable.
 * 
 * @author Gideon Smeding
 *
 */
public class ConvexPolygon extends Polygon implements DynamicShape {

    /** Construct the convex polygon with a list of vertices
	 * sorted in counterclockwise order.
	 * Note that all the vector values will be copied.
	 * 
	 * Throws an exception when too few vertices are given (< 3)
	 * and when the supplied vertices are not convex.
	 * Polygons with area = 0, will be reported as non-convex too.
	 * 
	 * @param vertices Vertices sorted in counterclockwise order
	 */
    public ConvexPolygon(ROVector2f[] vertices) {
        if (vertices.length < 3) throw new IllegalArgumentException("A polygon can not have fewer than 3 edges!");
        this.vertices = new Vector2f[vertices.length];
        for (int i = 0; i < vertices.length; i++) {
            this.vertices[i] = new Vector2f(vertices[i]);
        }
        if (!super.isConvex()) throw new IllegalArgumentException("The supplied vertices do not represent a convex polygon!");
        float r = computeBoundingCircleRadius();
        this.bounds = new AABox(r * 2, r * 2);
        this.area = computeArea();
        this.centroid = computeCentroid();
    }

    /**
	 * Because convexness is checked at construction
	 * we can always return true here.
	 * @see Polygon#isConvex()
	 */
    public boolean isConvex() {
        return true;
    }

    /**
	 * Test whether or not the point p is in this polygon in O(n),
	 * where n is the number of vertices in this polygon.
	 *  
	 * @param p The point to be tested for inclusion in this polygon
	 * @return true iff the p is in this polygon (not on a border)
	 */
    public boolean contains(Vector2f p) {
        int l = vertices.length;
        for (int i = 0; i < vertices.length; i++) {
            Vector2f x = vertices[i];
            Vector2f y = vertices[(i + 1) % l];
            Vector2f z = p;
            if ((z.x - x.x) * (y.y - x.y) - (y.x - x.x) * (z.y - x.y) >= 0) return false;
        }
        return true;
    }

    /**
	 * Get point on this polygon's hull that is closest to p.
	 * 
	 * TODO: make this thing return a negative value when it is contained in the polygon
	 * 
	 * @param p The point to search the closest point for
	 * @return the nearest point on this vertex' hull
	 */
    public ROVector2f getNearestPoint(ROVector2f p) {
        float r = Float.MAX_VALUE;
        float l;
        Vector2f v;
        int m = -1;
        for (int i = 0; i < vertices.length; i++) {
            v = new Vector2f(vertices[i]);
            v.sub(p);
            l = v.x * v.x + v.y * v.y;
            if (l < r) {
                r = l;
                m = i;
            }
        }
        int length = vertices.length;
        Vector2f pm = new Vector2f(p);
        pm.sub(vertices[m]);
        Vector2f l1 = new Vector2f(vertices[(m - 1 + length) % length]);
        l1.sub(vertices[m]);
        Vector2f l2 = new Vector2f(vertices[(m + 1) % length]);
        l2.sub(vertices[m]);
        Vector2f normal;
        if (pm.dot(l1) > 0) {
            normal = MathUtil.getNormal(vertices[(m - 1 + length) % length], vertices[m]);
        } else if (pm.dot(l2) > 0) {
            normal = MathUtil.getNormal(vertices[m], vertices[(m + 1) % length]);
        } else {
            return vertices[m];
        }
        normal.scale(-pm.dot(normal));
        normal.add(p);
        return normal;
    }

    /**
	 * @see net.phys2d.raw.shapes.Shape#getSurfaceFactor()
	 */
    public float getSurfaceFactor() {
        return getArea();
    }
}
