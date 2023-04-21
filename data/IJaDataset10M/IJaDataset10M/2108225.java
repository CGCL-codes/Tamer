package joelib2.gui.render3D.math.util;

/**
 * This class provides some math utility methods.
 *
 * @.author    Mike Brusati
 * @.author     wegnerj
 * @.license    GPL
 * @.cvsversion    $Revision: 1.6 $, $Date: 2005/02/17 16:48:33 $
 */
public class MathUtils {

    /**
     * Perform a division of the input integers, and round to the next integer
     * if the divisor is not a even multiple of the dividend.
     *
     * @param dividend  the number to be divided
     * @param divisor   the number by which to divide
     * @return          the result of the division, with possible rounding
     */
    public static int divideAndRound(int dividend, int divisor) {
        int result = 0;
        if (divisor != 0) {
            result = ((dividend % divisor) == 0) ? (dividend / divisor) : ((dividend / divisor) + 1);
        }
        return result;
    }
}
