package org.gamegineer.game.internal.core.commands;

import org.junit.Test;

/**
 * A fixture for testing the
 * {@link org.gamegineer.game.internal.core.commands.InitializeEngineCommand}
 * class.
 */
public final class InitializeEngineCommandTest {

    /**
     * Initializes a new instance of the {@code InitializeEngineCommandTest}
     * class.
     */
    public InitializeEngineCommandTest() {
        super();
    }

    /**
     * Ensures the constructor throws an exception when passed a {@code null}
     * game configuration.
     */
    @Test(expected = NullPointerException.class)
    public void testConstructor_GameConfig_Null() {
        new InitializeEngineCommand(null);
    }
}
