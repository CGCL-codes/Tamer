package de.hdtconsulting.jrando.randomizer;

import org.apache.log4j.Logger;
import de.hdtconsulting.jrando.randomizer.api.IRandomizer;

/**
 *
 * @author hdt
 *
 */
public class LongRollingSequence implements IRandomizer<Long> {

    /**
	 * Logger for this class
	 */
    private static final Logger logger = Logger.getLogger(LongRollingSequence.class);

    /**
	 * Bean name in Spring configuration
	 */
    public static final String SPRING_BEAN_NAME = "jrandoLongRollingSequence";

    /**
	 * actual value for sequence (default: 0)
	 */
    private Long sequence = 0L;

    /**
	 * start value for sequence (default: 0)
	 */
    private Long min = 0L;

    /**
	 * max value for sequene (default: Long.MAXVALUE)
	 */
    private Long max = Long.MAX_VALUE;

    public Long getRandom() {
        Long returnLong = this.sequence++;
        if (this.sequence >= max) {
            returnLong = this.sequence++;
            this.sequence = this.min;
        }
        return returnLong;
    }

    public void setMax(Long max) {
        this.max = max;
    }

    public void setMin(Long min) {
        this.min = min;
        this.sequence = min;
    }

    public void setSeed(long seed) {
        if (logger.isEnabledFor(org.apache.log4j.Level.WARN)) {
            logger.warn("setSeed(long) - setSeed not supported!", null);
        }
    }
}
