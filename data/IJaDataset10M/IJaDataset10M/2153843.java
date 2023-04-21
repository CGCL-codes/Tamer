package math.abstractalgebra;

public class AbstractMath {

    /**
	 * Raises the argument to the given integral power
	 * using operations of the given group.
	 * @param <T> Type of the elements
	 * @param grp The group to use for "multiplication"
	 * @param x The value to take the power of
	 * @param exponent The exponent
	 * @return x ** exponent
	 */
    public static <T> T pow(Group<T> grp, T x, long exponent) {
        if (exponent < 0) {
            return grp.getInversion().perform(pow(grp, x, -exponent));
        }
        if (exponent == 0) {
            return grp.getIdentity();
        }
        if (exponent == 1) {
            return x;
        }
        T v2 = pow(grp, x, exponent / 2);
        v2 = grp.getOperation().perform(v2, v2);
        if (exponent % 2 == 1) {
            v2 = grp.getOperation().perform(v2, x);
        }
        return v2;
    }

    /**
	 * Compares two elements in an ordered ring.
	 * @param <T> Type of the elements
	 * @param ring Ordered ring of elements
	 * @param x left
	 * @param y right
	 * @return -1 if x < y 0 else if x == y else 1
	 */
    public static <T> int compare(OrderedRing<T> ring, T x, T y) {
        if (ring.lessThanOrEqual(x, y)) {
            if (ring.lessThanOrEqual(y, x)) {
                return 0;
            } else {
                return -1;
            }
        } else {
            return 1;
        }
    }

    public static <T> T subtract(Ring<T> ring, T x, T y) {
        return ring.getAdd().getOperation().perform(x, ring.getAdd().getInversion().perform(y));
    }
}
