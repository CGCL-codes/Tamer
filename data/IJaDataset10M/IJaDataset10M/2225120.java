package org.vizzini.example.tarot;

import org.vizzini.game.cardgame.CardCollection;
import org.vizzini.game.cardgame.DefaultSuit;
import org.vizzini.game.cardgame.ICard;
import org.vizzini.game.cardgame.ISuit;
import org.vizzini.game.cardgame.PokerCard;

/**
 * Provides a tarot card deck for card games in the game framework.
 */
public class TarotDeck extends CardCollection {

    /** Serial version UID. */
    private static final long serialVersionUID = 1L;

    /** Major arcana suit. */
    public static final ISuit MAJOR = new DefaultSuit("major");

    /** Clubs suit. */
    public static final ISuit WANDS = new DefaultSuit("wands");

    /** Hearts suit. */
    public static final ISuit CUPS = new DefaultSuit("cups");

    /** Spades suit. */
    public static final ISuit SWORDS = new DefaultSuit("swords");

    /** Diamonds suit. */
    public static final ISuit PENTACLES = new DefaultSuit("pentacles");

    /** Array of suits. */
    public static final ISuit[] SUITS = new ISuit[] { MAJOR, WANDS, CUPS, SWORDS, PENTACLES };

    /**
     * Construct this object.
     */
    protected TarotDeck() {
        super();
        init();
    }

    /**
     * Reset this deck.
     * 
     * @since v0.1
     */
    public void reset() {
        clear();
        init();
    }

    /**
     * Initialize the deck with the standard cards.
     */
    protected void init() {
        initCard("theFool", 0, MAJOR);
        initCard("theMagician", 1, MAJOR);
        initCard("theHighPriestess", 2, MAJOR);
        initCard("theEmpress", 3, MAJOR);
        initCard("theEmperor", 4, MAJOR);
        initCard("theHierophant", 5, MAJOR);
        initCard("theLovers", 6, MAJOR);
        initCard("theChariot", 7, MAJOR);
        initCard("strength", 8, MAJOR);
        initCard("theHermit", 9, MAJOR);
        initCard("wheelOfFortune", 10, MAJOR);
        initCard("justice", 11, MAJOR);
        initCard("theHangedMan", 12, MAJOR);
        initCard("death", 13, MAJOR);
        initCard("temperance", 14, MAJOR);
        initCard("theDevil", 15, MAJOR);
        initCard("theTower", 16, MAJOR);
        initCard("theStar", 17, MAJOR);
        initCard("theMoon", 18, MAJOR);
        initCard("theSun", 19, MAJOR);
        initCard("judgement", 20, MAJOR);
        initCard("theWorld", 21, MAJOR);
        initCards("ace", 1);
        initCards("2", 2);
        initCards("3", 3);
        initCards("4", 4);
        initCards("5", 5);
        initCards("6", 6);
        initCards("7", 7);
        initCards("8", 8);
        initCards("9", 9);
        initCards("10", 10);
        initCards("page", 11);
        initCards("knight", 12);
        initCards("queen", 13);
        initCards("king", 14);
    }

    /**
     * Create and add a card with the given parameters.
     * 
     * @param name Name.
     * @param value Value.
     * @param suit Suit.
     */
    protected void initCard(final String name, final int value, final ISuit suit) {
        final ICard card = new PokerCard(name, value, suit);
        add(card);
    }

    /**
     * Create cards for each suit with the given parameters.
     * 
     * @param name Name.
     * @param value Value.
     */
    protected void initCards(final String name, final int value) {
        initCard(name, value, WANDS);
        initCard(name, value, CUPS);
        initCard(name, value, SWORDS);
        initCard(name, value, PENTACLES);
    }
}
