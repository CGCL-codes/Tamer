package org.freeworld.prilib.util;

import java.io.Serializable;

/**
 * <p>The rounding policy allows a developer ot make very precise decisions on
 * how number entry and display rounding are performed.</p>
 * 
 * @author dchemko
 */
public class RoundingPolicy implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * No limit can be used in calculating how many decimals are being dropped
    * as well as how many decimals to be displayed by the output renderer. This
    * is the default value for both cases.
    */
    public static final int NO_LIMIT = Integer.MIN_VALUE;

    private String roundingName = null;

    private int truncateDecimalCount = NO_LIMIT;

    private float roundUpThreshold = 0.5F;

    private float roundToIncrement = 1.0F;

    private float postProcessPlusMinus = 0.0F;

    private int displayDecimalCount = NO_LIMIT;

    /**
    * <p>Sets the friendly name of the rounding so that it can be selected
    * graphically and reused later.</p>
    * 
    * @param roundingName - The name of the rounding
    */
    public void setRoundingName(String roundingName) {
        this.roundingName = roundingName;
    }

    /**
    * <p>Fetches the name of this rounding</p>
    * 
    * @return The name of the rounding or null if a name wasn't specified
    */
    public String getRoundingName() {
        return roundingName;
    }

    /**
    * <p>Specifes the number of decimals dropped on the floor when input
    * manually by a setter. If the number of decimals excedes this limit,
    * those decimals will be truncated before the rounding is calculated.
    * If numbers are negative, the number is not shrunk, but each base 10
    * component is converted to 0 from its original value.</p>
    * 
    * <p>The truncation uses base 10 to determine decimal locations</p>
    * 
    * <p>Eg.
    * <pre> IN: 12345678.12345678
    * truncateDecimalCount == 2
    * OUT: 12345678.12
    * truncateDecimalCount == 0
    * OUT: 12345678
    * truncateDecimalCount == -2
    * OUT: 12345600
    * truncateDecimalCount == NO_LIMIT
    * OUT: 12345678.12345678</pre></p>
    * @param truncateDecimalCount - The number of decimals to truncate from the end of the number
    */
    public void setTruncateDecimalCount(int truncateDecimalCount) {
        this.truncateDecimalCount = truncateDecimalCount;
    }

    /**
    * <p>Fetches the truncation decimal count</p>
    * 
    * @return The number of decimals retained by the truncation. See
    * {@link #setTruncateDecimalCount(int)} for the truncation formula.
    */
    public int getTruncateDecimalCount() {
        return truncateDecimalCount;
    }

    /**
    * <p>Specifed at what threshold the rounding should round up or down. The
    * input float is used and the vald number range is 0.0 for always round
    * down to 1.0 for always round up. The rounding is determined by the
    * roundToIncrement.</p>
    * 
    * <p>The algorithm for the threshold goes like this: <b><pre>if(Threshold > 0.0 && IN%Increment >= Increment*Threshold) then round up</pre></b></p>
    * 
    * <p>Eg.
    * <pre> IN == 100.111
    * Increment == 25, Threshold == 0.0
    * OUT == 100
    * Increment == 25, Threshold == 0.0000000000000001
    * OUT == 125
    * Increment == 0.111, Threshold == 1.0
    * OUT == 100.222
    * 
    * Also
    * 
    * IN == 155
    * Increment == 50, Threshold == 0.1
    * OUT == 200
    * Increment == 50, Threshold == 0.09
    * OUT == 200
    * Increment == 50, Threshold == 0.11
    * OUT == 150
    * </pre></p>
    * 
    * @param roundUpThreshold
    */
    public void setRoundUpThreshold(float roundUpThreshold) {
        this.roundUpThreshold = roundUpThreshold;
    }

    /**
    * <p>Fetches the round up threshold</p>
    * 
    * @return The rounding threshold of the rounding policy
    */
    public float getRoundUpThreshold() {
        return roundUpThreshold;
    }

    /**
    * <p>Sets the rounding increment of the rounding algorithm. When the
    * rounding is complete, the resulting internal number should always be
    * divisible by this number.</p>
    * 
    * <p>See {@link #setRoundToIncrement(float)} on how the rounding
    * algorithm is calculated.</p>
    * 
    * @param roundToIncrement - The increment used within the rounding
    * algorithm
    */
    public void setRoundToIncrement(float roundToIncrement) {
        this.roundToIncrement = roundToIncrement;
    }

    /**
    * <p>Fetches the rounding increment used within the rounding algorthm</p>
    * 
    * @return The rounding increment used by the rounding algorithm
    */
    public float getRoundToIncrement() {
        return roundToIncrement;
    }

    /**
    * <p>Sets a one-time number adjustment to be performed after the rounding
    * is calculated. This is for times when your increment should be a whole
    * number, like $1, $2, but you always want that nice $x.99 price tag.</p>
    * 
    * <p><pre> Eg. IN 5
    * PlusMinus: -0.3
    * OUT 4.97
    * PlusMinus: 4
    * OUT 9</pre></p>
    * 
    * @param postProcessPlusMinus - The number added to the end result of the
    * rounding
    */
    public void setPostProcessPlusMinus(float postProcessPlusMinus) {
        this.postProcessPlusMinus = postProcessPlusMinus;
    }

    /**
    * <p>Fetches the one time addition to the number which is performed after
    * the number is rounded</p>
    * 
    * @return The number added to the number specified after rounding occurs
    */
    public float getPostProcessPlusMinus() {
        return postProcessPlusMinus;
    }

    /**
    * <p>Specified how many decimals should be visible to the end user when
    * displayed on typical renderers. This can be overridden by specialy GUI
    * components if the true raw value of the number is desired.</p>
    * 
    * @param displayDecimalCount - The number of decimal places to render on a
    * typical user input component
    */
    public void setDisplayDecimalCount(int displayDecimalCount) {
        this.displayDecimalCount = displayDecimalCount;
    }

    /**
    * <p>Fetches the number of decimals that should be rendered to a user
    * interface</p>
    * 
    * @return The number of decimals to render to the user interface for this
    * value / column
    */
    public int getDisplayDecimalCount() {
        return displayDecimalCount;
    }

    /**
    * <p>Creates an exact duplicate of the routing</p>
    * 
    * @return A new copy of this object
    */
    public RoundingPolicy newCopy() {
        RoundingPolicy retr = new RoundingPolicy();
        retr.setRoundingName(getRoundingName());
        retr.setTruncateDecimalCount(getTruncateDecimalCount());
        retr.setRoundUpThreshold(getRoundUpThreshold());
        retr.setRoundToIncrement(getRoundToIncrement());
        retr.setPostProcessPlusMinus(getPostProcessPlusMinus());
        retr.setDisplayDecimalCount(getDisplayDecimalCount());
        return retr;
    }
}
