package utils.linear;

/**
 *
 * @author Calvin Ashmore
 */
public class LVect3i {

    public int x, y, z;

    public LVect3i() {
        x = 0;
        y = 0;
        z = 0;
    }

    public LVect3i(LVect3i a) {
        this.x = a.x;
        this.y = a.y;
        this.z = a.z;
    }

    public LVect3i(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public LVect3i zero() {
        return new LVect3i();
    }

    public LVect3i add(LVect3i a) {
        return new LVect3i(x + a.x, y + a.y, z + a.z);
    }

    public void addv(LVect3i a) {
        x += a.x;
        y += a.y;
        z += a.z;
    }

    public LVect3i sub(LVect3i a) {
        return new LVect3i(x - a.x, y - a.y, z - a.z);
    }

    public void subv(LVect3i a) {
        x -= a.x;
        y -= a.y;
        z -= a.z;
    }

    public LVect3i mult(int c) {
        return new LVect3i(c * x, c * y, c * z);
    }

    public void multv(int c) {
        x *= c;
        y *= c;
        z *= c;
    }

    public LVect3i normal() {
        return null;
    }

    public void normalv() {
    }

    public int dot(LVect3i v) {
        return v.x * x + v.y * y + v.z * z;
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    @Override
    public String toString() {
        return "< " + x + " , " + y + " , " + z + " >";
    }

    public static LVect3i parse(String s) {
        s = s.replaceAll("[<> ]", "");
        String sa[] = s.split(",");
        return new LVect3i(Integer.parseInt(sa[0]), Integer.parseInt(sa[1]), Integer.parseInt(sa[2]));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LVect3i other = (LVect3i) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        if (this.z != other.z) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash ^= 73856093 * this.x;
        hash ^= 19349663 * this.y;
        hash ^= 83492791 * this.z;
        return hash;
    }
}
