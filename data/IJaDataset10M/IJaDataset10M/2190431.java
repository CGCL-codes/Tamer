package src.lib.objects;

import java.io.Serializable;

/**
 * This file was shared from the Endrov library, 2-clause BSD licensed, Johan Henriksson  
 * http://www.endrov.net/
 *  
 * A Java tuple (a,b)
 * 
 * This code was taken from a forum post and modified. It is the only possible
 * technical implementation and hence not copyrightable.
 * 
 * @author afejes
 * @author Johan Henriksson
 * @version $Revision: 2530 $
 */
public class Triplet<L, M, R> implements Serializable {

    private static final long serialVersionUID = 0;

    private L a;

    private M b;

    private R c;

    public Triplet(L first, M second, R third) {
        this.a = first;
        this.b = second;
        this.c = third;
    }

    public void set_first(L number) {
        a = number;
    }

    public void set_second(M number) {
        b = number;
    }

    public void set_third(R number) {
        c = number;
    }

    /**
	 * First value (a,_) -> a
	 * @return first value of tuple
	 */
    public L get_first() {
        return a;
    }

    /**
	 * Second value (_,b) -> b
	 * @return second value of tuple
	 */
    public M get_second() {
        return b;
    }

    /**
	 * Second value (_,b) -> b
	 * @return second value of tuple
	 */
    public R get_third() {
        return c;
    }

    public final boolean equals(Object o) {
        if (!(o instanceof Triplet<?, ?, ?>)) {
            return false;
        }
        final Triplet<?, ?, ?> other = (Triplet<?, ?, ?>) o;
        return equal(get_first(), other.get_first()) && equal(get_second(), other.get_second()) && equal(get_third(), other.get_third());
    }

    public static final boolean equal(Object o1, Object o2) {
        if (o1 == null) {
            return o2 == null;
        } else {
            return o1.equals(o2);
        }
    }

    public static <L, M, R> Triplet<L, M, R> make(L a, M b, R c) {
        return new Triplet<L, M, R>(a, b, c);
    }

    public String toString() {
        return "(" + a + "," + b + "," + c + ")";
    }
}
