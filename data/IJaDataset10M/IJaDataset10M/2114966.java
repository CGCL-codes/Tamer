package org.lindenb.util;

/**
 * @author lindenb
 *
 */
public class Couple<T> extends Pair<T, T> {

    /**
	 * @param a
	 * @param b
	 */
    public Couple(T a, T b) {
        super(a, b);
    }

    public boolean contains(T o) {
        return first().equals(o) || second().equals(o);
    }

    public T getComplement(T a) {
        if (first().equals(a)) {
            return second();
        } else if (second().equals(a)) {
            return first();
        }
        return null;
    }

    @Override
    public int hashCode() {
        return 31 + (first().hashCode() + second().hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || !(obj instanceof Couple)) return false;
        Couple<?> cp = Couple.class.cast(obj);
        return (first().equals(cp.first()) && second().equals(cp.second())) || (first().equals(cp.second()) && second().equals(cp.first()));
    }

    @Override
    public String toString() {
        String a = first().toString();
        String b = second().toString();
        if (a.compareTo(b) < 0) {
            return "(" + a + "," + b + ")";
        }
        return "(" + b + "," + a + ")";
    }
}
