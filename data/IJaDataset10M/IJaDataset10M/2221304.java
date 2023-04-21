package clustering.framework;

/**
 * @author Tudor.Ionescu@supelec.fr
 * Distance Matrix Element Class
 */
public class DMElement implements Comparable {

    public int i = -1, j = -1;

    public double dist = -1;

    public DMElement(int i, int j, double dist) {
        this.i = i;
        this.j = j;
        this.dist = dist;
    }

    public int compareTo(Object dme) {
        if (dist < ((DMElement) dme).dist) {
            return 1;
        } else if (dist > ((DMElement) dme).dist) {
            return -1;
        }
        return 0;
    }

    public boolean equals(Object dme) {
        if (((DMElement) dme).i == i && ((DMElement) dme).j == j) return true;
        if (((DMElement) dme).i == j && ((DMElement) dme).j == i) return true;
        return false;
    }
}
