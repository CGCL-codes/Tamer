package org.encog.ml;

/**
 * Defines a Machine Learning Method that can be reset to an untrained 
 * starting point.  Most weight based machine learning methods, such
 * as neural networks support this.  Support vector machines do not.
 */
public interface MLResettable extends MLMethod {

    /**
	 * Reset the weights.
	 */
    void reset();

    /**
	 * Reset the weights with a seed.
	 * @param seed The seed value.
	 */
    void reset(int seed);
}
