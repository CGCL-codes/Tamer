package org.gamegineer.table.internal.core;

import org.junit.Test;

/**
 * A fixture for testing the
 * {@link org.gamegineer.table.internal.core.CardSurfaceDesignProxy} class.
 */
public final class CardSurfaceDesignProxyTest {

    /**
     * Initializes a new instance of the {@code CardSurfaceDesignProxyTest}
     * class.
     */
    public CardSurfaceDesignProxyTest() {
        super();
    }

    /**
     * Ensures the constructor throws an exception when passed a {@code null}
     * card surface design.
     */
    @Test(expected = NullPointerException.class)
    public void testConstructor_CardSurfaceDesign_Null() {
        new CardSurfaceDesignProxy(null);
    }
}
