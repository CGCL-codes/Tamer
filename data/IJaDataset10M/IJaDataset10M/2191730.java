package org.vizzini.game.cardgame;

import java.util.Properties;

/**
 * Provides common data objects to use in unit tests.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.1
 */
public class TestData extends org.vizzini.game.TestData {

    /** First suit name. */
    public static final String SUIT_NAME0 = "diamonds";

    /** Second suit name. */
    public static final String SUIT_NAME1 = "hearts";

    /** First suit. */
    public static final ISuit SUIT0 = new DefaultSuit(SUIT_NAME0);

    /** Second suit. */
    public static final ISuit SUIT1 = new DefaultSuit(SUIT_NAME1);

    /** Third suit. */
    public static final ISuit SUIT2 = new DefaultSuit(SUIT_NAME0);

    /** First card name. */
    public static final String CARD_NAME0 = "ace";

    /** Second card name. */
    public static final String CARD_NAME1 = "2";

    /** Third card name. */
    public static final String CARD_NAME2 = "10";

    /** First card. */
    public static final ICard CARD0 = new PokerCard(CARD_NAME0, 1, SUIT0);

    /** Second card. */
    public static final ICard CARD1 = new PokerCard(CARD_NAME1, 2, SUIT1);

    /** Third card. */
    public static final ICard CARD2 = new PokerCard(CARD_NAME2, 10, SUIT2);

    /**
     * Construct this object with the given parameters.
     *
     * @param  properties  Properties.
     *
     * @since  v0.4
     */
    public TestData(Properties properties) {
        super(properties);
    }
}
