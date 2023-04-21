package de.hdtconsulting.jrando.randomizer;

import java.util.Random;
import org.apache.log4j.Logger;
import de.hdtconsulting.jrando.randomizer.api.IRandomizer;

/**
 * 
 * This randomizer generates random Double values
 * 
 * @author hdt
 *
 */
public class DoubleRandomizer implements IRandomizer<Double> {

    /**
	 * Logger for this class
	 */
    private static final Logger logger = Logger.getLogger(DoubleRandomizer.class);

    /**
	 * Bean name in Spring configuration
	 */
    public static final String SPRING_BEAN_NAME = "jrandoDoubleRandomizer";

    /**
	 * Internal random implementation
	 */
    private Random r = new Random();

    public Double getRandom() {
        if (logger.isDebugEnabled()) {
            logger.debug("getRandom() - start");
        }
        Double returnDouble = r.nextDouble();
        if (logger.isDebugEnabled()) {
            logger.debug("getRandom() - end - return value=" + returnDouble);
        }
        return returnDouble;
    }

    public void setMax(Double max) {
        if (logger.isEnabledFor(org.apache.log4j.Level.WARN)) {
            logger.warn("setMax(Double) - setMax not supported!", null);
        }
    }

    public void setMin(Double min) {
        if (logger.isEnabledFor(org.apache.log4j.Level.WARN)) {
            logger.warn("setMin(Double) - setMin not supported!", null);
        }
    }

    public void setSeed(long seed) {
        this.r.setSeed(seed);
    }
}
