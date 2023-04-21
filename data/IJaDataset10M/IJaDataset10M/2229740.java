package org.icehockeymanager.ihm.game.match.simpleagentengine.math;

/**
 * The Class Util.
 */
public abstract class Util {

    /**
   * Clamp.
   * 
   * @param arg the arg
   * @param minVal the min val
   * @param maxVal the max val
   * 
   * @return the double
   */
    public static double clamp(double arg, double minVal, double maxVal) {
        if (arg < minVal) {
            return minVal;
        }
        if (arg > maxVal) {
            return maxVal;
        }
        return arg;
    }
}
