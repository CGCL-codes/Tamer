package org.gamegineer.tictactoe.internal.core;

import org.junit.Test;

/**
 * A fixture for testing the
 * {@link org.gamegineer.tictactoe.internal.core.Services} class.
 */
public final class ServicesTest {

    /**
     * Initializes a new instance of the {@code ServicesTest} class.
     */
    public ServicesTest() {
        super();
    }

    /**
     * Ensures the {@code open} method throws an exception when passed a
     * {@code null} bundle context.
     */
    @Test(expected = NullPointerException.class)
    public void testOpen_Context_Null() {
        Services.getDefault().open(null);
    }
}
