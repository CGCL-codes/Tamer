package net.sf.nebulacards.game;

/**
 * To be implemented by games that have a trump suit.
 *
 * @author James Ranson
 * @version 0.8
 */
public interface TrumpGame extends CardGame {

    /**
     * Get the trump suit.
     * @return The trump suit.
     */
    int getTrump();

    /**
     * Get the trump suit name.  This helps generic user interfaces to
	 * display the trump meaningfully.
     * @return The trump suit name.
     */
    String getTrumpName();

    /**
	 * Set the trump suit.
	 * @param t The new trump suit.
	 */
    void setTrump(int t);
}
