package de.helwich.math.symbolic.node;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Implement some operations which are missing in the GWT API.
 * 
 * @author Hendrik Helwich
 *
 */
class NoGWT {

    private NoGWT() {
    }

    static BigDecimal createBigDecimal(BigInteger i) {
        return new BigDecimal(i.toString());
    }

    static BigInteger gcd(BigInteger a, BigInteger b) {
        if (BigInteger.ZERO.equals(a)) return b;
        a = a.abs();
        b = b.abs();
        while (!BigInteger.ZERO.equals(b)) if (a.compareTo(b) > 0) a = a.subtract(b); else b = b.subtract(a);
        return a;
    }
}
