package net.sf.nebulacards.game;

import net.sf.nebulacards.main.*;

/**
 * Implement the more mundane features of CardGame.
 * @author James Ranson
 * @version 0.8
 */
public abstract class CardGameAdapter extends GameAdapter implements CardGame {

    /**
	 * The (public) data for each individual player.
	 * @see #getPlayers()
	 */
    Player[] m_players;

    /**
	 * Get the public player data.  This is the player data that gets
	 * broadcast to everyone, and not confidential data like the cards
	 * in the player's hand.
	 * @return The per-player data for all seats (occupied or not).
	 */
    public Player[] getPlayers() {
        return m_players;
    }

    /**
	 * The cards in each player's hand.
	 */
    PileOfCards[] m_hands;

    /**
	 * Get the cards in each player's hand.
	 * @return Each player's hand.
	 */
    public PileOfCards[] getHands() {
        return m_hands;
    }

    public AddResult add(NebulaUi target, int priority) {
        AddResult result = super.add(target, priority);
        if (result != null && result.getPosition() >= 0) getPlayers()[result.getPosition()].setVacant(false);
        return result;
    }

    public void remove(int i) {
        getPlayers()[i].setVacant(true);
        super.remove(i);
    }

    /**
	 * Constructor.
	 * @param n The number of elements in the various arrays.
	 */
    public CardGameAdapter(int n) {
        super(n);
        m_players = new Player[n];
        m_hands = new PileOfCards[n];
        for (int i = 0; i < n; i++) {
            m_players[i] = new Player("");
            m_players[i].setVacant(true);
            m_hands[i] = new PileOfCards();
        }
    }

    /**
	 * Constructor.  Equivalent to CardGameAdapter(4).
	 */
    public CardGameAdapter() {
        this(4);
    }
}
