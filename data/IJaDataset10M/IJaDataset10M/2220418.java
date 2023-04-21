package net.sf.l2j.gameserver.util;

/**
 * Flood protector configuration
 *
 * @author fordfrog
 */
public final class FloodProtectorConfig {

    /**
	 * Type used for identification of logging output.
	 */
    public String FLOOD_PROTECTOR_TYPE;

    /**
	 * Flood protection interval in game ticks.
	 */
    public int FLOOD_PROTECTION_INTERVAL;

    /**
	 * Whether flooding should be logged.
	 */
    public boolean LOG_FLOODING;

    /**
	 * If specified punishment limit is exceeded, punishment is applied.
	 */
    public int PUNISHMENT_LIMIT;

    /**
	 * Punishment type. Either 'none', 'ban' or 'jail'.
	 */
    public String PUNISHMENT_TYPE;

    /**
	 * For how long should the char/account be punished.
	 */
    public int PUNISHMENT_TIME;

    /**
	 * Creates new instance of FloodProtectorConfig.
	 *
	 * @param floodProtectorType
	 *            {@link #FLOOD_PROTECTOR_TYPE}
	 */
    public FloodProtectorConfig(final String floodProtectorType) {
        super();
        FLOOD_PROTECTOR_TYPE = floodProtectorType;
    }
}
