package nl.adaptivity.lang;

/**
 * A utility class for wrapping primitives.
 *
 * @author Paul de Vrieze
 * @version 0.1 $Revision: 483 $
 */
public final class Wrapper {

    private Wrapper() {
    }

    public static Integer w(final int pI) {
        return new Integer(pI);
    }

    public static Double w(final double pD) {
        return new Double(pD);
    }

    public static Boolean w(final boolean pB) {
        return Boolean.valueOf(pB);
    }

    public static Character w(final char pC) {
        return new Character(pC);
    }

    public static Long w(final long pL) {
        return new Long(pL);
    }
}
