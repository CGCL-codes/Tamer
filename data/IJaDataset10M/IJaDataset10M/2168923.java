package extlib;

/**
 * REF. :
 * Base pair class.
 * <a href="http://myjavatools.com/projects/Category/source.zip">here</a>
 */
public class Pair<X, Y> {

    private final X x;

    private final Y y;

    public X x() {
        return x;
    }

    public Y y() {
        return y;
    }

    private Pair(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int hashCode() {
        return x.hashCode() * 2 + y.hashCode();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Pair<X, Y> other = (Pair<X, Y>) obj;
        if (this.x != other.x && (this.x == null || !this.x.equals(other.x))) {
            return false;
        }
        if (this.y != other.y && (this.y == null || !this.y.equals(other.y))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public static <X, Y> Pair<X, Y> pair(X x, Y y) {
        return new Pair<X, Y>(x, y);
    }

    public static <X> Pair<X, X> pair(X[] source) {
        assert source.length == 2 : "Pair is built on a two-element array; got " + source.length;
        return pair(source[0], source[1]);
    }
}
