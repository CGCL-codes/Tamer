package org.gamegineer.table.internal.net.node.common.messages;

import org.junit.Before;
import org.junit.Test;

/**
 * A fixture for testing the
 * {@link org.gamegineer.table.internal.net.node.common.messages.CardIncrementMessage}
 * class.
 */
public final class CardIncrementMessageTest {

    /** The card increment message under test in the fixture. */
    private CardIncrementMessage message_;

    /**
     * Initializes a new instance of the {@code CardIncrementMessageTest} class.
     */
    public CardIncrementMessageTest() {
    }

    /**
     * Sets up the test fixture.
     * 
     * @throws java.lang.Exception
     *         If an error occurs.
     */
    @Before
    public void setUp() throws Exception {
        message_ = new CardIncrementMessage();
    }

    /**
     * Ensures the {@code setCardPileIndex} method throws an exception when
     * passed an illegal card pile index that is negative.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetCardPileIndex_CardPileIndex_Null() {
        message_.setCardPileIndex(-1);
    }

    /**
     * Ensures the {@code setIncrement} method throws an exception when passed a
     * {@code null} increment.
     */
    @Test(expected = NullPointerException.class)
    public void testSetIncrement_Increment_Null() {
        message_.setIncrement(null);
    }

    /**
     * Ensures the {@code setIndex} method throws an exception when passed an
     * illegal index that is negative.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetIndex_Index_Null() {
        message_.setIndex(-1);
    }
}
