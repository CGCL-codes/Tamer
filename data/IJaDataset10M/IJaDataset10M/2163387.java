package toolchain;

import com.jme.math.Vector3f;

public class Triangle {

    public Vector3f a;

    public Vector3f b;

    public Vector3f c;

    public Vector3f ab;

    public Vector3f ac;

    public Vector3f n;

    public Bounds2D bounds;

    public Triangle(Vector3f a, Vector3f b, Vector3f c) {
        this.a = a;
        this.b = b;
        this.c = c;
        ab = b.subtract(a);
        ac = c.subtract(a);
        n = ab.cross(ac);
        bounds = new Bounds2D();
        bounds.addPoint(a);
        bounds.addPoint(b);
        bounds.addPoint(c);
    }

    public String toString() {
        return String.format("[(%f, %f, %f) (%f, %f, %f) (%f, %f, %f)]", a.x, a.y, a.z, b.x, b.y, b.z, c.x, c.y, c.z);
    }
}
