package math.linearalgebra;

import math.abstractalgebra.Ring;

/**
 * Represents the point that is the linear
 * combination of vectors in a space.
 * @author egoff
 *
 * @param <T>
 */
public class GetPointVector<T> implements Vector<T> {

    private static final long serialVersionUID = 1L;

    private final Ring<T> ring;

    private final Space<T> s;

    private final Vector<T> vec;

    /**
	 * Constructor
	 * @param ring The ring of sub elements
	 * @param s The space
	 * @param vec The coefficients of the vector.
	 */
    public GetPointVector(Ring<T> ring, Space<T> s, Vector<T> vec) {
        super();
        this.ring = ring;
        this.s = s;
        this.vec = vec;
    }

    public T get(int j) {
        Vector<T> origin = s.getOrigin();
        Matrix<T> m = s.getDirections();
        T mypt = origin.get(j);
        return ring.getAdd().getOperation().perform(mypt, LinearMath.dotProduct(ring, m.getRow(j), vec));
    }

    public int size() {
        Vector<T> origin = s.getOrigin();
        return origin.size();
    }
}
