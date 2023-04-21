package net.java.dev.joode.collision.convex.bsp;

import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3f;
import net.java.dev.joode.collision.convex.gauss.GaussMapVertex;

public class Plane {

    public static final int COINCIDENT = BSPShape.COINCIDENT;

    public static final int IN_BACK_OF = BSPShape.IN_BACK_OF;

    public static final int IN_FRONT_OF = BSPShape.IN_FRONT_OF;

    public static final int SPANNING = BSPShape.SPANNING;

    public static final float EPSILON = .0001f;

    public final float a;

    public final float b;

    public final float c;

    public final float d;

    public final float nx;

    public final float ny;

    public final float nz;

    public final float p;

    public Plane(float a, float b, float c, float d) {
        float total = (float) Math.sqrt(a * a + b * b + c * c);
        this.a = a / total;
        this.b = b / total;
        this.c = c / total;
        this.d = d / total;
        float dividor = (float) Math.sqrt(a * a + b * b + c * c);
        nx = a / dividor;
        ny = b / dividor;
        nz = c / dividor;
        p = d / dividor;
    }

    /**
	 * creates a plane that passes through all three points
	 * @param p1
	 * @param p2
	 * @param p3
	 */
    public Plane(Vector3f p1, Vector3f p2, Vector3f p3) {
        this(p1.x, p1.y, p1.z, p2.x, p2.y, p2.z, p3.x, p3.y, p3.z);
    }

    public Plane(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3) {
        this(y1 * (z2 - z3) + y2 * (z3 - z1) + y3 * (z1 - z2), z1 * (x2 - x3) + z2 * (x3 - x1) + z3 * (x1 - x2), x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2), -(x1 * (y2 * z3 - y3 * z2) + x2 * (y3 * z1 - y1 * z3) + x3 * (y1 * z2 - y2 * z1)));
    }

    public Plane negate() {
        return new Plane(-a, -b, -c, -d);
    }

    public int classify(Vector3f vertex) {
        float dist = dist(vertex);
        if (dist < -EPSILON) {
            return BSPShape.IN_BACK_OF;
        } else if (dist > EPSILON) {
            return BSPShape.IN_FRONT_OF;
        } else {
            return BSPShape.COINCIDENT;
        }
    }

    public float dist(GaussMapVertex v) {
        return dist(v.x, v.y, v.z);
    }

    public float dist(Vector3f v) {
        return dist(v.x, v.y, v.z);
    }

    public float dist(float x, float y, float z) {
        float dist = a * x + b * y + c * z + d;
        return dist;
    }

    /**
	 * give the coord of the point of intersection between the assumed infinite line
	 * that is defined between the given points
	 * @param v1
	 * @param v2
	 * @return
	 */
    public Vector3f intercept(Vector3f v1, Vector3f v2) {
        float lambda = -(a * v1.x + b * v1.y + c * v1.z + d / (v2.x - v1.x + v2.y - v1.y - v2.x - v1.z));
        Vector3f intercept = new Vector3f(v1.x + lambda * (v2.x - v1.x), v1.y + lambda * (v2.y - v1.y), v1.z + lambda * (v2.z - v1.z));
        return intercept;
    }

    public Vector3f getNormal(Vector3f passback) {
        passback.x = a;
        passback.y = b;
        passback.z = c;
        return passback;
    }

    public int hashCode() {
        return (int) (a + b + c + d);
    }

    public boolean equals(Object ob) {
        Plane o = (Plane) ob;
        return nx == o.nx && ny == o.ny && nz == o.nz && p == o.p;
    }

    public Vector3f getHessian(Vector3f passback) {
        passback.x = nx;
        passback.y = ny;
        passback.z = nz;
        return passback;
    }

    public Plane rotate(Matrix3f rot) {
        Vector3f normal = getNormal(new Vector3f());
        rot.transform(normal);
        return new Plane(normal.x, normal.y, normal.z, d);
    }

    public Plane translate(Vector3f trans) {
        float d = this.d - (trans.x * a + trans.y * b + trans.z * c);
        return new Plane(a, b, c, d);
    }

    public String toString() {
        return a + "x + " + b + "y + " + c + "z + " + d + " = 0";
    }
}
