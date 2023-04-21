package net.sourceforge.jabm.util;

import java.io.Serializable;
import cern.jet.random.AbstractContinousDistribution;

/**
 * A random variate that takes on the absolute value of values drawn
 * from an underlying probability distribution.
 * 
 * @author Steve Phelps
 */
public class AbsoluteContinuousDistribution extends AbstractContinousDistribution implements Serializable {

    protected AbstractContinousDistribution underlyingDistribution;

    public AbsoluteContinuousDistribution(AbstractContinousDistribution underlyingDistribution) {
        super();
        this.underlyingDistribution = underlyingDistribution;
    }

    @Override
    public double nextDouble() {
        return Math.abs(underlyingDistribution.nextDouble());
    }
}
