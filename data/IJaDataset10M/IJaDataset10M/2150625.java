package com.choicemaker.matching.gen;

/**
 * Various utilities.
 *
 * @author    Martin Buechi
 * @version   $Revision: 1.1.1.1 $ $Date: 2009/05/03 16:03:04 $
 */
public class Utils {

    /** Returns the number of true parameter values */
    public static int trueCount(boolean b0) {
        return b0 ? 1 : 0;
    }

    /** Returns the number of true parameter values */
    public static int trueCount(boolean b0, boolean b1) {
        int count = b0 ? 1 : 0;
        if (b1) ++count;
        return count;
    }

    /** Returns the number of true parameter values */
    public static int trueCount(boolean b0, boolean b1, boolean b2) {
        int count = b0 ? 1 : 0;
        if (b1) ++count;
        if (b2) ++count;
        return count;
    }

    /** Returns the number of true parameter values */
    public static int trueCount(boolean b0, boolean b1, boolean b2, boolean b3) {
        int count = b0 ? 1 : 0;
        if (b1) ++count;
        if (b2) ++count;
        if (b3) ++count;
        return count;
    }

    /** Returns the number of true parameter values */
    public static int trueCount(boolean b0, boolean b1, boolean b2, boolean b3, boolean b4) {
        int count = b0 ? 1 : 0;
        if (b1) ++count;
        if (b2) ++count;
        if (b3) ++count;
        if (b4) ++count;
        return count;
    }
}
