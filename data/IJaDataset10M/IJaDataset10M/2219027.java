package org.redwood.business.report.util;

import java.io.Serializable;

/**
 * This class helps to save class limits of classes of a classifications measure.
 *  The boundaries are long values.
 *
 * @author  Gerrit Franke
 * @version 1.0
 */
public class ClassLimits implements Serializable {

    /** The lower class limit. */
    long lowerLimit;

    /** The upper class limit.*/
    long upperLimit;

    /** Constructor.
   *  @param lowerLimit  The lower class limit.
   *  @param upperLimit  The upper class limit.
   */
    public ClassLimits(long lowerLimit, long upperLimit) {
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
    }

    /** 
   * Returns the lower class limit
   * 
   * @return      The lower class limit
   */
    public long getLowerLimit() {
        return lowerLimit;
    }

    /** 
   * Returns the upper class limit
   * 
   * @return      The upper class limit
   */
    public long getUpperLimit() {
        return upperLimit;
    }
}
