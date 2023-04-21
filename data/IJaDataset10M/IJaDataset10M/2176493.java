package org.gamegineer.table.internal.net.node.common.messages;

import org.junit.Before;
import org.junit.Test;

/**
 * A fixture for testing the
 * {@link org.gamegineer.table.internal.net.node.common.messages.GiveControlMessage}
 * class.
 */
public final class GiveControlMessageTest {

    /** The give control message under test in the fixture. */
    private GiveControlMessage message_;

    /**
     * Initializes a new instance of the {@code GiveControlMessageTest} class.
     */
    public GiveControlMessageTest() {
    }

    /**
     * Sets up the test fixture.
     * 
     * @throws java.lang.Exception
     *         If an error occurs.
     */
    @Before
    public void setUp() throws Exception {
        message_ = new GiveControlMessage();
    }

    /**
     * Ensures the {@code setPlayerName} method throws an exception when passed
     * a {@code null} player name.
     */
    @Test(expected = NullPointerException.class)
    public void testSetPlayerName_PlayerName_Null() {
        message_.setPlayerName(null);
    }
}
