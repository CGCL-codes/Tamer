package com.rapidminer.tools.math.distribution;

import com.rapidminer.tools.Tools;

/**
 * The distribution for a continous variable.
 * 
 * @author Tobias Malbrecht
 */
public abstract class ContinuousDistribution implements Distribution {

    /**
	 * 
	 */
    private static final long serialVersionUID = 6775492269986383673L;

    public final boolean isDiscrete() {
        return false;
    }

    public final boolean isContinuous() {
        return true;
    }

    public abstract double getProbability(double x);

    /**
	 * This method returns a lower bound of values. This bound
	 * should be given by the distributions tail, for example bounds
	 * should contain 95% interval. Nominal distributions should
	 * return NaN.
	 */
    public abstract double getLowerBound();

    /** 
	 * This method returns an upper bound of possible values. This
	 * bound should be given by the distributions tail, for example
	 * bounds should contain 95% interval. Nominal distributions
	 * should return NaN.
	 */
    public abstract double getUpperBound();

    public String mapValue(double value) {
        return Tools.formatNumber(value);
    }
}
