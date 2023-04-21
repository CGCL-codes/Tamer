package com.andrewswan.lostcities.domain.move;

import com.andrewswan.lostcities.domain.Card;
import com.andrewswan.lostcities.domain.DiscardPile;
import com.andrewswan.lostcities.domain.Game;
import com.andrewswan.lostcities.domain.Suit;
import com.andrewswan.lostcities.domain.player.Player;

/**
 * A {@link CardPlay} in which the {@link Player} puts the {@link Card} into the
 * {@link DiscardPile} for its {@link Suit}.
 *
 * @author Andrew
 */
public class Discard extends AbstractCardPlay {

    /**
	 * Constructor for discarding the given card
	 *
	 * @param card the card to discard; can't be <code>null</code>
	 */
    public Discard(final Card card) {
        super(card);
    }

    public void execute(final Game game, final Player player) {
        game.discard(player, card);
    }

    public Suit getDiscardedSuit() {
        return this.card.getSuit();
    }
}
