package net.sourceforge.epoint.util;

import java.math.BigInteger;

/**
 * Prime number generator using ArcFour
 *
 * @author <a href="mailto:nagydani@users.sourceforge.net">Daniel A. Nagy</a>
 */
public class ArcFourPrime extends BigInteger {

    private static byte[] genPrime(int l, ArcFourRandom rand) {
        byte[] b = new byte[l];
        do {
            rand.nextBytes(b);
            b[l - 1] |= 3;
            b[0] |= 0x80;
        } while (!new BigInteger(1, b).isProbablePrime(112));
        return b;
    }

    public ArcFourPrime(int l, ArcFourRandom rand) {
        super(1, genPrime(l, rand));
    }
}
