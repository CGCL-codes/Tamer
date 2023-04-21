package uk.co.silentsoftware.core.converters.image.orderedditherstrategy;

/**
 * Special case of a magic square which has horizontal, vertical 
 * and diagonal totals all equal.
 * 
 * An example of this dither is at http://en.wikipedia.org/wiki/Magic_square
 */
public class NasikMagicSquareDitherStrategy extends AbstractOrderedDitherStrategy implements OrderedDitherStrategy {

    private static final int[] COEFFS = new int[] { 0, 56, 12, 52, 44, 20, 32, 24, 48, 8, 60, 4, 28, 36, 16, 40 };

    public int[] getCoefficients() {
        return COEFFS;
    }

    public int getMatrixWidth() {
        return 4;
    }

    public int getMatrixHeight() {
        return 4;
    }

    public String toString() {
        return "Magic Square 4x4 (Nasik)";
    }
}
