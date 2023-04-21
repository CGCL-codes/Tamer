package xmage.math;

import huf.misc.HMath;

/**
 * Class representing single-precision triangle in three dimensions.
 *
 * <p>
 * Note that for this class to work correctly and for {@link Planef#plPoint} and
 * {@link Planef#plNormal} variables to be updated you need to set triangle
 * vertices using one of <code>set(...)</code> methods and not write values to
 * vertex objects directly.
 * </p>
 */
public class Triangle3f {

    /** First triangle vertex. */
    public final Point3f a = new Point3f(0.0f, 0.0f, 0.0f);

    /** Second triangle vertex. */
    public final Point3f b = new Point3f(0.0f, 0.0f, 1.0f);

    /** Third triangle vertex. */
    public final Point3f c = new Point3f(1.0f, 0.0f, 0.0f);

    /**
	 * Create new triangle.
	 *
	 * <p>
	 * By defaul triangle is built on vertices
	 * <code>(0.0f, 0.0f, 0.0f), (0.0f, 0.0f, 1.0f), (1.0f, 0.0f, 0.0f)</code>.
	 * </p>
	 */
    public Triangle3f() {
    }

    /**
	 * Create triangle on three vertices.
	 *
	 * <p>
	 * Vertices must be in counter-clockwise order looking from
	 * triangle front (<i>normal</i>) side.
	 * </p>
	 *
	 * @param ax X coordinate of first triangle vertex
	 * @param ay Z coordinate of first triangle vertex
	 * @param az Z coordinate of first triangle vertex
	 * @param bx X coordinate of second triangle vertex
	 * @param by Z coordinate of second triangle vertex
	 * @param bz Z coordinate of second triangle vertex
	 * @param cx X coordinate of third triangle vertex
	 * @param cy Z coordinate of third triangle vertex
	 * @param cz Z coordinate of third triangle vertex
	 */
    public Triangle3f(float ax, float ay, float az, float bx, float by, float bz, float cx, float cy, float cz) {
        set(ax, ay, az, bx, by, bz, cx, cy, cz);
    }

    /**
	 * Create triangle on three vertices.
	 *
	 * <p>
	 * Vertices must be in counter-clockwise order looking from
	 * triangle front (<i>normal</i>) side.
	 * </p>
	 *
	 * @param a coordinates of first triangle vertex
	 * @param b coordinates of second triangle vertex
	 * @param c coordinates of third triangle vertex
	 */
    public Triangle3f(float a[], float b[], float c[]) {
        set(a, b, c);
    }

    /**
	 * Create new triangle.
	 *
	 * <p>
	 * Vertices must be in counter-clockwise order looking from
	 * triangle front (<i>normal</i>) side.
	 * </p>
	 *
	 * @param a first triangle vertex
	 * @param b second triangle vertex
	 * @param c third triangle vertex
	 */
    public Triangle3f(Point3f a, Point3f b, Point3f c) {
        set(a, b, c);
    }

    /**
	 * Set triangle vertices.
	 *
	 * <p>
	 * Vertices must be in counter-clockwise order looking from
	 * triangle front (<i>normal</i>) side.
	 * </p>
	 *
	 * @param ax X coordinate of first triangle vertex
	 * @param ay Z coordinate of first triangle vertex
	 * @param az Z coordinate of first triangle vertex
	 * @param bx X coordinate of second triangle vertex
	 * @param by Z coordinate of second triangle vertex
	 * @param bz Z coordinate of second triangle vertex
	 * @param cx X coordinate of third triangle vertex
	 * @param cy Z coordinate of third triangle vertex
	 * @param cz Z coordinate of third triangle vertex
	 */
    public void set(float ax, float ay, float az, float bx, float by, float bz, float cx, float cy, float cz) {
        a.set(ax, ay, az);
        b.set(bx, by, bz);
        c.set(cx, cy, cz);
    }

    /**
	 * Set triangle vertices.
	 *
	 * <p>
	 * Vertices must be in counter-clockwise order looking from
	 * triangle front (<i>normal</i>) side.
	 * </p>
	 *
	 * @param a coordinates of first triangle vertex
	 * @param b coordinates of second triangle vertex
	 * @param c coordinates of third triangle vertex
	 */
    public void set(float a[], float b[], float c[]) {
        set(a[0], a[1], a[2], b[0], b[1], b[2], c[0], c[1], c[2]);
    }

    /**
	 * Set triangle vertices.
	 *
	 * <p>
	 * Vertices must be in counter-clockwise order looking from
	 * triangle front (<i>normal</i>) side.
	 * </p>
	 *
	 * @param a first triangle vertex
	 * @param b second triangle vertex
	 * @param c third triangle vertex
	 */
    public void set(Point3f a, Point3f b, Point3f c) {
        set(a.x, a.y, a.z, b.x, b.y, b.z, c.x, c.y, c.z);
    }

    /**
	 * Check if point projected onto triangle plane lies inside this triangle.
	 *
	 * <p>
	 * Points lying on the triangle edge or equal to triangle vertices
	 * or very close to them are treated as being 'inside'.
	 * </p>
	 *
	 * @param p point to check
	 * @return <code>true</code> if point p lies inside this triangle or
	 *         <code>false</code> otherwise
	 */
    public boolean isInside(Point3f p) {
        Point3f pp = new Planef(a, b, c).nearest(p);
        Point3f iiA = new Point3f(a).sub(pp);
        Point3f iiB = new Point3f(b).sub(pp);
        Point3f iiC = new Point3f(c).sub(pp);
        return HMath.isClose(0.0f, iiA.length()) || HMath.isClose(0.0f, iiB.length()) || HMath.isClose(0.0f, iiC.length()) || HMath.isClose((float) Math.PI * 2.0f, iiA.angle(iiB) + iiB.angle(iiC) + iiC.angle(iiA));
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((a == null) ? 0 : a.hashCode());
        result = PRIME * result + ((b == null) ? 0 : b.hashCode());
        result = PRIME * result + ((c == null) ? 0 : c.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final Triangle3f other = (Triangle3f) obj;
        return a.equals(other.a) && b.equals(other.b) && c.equals(other.c);
    }

    /**
	 * Return common string representation of this class.
	 *
	 * @return string representation of this class
	 */
    @Override
    public String toString() {
        return "[a=" + a + " b=" + b + " c=" + c + "]";
    }
}
