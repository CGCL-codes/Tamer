package de.kumpe.hadooptimizer;

import org.apache.commons.math.random.RandomGenerator;

/**
 * An {@link Optimizer} tries to optimize a given population of individuals to
 * improve their fitness. The concrete optimization technique depends on the
 * subclasses implementation.
 * <p>
 * Subclasses need to implement this optimization technique inside
 * {@link #optimize()} and should override {@link #getConfiguration()} to cast
 * the result to the appropriate {@link OptimizerConfiguration}'s subtype.
 * 
 * <p>
 * The implementations are generally considered to be <b>not thread-safe</b> and
 * <b>not reusable</b>. Every instance should only be used for one optimization.
 * And if the subclass' instance can be reused, external synchronization should
 * be used to ensure that only one optimization runs on a given instance.
 * 
 * @param <I>
 *            the individuals' type
 * 
 * @author <a href="http://kumpe.de/christian/java">Christian Kumpe</a>
 */
public abstract class Optimizer<I> {

    private final OptimizerConfiguration<I> configuration;

    private final RandomGenerator randomGenerator;

    /**
	 * Creates a new {@link Optimizer} object with the given
	 * {@link OptimizerConfiguration}.
	 * <p>
	 * The configuration will be {@link OptimizerConfiguration#clone() cloned}
	 * and {@link OptimizerConfiguration#validate() validated}.
	 * 
	 * @param configuration
	 *            the new optimizer's configuration
	 * 
	 * 
	 * @throws NullPointerException
	 *             if {@code configuration} is <code>null</code>
	 * @throws IllegalStateException
	 *             if the given configuration is not valid
	 */
    public Optimizer(final OptimizerConfiguration<I> configuration) {
        this.configuration = configuration.clone();
        this.configuration.validate();
        this.randomGenerator = this.configuration.getRandomGeneratorFactory().createRandomGenerator(System.currentTimeMillis());
    }

    /**
	 * Returns the optimizer's configuration. This configuration should not be
	 * modified once the optimizer has been created.
	 * <p>
	 * This method can be overridden to cast the {@link OptimizerConfiguration}
	 * to the appropriate subtype for the concrete optimizer implementation.
	 * 
	 * @return the optimizer's configuration
	 */
    protected OptimizerConfiguration<I> getConfiguration() {
        return configuration;
    }

    protected RandomGenerator getRandomGenerator() {
        return randomGenerator;
    }

    /**
	 * Starts the optimization.
	 * <p>
	 * Subclasses need to implement this method with a concrete optimization
	 * technique.
	 * 
	 * @throws OptimizerException
	 *             if a checked exception occurs inside the optimization
	 */
    public abstract void optimize();
}
