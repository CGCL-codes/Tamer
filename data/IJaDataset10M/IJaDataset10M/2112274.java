package org.jquantlib.math.interpolation;

import java.util.Arrays;
import org.jquantlib.number.RealComparator;
import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.vector.Vector;

public abstract class Interpolation<T extends Real> implements Extrapolator {

    protected Vector<Real> vx;

    protected Vector<Real> vy;

    protected Interpolation(final Vector<Real> x, final Vector<Real> y) {
        this.vx = x;
        this.vy = y;
        if (this.vx.getDimension() < 2) throw new IllegalArgumentException("not enough points to interpolate");
        for (int i = 0; i < this.vx.getDimension() - 1; i++) {
            if (this.vx.get(i).isGT(this.vx.get(i + 1))) throw new IllegalArgumentException("unsorted values on array X");
        }
    }

    /**
	 * This method does not have anything to do with Observer.update()
	 */
    public abstract void update();

    protected abstract T xMin();

    protected abstract T xMax();

    protected abstract T getValue(final Real x);

    protected abstract T primitive(final Real x);

    protected abstract T derivative(final Real x);

    protected abstract T secondDerivative(final Real x);

    public final Vector<? extends Real> xValues() {
        return this.vx;
    }

    public final Vector<? extends Real> yValues() {
        return this.vy;
    }

    public final T getValue(final Real x, boolean allowExtrapolation) {
        checkRange(x, allowExtrapolation);
        return getValue(x);
    }

    protected final T primitive(final Real x, boolean allowExtrapolation) {
        checkRange(x, allowExtrapolation);
        return primitive(x);
    }

    protected final T derivative(final Real x, boolean allowExtrapolation) {
        checkRange(x, allowExtrapolation);
        return derivative(x);
    }

    protected final T secondDerivative(final Real x, boolean allowExtrapolation) {
        checkRange(x, allowExtrapolation);
        return secondDerivative(x);
    }

    protected final boolean isInRange(final Real x) {
        Real x1 = xMin();
        Real x2 = xMax();
        return (x.isGT(x1) && x.isLE(x2));
    }

    protected final void checkRange(final Real x, boolean extrapolate) {
        if (!(extrapolate || allowsExtrapolation() || isInRange(x))) {
            StringBuilder sb = new StringBuilder();
            sb.append("interpolation range is [");
            sb.append(xMin()).append(", ").append(xMax());
            sb.append("]: extrapolation at ");
            sb.append(x);
            sb.append(" not allowed");
            throw new IllegalArgumentException(sb.toString());
        }
    }

    /**
	 * Searches the specified array of T for the specified value using the
	 * binary search algorithm. The array must be sorted (as by the
	 * sort(T[]) method) prior to making this call. If it is not sorted,
	 * the results are undefined. If the array contains multiple elements with
	 * the specified value, there is no guarantee which one will be found. This
	 * method considers all NaN values to be equivalent and equal.
	 * 
	 * @return index of the search key, if it is contained in the array;
	 *         otherwise, (-(insertion point) - 1). The insertion point is
	 *         defined as the point at which the key would be inserted into the
	 *         array: the index of the first element greater than the key, or
	 *         a.length if all elements in the array are less than the specified
	 *         key. Note that this guarantees that the return value will be >= 0
	 *         if and only if the key is found.
	 *         
	 * @see Arrays#binarySearch(Object[], Object)
	 */
    private final int binarySearch(final Real x) {
        int len = this.vx.getDimension();
        Real[] objs = new Real[len];
        for (int i = 0; i < len; i++) {
            objs[i] = this.vx.get(i);
        }
        return Arrays.binarySearch(objs, x, new RealComparator<Real>());
    }

    protected final int locate(final Real x) {
        return Math.abs(binarySearch(x));
    }

    /**
	 * Implements multiple inheritance via delegate pattern to an inner class
	 * 
	 * @see Extrapolator
	 */
    private DefaultExtrapolator delegatedExtrapolator = new DefaultExtrapolator();

    public final boolean allowsExtrapolation() {
        return delegatedExtrapolator.allowsExtrapolation();
    }

    public void disableExtrapolation() {
        delegatedExtrapolator.disableExtrapolation();
    }

    public void enableExtrapolation() {
        delegatedExtrapolator.enableExtrapolation();
    }
}
