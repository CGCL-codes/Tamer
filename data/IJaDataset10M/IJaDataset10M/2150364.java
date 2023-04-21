package jpicedt.util.math;

/** 
 * @author <a href="mailto:vincentb1@users.sourceforge.net">Vincent Bela�che</a>
 * @since jPicEdt 1.6
 */
public class Interval implements Cloneable {

    double min;

    double max;

    public Interval clone() {
        return new Interval(min, max);
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public boolean isEmpty() {
        return max < min;
    }

    public Interval(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public Interval(Interval x) {
        min = x.min;
        max = x.max;
    }

    public Interval(double[] a) {
        min = a[0];
        max = a[1];
    }

    public Interval() {
        this(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    /**
	 * Affecte les bornes de l'intervalle.
	 *
	 * @param min une valeur <code>double</code> donnant la nouvelle borne min
	 * de l'intervalle
	 * @param max une valeur <code>double</code> donnant la nouvelle borne max
	 * de l'intervalle
	 */
    public void set(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public void setToR() {
        min = Double.NEGATIVE_INFINITY;
        max = Double.POSITIVE_INFINITY;
    }

    public void setToEmpty() {
        min = Double.POSITIVE_INFINITY;
        max = Double.NEGATIVE_INFINITY;
    }

    public boolean equals(Interval x) {
        return min == x.min && max == x.max;
    }

    public boolean contains(double x) {
        return min <= x && x <= max;
    }

    public boolean intersects(Interval other) {
        return min <= other.max && max >= other.min;
    }

    public Interval intersect(Interval other) {
        if (min < other.min) min = other.min;
        if (max > other.max) max = other.max;
        return this;
    }

    public Interval cloneIntersect(Interval other) {
        return new Interval(this).intersect(other);
    }

    public String toString() {
        String ret = new String("[" + min + ", " + max + "]");
        return ret;
    }
}
