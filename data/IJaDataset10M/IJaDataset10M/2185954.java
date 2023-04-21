package org.gamegineer.game.internal.core;

import org.junit.Test;

/**
 * A fixture for testing the
 * {@link org.gamegineer.game.internal.core.GameAttributes} class.
 */
public final class GameAttributesTest {

    /**
     * Initializes a new instance of the {@code GameAttributesTest} class.
     */
    public GameAttributesTest() {
        super();
    }

    /**
     * Ensures the {@code stageState} method throws an exception when passed a
     * {@code null} stage identifier.
     */
    @Test(expected = AssertionError.class)
    public void testStageState_StageId_Null() {
        GameAttributes.stageState(null);
    }
}
