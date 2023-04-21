package soc.util;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.5 $
 */
public class Pair {

    private Object a;

    private Object b;

    /**
     * Creates a new Pair object initialized with null values.
     */
    public Pair() {
        this(null, null);
    }

    /**
     * Creates a new Pair object.
     *
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     */
    public Pair(Object i, Object j) {
        a = i;
        b = j;
    }

    /**
     * returns a hash code for the object
     *
     * @return the hash code
     */
    public int hashCode() {
        return ((a == null) ? 0 : a.hashCode()) ^ ((b == null) ? 0 : b.hashCode());
    }

    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object o) {
        if (o instanceof Pair) {
            Pair ip = (Pair) o;
            if (((ip.a == a) && (ip.b == b)) || ((ip.a == b) && (ip.b == a))) {
                return true;
            }
        }
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getA() {
        return a;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getB() {
        return b;
    }

    /**
     * DOCUMENT ME!
     *
     * @param val DOCUMENT ME!
     */
    public void setA(Object val) {
        a = val;
    }

    /**
     * DOCUMENT ME!
     *
     * @param val DOCUMENT ME!
     */
    public void setB(Object val) {
        b = val;
    }
}
