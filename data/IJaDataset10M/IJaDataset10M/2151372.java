package utils.linear;

/**
 *
 * @author Calvin Ashmore
 */
public class LVect3d implements Linear<LVect3d> {

    public double x, y, z;

    public LVect3d() {
        x = 0;
        y = 0;
        z = 0;
    }

    public LVect3d(LVect3d a) {
        this.x = a.x;
        this.y = a.y;
        this.z = a.z;
    }

    public LVect3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public LVect3d zero() {
        return new LVect3d();
    }

    public LVect3d add(LVect3d a) {
        return new LVect3d(x + a.x, y + a.y, z + a.z);
    }

    public void addv(LVect3d a) {
        x += a.x;
        y += a.y;
        z += a.z;
    }

    public LVect3d sub(LVect3d a) {
        return new LVect3d(x - a.x, y - a.y, z - a.z);
    }

    public void subv(LVect3d a) {
        x -= a.x;
        y -= a.y;
        z -= a.z;
    }

    public LVect3d mult(double c) {
        return new LVect3d(c * x, c * y, c * z);
    }

    public void multv(double c) {
        x *= c;
        y *= c;
        z *= c;
    }

    public LVect3d normal() {
        double m = 1.0 / magnitude();
        return new LVect3d(m * x, m * y, m * z);
    }

    public void normalv() {
        double m = 1.0 / magnitude();
        x *= m;
        y *= m;
        z *= m;
    }

    public double dot(LVect3d v) {
        return v.x * x + v.y * y + v.z * z;
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public static LVect3d cross(LVect3d a, LVect3d b) {
        return new LVect3d(a.y * b.z - a.z * b.y, a.z * b.x - a.x * b.z, a.x * b.y - a.y * b.x);
    }

    @Override
    public String toString() {
        return "< " + x + " , " + y + " , " + z + " >";
    }

    public static LVect3d parse(String s) {
        s = s.replaceAll("[<> ]", "");
        String sa[] = s.split(",");
        return new LVect3d(Double.parseDouble(sa[0]), Double.parseDouble(sa[1]), Double.parseDouble(sa[2]));
    }

    @Override
    public final LVect3d clone() {
        return new LVect3d(this);
    }

    public void setTo(LVect3d a) {
        this.x = a.x;
        this.y = a.y;
        this.z = a.z;
    }
}
