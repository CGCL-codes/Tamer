package de.fzi.mappso.particlecluster;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import de.fzi.mappso.align.Config;
import de.fzi.mappso.align.MapPSOConfigurationException;

/**
 * This class encapsulates the termination condition of the algorithm.
 * 
 * @author Carsten Daenschel
 */
public class TerminatingCondition {

    private static final Log logger = LogFactory.getLog(TerminatingCondition.class);

    private Method method;

    /**
	 * Creates a new instance.
	 * @throws MapPSOConfigurationException if there is a problem with the configuration parameters.
	 */
    public TerminatingCondition() throws MapPSOConfigurationException {
        logger.info("Initialising termination condition ...");
        processConfiguration();
        logger.info("Termination condition initialised.");
    }

    /**
	 * sets the method according to the parameter "termination"
	 * @throws MapPSOConfigurationException if there is a problem with the configuration parameters.
	 */
    private void processConfiguration() throws MapPSOConfigurationException {
        Config.TerminationCondition condition = Config.getMainConfig().getTerminationCondition();
        switch(condition) {
            case maxIterations:
                method = new MaxIterations();
                break;
            case noChangeInConfidence:
                method = new NoChangeInConfidence();
                break;
            default:
                final String errMsg = "Internal error: unexpected termination condition found in configuration.";
                logger.fatal(errMsg);
                throw new AssertionError(errMsg);
        }
    }

    public boolean terminate(int currentIter, double currentConfidence) {
        return method.terminate(currentIter, currentConfidence);
    }

    /**
	 * Abstract representation for several termination conditions.
	 * 
	 * @author Carsten Daenschel
	 */
    private abstract static class Method {

        /**
	     * Checks whether the termination condition is fulfilled.
	     * @param currentIter Current iteration.
	     * @param currentConfidence Current confidence.
	     * @return <code>true</code> if termination condition fulfilled, <code>false</code> otherwise.
	     */
        public abstract boolean terminate(int currentIter, double currentConfidence);
    }

    /**
	 * Representation of a termination condition,
	 * which is fulfilled when the maximum number of iterations is reached.
	 * 
	 * @author Carsten Daenschel
	 */
    private static class MaxIterations extends Method {

        private final int maxIterations;

        /**
		 * Creates a new instance of this termination condition.
		 * @throws MapPSOConfigurationException if there is a problem with the configuration parameters.
		 */
        public MaxIterations() throws MapPSOConfigurationException {
            maxIterations = Config.getMainConfig().getIterations();
            if (logger.isInfoEnabled()) logger.info("Termination condition: Stop after " + maxIterations + " iterations.");
        }

        @Override
        public boolean terminate(int currentIter, double currentConfidence) {
            boolean conditionFulfilled = currentIter > maxIterations;
            if (logger.isDebugEnabled()) logger.debug("Termination condition " + (conditionFulfilled ? "" : "not ") + "fulfilled.");
            return conditionFulfilled;
        }
    }

    /**
	 * Representation of a termination condition,
	 * which is fulfilled when the confidence has not changed
	 * for a number of iterations. This number of iterations
	 * is specified by the sub parameter of the {@link Config#TERMINATION_CONDITION}
	 * configuration parameter. (an integer separated by a whitespace).
	 *  
	 * @author Carsten Daenschel
	 *
	 */
    private class NoChangeInConfidence extends Method {

        private final int maxCounter;

        private int counter = 0;

        private double knownBestConfidence = 0.d;

        /**
		 * Creates a new instance of this termination condition.
		 * @throws MapPSOConfigurationException if there is a problem with the configuration parameters. 
		 */
        public NoChangeInConfidence() throws MapPSOConfigurationException {
            try {
                String subParam = Config.getMainConfig().getTerminationCondition().getSubParam();
                if (subParam.isEmpty()) {
                    final String errMsg = "\"" + Config.getMainConfig().getTerminationCondition().name() + "\" termination condition requires a parseable integer as sub parameter (whitespace-separated)";
                    logger.error(errMsg);
                    throw new MapPSOConfigurationException(errMsg);
                }
                maxCounter = Integer.parseInt(subParam);
            } catch (NumberFormatException e) {
                final String errMsg = "termination condition subparameter is not a parseable integer.";
                logger.error(errMsg);
                throw new MapPSOConfigurationException(errMsg);
            }
            if (logger.isInfoEnabled()) logger.info("Termination condition: Stop after " + maxCounter + " iterations of stagnation.");
        }

        @Override
        public boolean terminate(int currentIter, double currentConfidence) {
            boolean conditionFulfilled = false;
            if (currentConfidence > knownBestConfidence) {
                knownBestConfidence = currentConfidence;
                counter = 0;
                conditionFulfilled = false;
            }
            if (counter > maxCounter) conditionFulfilled = true; else {
                counter++;
                conditionFulfilled = false;
            }
            if (logger.isDebugEnabled()) logger.debug("Termination condition " + (conditionFulfilled ? "" : "not") + " fulfilled.");
            return conditionFulfilled;
        }
    }
}
