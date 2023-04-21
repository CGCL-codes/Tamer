package qs;

import java.math.BigInteger;
import java.io.*;

/**
 *  Objects of this class encapsulate the constants, for
 *  quadratic sieve work units of a given factorisation.
 * 
 *  @author Matthew Painter
 *  @author Jonathan Knowles
 */
public class QSWorkUnitConstants implements Serializable {

    protected BigInteger n;

    protected BigInteger[] factorBase;

    protected float logError;

    protected float[] logF;

    protected long maxLargePrime;

    protected int smallPrime;

    protected int M;

    protected int[] sqrtNmodP;

    protected static final BigInteger ZERO = new BigInteger("0");

    protected static final BigInteger ONE = new BigInteger("1");

    protected static final BigInteger TWO = new BigInteger("2");

    protected static final BigInteger THREE = new BigInteger("3");

    protected static final BigInteger FOUR = new BigInteger("4");

    protected static final BigInteger FIVE = new BigInteger("5");

    protected static final BigInteger SIX = new BigInteger("6");

    protected static final BigInteger SEVEN = new BigInteger("7");

    protected static final BigInteger EIGHT = new BigInteger("8");

    public int hashCode() {
        return n.hashCode();
    }

    protected QSWorkUnitConstants() {
    }
}
