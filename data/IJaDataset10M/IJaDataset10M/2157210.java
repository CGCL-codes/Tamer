package common;

/**
 *
 * @author Jan-Philipp Kappmeier
 */
public class Tuple<U, V> {

    public U u;

    public V v;

    public Tuple(U u, V v) {
        this.u = u;
        this.v = v;
    }

    public U getU() {
        return u;
    }

    public V getV() {
        return v;
    }

    @Override
    public String toString() {
        return "(" + u + ',' + v + ')';
    }
}
