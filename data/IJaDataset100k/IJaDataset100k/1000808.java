package net.sf.nebulacards.main;

/**
 * Represent one instance of a card game.  Most card games involve players
 * holding cards in their hand, so that is represented in this interface.
 * @author James Ranson
 * @version 0.7
 */
public interface CardGame extends Game {

    /**
	 * Get the public player data.  This is the player data that gets
	 * broadcast to everyone, and not confidential data like the cards
	 * in the player's hand.
	 * @return The per-player data for all seats (occupied or not).
	 */
    Player[] getPlayers();

    /**
	 * Get the cards in each player's hand.
	 * @return Each player's hand.
	 */
    PileOfCards[] getHands();

    /**
	 * Get the seating capacity of this game.
	 * @return The number of players that may be at the table.
	 */
    public int getCapacity();
}
