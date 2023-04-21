package jfun.yan.xml.nuts.optional;

/**
 * The Nut class to verify that two objects are equal.
 * <p>
 * @author Ben Yu
 * Nov 9, 2005 11:51:38 PM
 */
public class AssertEqualNut extends BinaryAssertionNut {

    public void assertion(Object v1, Object v2) {
        if (v1 == null && v2 != null || !v1.equals(v2)) throw raise("" + v1 + " != " + v2);
    }
}
