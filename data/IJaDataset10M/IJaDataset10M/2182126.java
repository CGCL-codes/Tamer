package de.fzi.mapevo.operators;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import de.fzi.mapevo.algorithm.Config;
import de.fzi.mapevo.algorithm.MapEVOConfigurationException;

/**
 * {@link ProbabilityCalculator} for the probability to add a correspondence.
 * The calculator uses one configuration parameter providing an upper and lower bound
 * of probabilities. Since this class is implemented as a singleton,
 * the configuration parameter cannot be changed, once the class is initialised,
 * i.e. after the first call of {@link #getInstance()}.
 * 
 * @author Juergen Bock (bock@fzi.de)
 * @author Michael Mutter
 *
 */
public final class PsetV implements ProbabilityCalculator {

    /**
     * Logger.
     */
    private static final Log logger = LogFactory.getLog(PsetV.class);

    /**
     * Singleton instance.
     */
    private static PsetV theInstance;

    /**
     * Lower bound of the probability.
     */
    private final double lower_bound;

    /**
	 * Upper bound of the probability.
	 */
    private final double upper_bound;

    /**
     * Obtains an instance of this calculator.
     * @return An instance of this calculator.
     */
    public static PsetV getInstance() {
        if (theInstance == null) theInstance = new PsetV();
        return theInstance;
    }

    /**
     * Creates a new instance.
     * Explicit instantiation forbidden.
     * Use {@link #getInstance()} to obtain an instance.
     */
    private PsetV() {
        if (logger.isInfoEnabled()) logger.info("Instantiating " + this.getClass().getSimpleName());
        double lowerBoundParam;
        double upperBoundParam;
        try {
            lowerBoundParam = Config.getMainConfig().getPermutationSetValueProbabilityRanges()[0];
            upperBoundParam = Config.getMainConfig().getPermutationSetValueProbabilityRanges()[1];
            if (lowerBoundParam < 0.) throw new MapEVOConfigurationException("Lower bound is negativ!: " + lowerBoundParam);
            if (upperBoundParam < 0.) throw new MapEVOConfigurationException("Upper bound is negativ!: " + upperBoundParam);
            if (lowerBoundParam > 1.) throw new MapEVOConfigurationException("Lower bound is greater than 1!: " + lowerBoundParam);
            if (upperBoundParam > 1.) throw new MapEVOConfigurationException("Upper bound is greater than 1!: " + upperBoundParam);
        } catch (MapEVOConfigurationException e) {
            logger.warn("Could not process the '" + Config.PERMUTATION_SET_VALUE_PROBABILITY + "' parameter: ", e);
            lowerBoundParam = 0.2;
            upperBoundParam = 0.03;
        }
        lower_bound = lowerBoundParam;
        upper_bound = upperBoundParam;
        if (logger.isDebugEnabled()) logger.debug("pSetN: using values " + lower_bound + " " + upper_bound);
    }

    @Override
    public double getProbability(double confidence, int maxIter, int curIter) {
        logger.info("Computing probability to add correspondence.");
        if (logger.isDebugEnabled()) logger.debug("[confidence=" + confidence + ", current iteration=" + curIter + ", maximum number of interations=" + maxIter + "]");
        final double m = (upper_bound - lower_bound) / (double) maxIter;
        final double pSetV = lower_bound + m * curIter;
        if (logger.isDebugEnabled()) logger.debug("Probability to add correspondence is " + pSetV);
        return pSetV;
    }
}
