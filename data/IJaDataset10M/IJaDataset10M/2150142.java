package com.ail.util;

import com.ail.core.Type;

/**
 * This class represents a percentage value based on a double.
 * todo A rate should be between 0 and 100 not 0 and 1
 * todo The rate should be represented as a BigDecimal - not a double.
 * @deprecated Use com.ail.util.Rate instead.
 * @version $Revision: 1.1 $
 * @state $State: Exp $
 * @date $Date: 2005/08/16 21:08:54 $
 * @source $Source: /home/bob/CVSRepository/projects/common/commercial.ear/commercial.jar/com/ail/util/Percent.java,v $
 * @stereotype type
 */
public class Percent extends Type {

    static final long serialVersionUID = -2314786106419874334L;

    private double rate;

    /**
     * Default constructor
     */
    public Percent() {
    }

    /**
     * Initialising constructor
     * @param rate The rate to be represented
     * @throws IllegalArgumentException If rate is not >=0 && <=1
     */
    public Percent(double rate) throws IllegalArgumentException {
        setRate(rate);
    }

    /**
     * Get the current rate.
     * @return Rate as a double
     */
    public double getRate() {
        return rate;
    }

    /**
     * Set the rate.
     * @param rate New rate.
     * @throws IllegalArgumentException If rate argument is not >=0 && <=1
     */
    public void setRate(double rate) throws IllegalArgumentException {
        if (rate < 0 || rate > 100.0) {
            throw new IllegalArgumentException("Percent value can only represent 0 <= value <= 100 (value was " + rate + ")");
        }
        this.rate = rate;
    }

    /**
     * Apply this percentage to a value
     * @param value The value to apply to
     * @return The result
     */
    public double applyTo(double value) {
        return value * (rate / 100.0);
    }
}
