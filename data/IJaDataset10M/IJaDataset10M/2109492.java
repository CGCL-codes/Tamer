package org.omegahat.Simulation.RandomGenerators;

/** 
 * Contains all of the magic constants necessary to implement
 * Bruce Collings' Pseudorandom Number Generator.
 *
 * @author  Greg Warnes
 * @author  $Author: warneg $
 * @version $Revision: 2 $, $Date: 2001-04-04 13:16:08 -0400 (Wed, 04 Apr 2001) $
 */
public class CollingsPRNGConstants {

    /** Prime Modulus Constant = 2^32-1 */
    public static int Modulus = 2147483647;

    /** Number of good multiplier constants in the pool */
    public static int SIZE = 112;

    /** Pool of (empirically) good multiplicative constants */
    public static int[] pool = { 28070, 28264, 28459, 30553, 30921, 31707, 32022, 32382, 32990, 33159, 33469, 33640, 33948, 34303, 34873, 35116, 35123, 35129, 35299, 36429, 36885, 37366, 37509, 37597, 37615, 37649, 38256, 38393, 38542, 38954, 39360, 40945, 41255, 41536, 41558, 41939, 42289, 42483, 43337, 43478, 44067, 44085, 44370, 44625, 45213, 45915, 46281, 46316, 46324, 46695, 47067, 47324, 47362, 47447, 47536, 47855, 48030, 48567, 49002, 49187, 49573, 49762, 50002, 50197, 50567, 50658, 50803, 51081, 51270, 51288, 51473, 51784, 51808, 52639, 52744, 53032, 53147, 53341, 53372, 53831, 53894, 54499, 54764, 54819, 55212, 55485, 55531, 55543, 55632, 56377, 57759, 57857, 58141, 58151, 58545, 58861, 60097, 60332, 60743, 61287, 61916, 62233, 62463, 63357, 63615, 63848, 63928, 64378, 64787, 64816, 65123, 65285 };
}
