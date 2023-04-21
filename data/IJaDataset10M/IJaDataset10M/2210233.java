package math.linearalgebra;

/**
 * Represents a vector with one element removed.
 * @author egoff
 *
 * @param <T>
 */
public final class RemoveElementVector<T> implements Vector<T> {

    private static final long serialVersionUID = 1L;

    private final Vector<T> x;

    private final int removal;

    /**
	 * Constructor
	 * @param x The vector to remove from
	 * @param removal The element to remove
	 */
    public RemoveElementVector(Vector<T> x, int removal) {
        this.x = x;
        this.removal = removal;
    }

    public int size() {
        return x.size() - 1;
    }

    public T get(int i) {
        return i < this.removal ? x.get(i) : x.get(i + 1);
    }

    @Override
    public String toString() {
        return "RemoveElementVector(" + removal + "," + x + ")";
    }
}
