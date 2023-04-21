package org.vizzini.game.cardgame;

import org.vizzini.game.IToken;

/**
 * Defines methods required by a card for card games in the game framework.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.1
 */
public interface ICard extends IToken, Comparable<ICard> {

    /** Name property name. */
    static final String SUIT_PROPERTY = "suit";

    /**
     * @return  the index representing the card's name.
     *
     * @since   v0.1
     */
    int getIndex();

    /**
     * @return  the suit.
     *
     * @since   v0.1
     */
    ISuit getSuit();
}
