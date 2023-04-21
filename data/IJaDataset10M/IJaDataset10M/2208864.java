package org.swemof.miner;

/**
 * Base implementation for miners that can apply Latent Semantic Analysis before
 * performing the actual mining algorithm.
 * <p>
 * This implementation facilitates easy LSA option retrieval.
 */
public abstract class LSAMiner extends AbstractMiner {

    /**
     * Default value for the <code>PERFORM_LSA</code> option. The default is
     * <code>true</code>.
     */
    public static final Boolean DEFAULT_VALUE_PERFORM_LSA = true;

    /**
     * Constant for the <code>PERFORM_LSA</code> option.
     */
    public static final String OPTION_KEY_PERFORM_LSA = "PERFORM_LSA";

    /**
     * Default value for the <code>RANK</code> option. The default is
     * <code>2</code>.
     */
    public static final int DEFAULT_VALUE_RANK = 2;

    /**
     * Constant for the <code>RANK</code> option.
     */
    public static final String OPTION_KEY_RANK = "RANK";

    /**
     * Default value for the <code>PERFORM_STOPWORD_FILTERING</code> option. The
     * default is <code>false</code>.
     */
    public static final Boolean DEFAULT_VALUE_PERFORM_STOPWORD_FILTERING = false;

    /**
     * Constant for the <code>PERFORM_STOPWORD_FILTERING</code> option.
     */
    public static final String OPTION_KEY_PERFORM_STOPWORD_FILTERING = "PERFORM_STOPWORD_FILTERING";

    /**
     * Returns whether LSA should be performed.
     * 
     * @return <code>true</code> when LSA should be performed.
     */
    protected boolean isPerformLSA() {
        return optionSupport.getBoolean(OPTION_KEY_PERFORM_LSA, DEFAULT_VALUE_PERFORM_LSA);
    }

    /**
     * Returns the rank to use for LSA.
     * 
     * @return the rank.
     */
    protected int getRank() {
        return optionSupport.getInteger(OPTION_KEY_RANK, DEFAULT_VALUE_RANK);
    }

    /**
     * Returns whether stop word filtering should be performed.
     * 
     * @return <code>true</code> when stop word filtering should be performed.
     */
    protected boolean isPerformStopWordFiltering() {
        return optionSupport.getBoolean(OPTION_KEY_PERFORM_STOPWORD_FILTERING, DEFAULT_VALUE_PERFORM_STOPWORD_FILTERING);
    }
}
