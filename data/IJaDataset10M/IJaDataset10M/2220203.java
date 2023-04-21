package org.encog.neural.networks.training;

/**
 * Specifies that a training algorithm has the concept of a learning rate.
 * This allows it to be used with strategies that automatically adjust the
 * learning rate.
 * 
 * @author jheaton
 *
 */
public interface LearningRate {

    /**
	 * Set the learning rate.
	 * @param rate The new learning rate
	 */
    void setLearningRate(double rate);

    /**
	 * @return The learning rate.
	 */
    double getLearningRate();
}
