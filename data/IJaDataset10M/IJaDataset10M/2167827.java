package org.gamegineer.common.internal.core.util.logging;

import org.junit.Test;

/**
 * A fixture for testing the
 * {@link org.gamegineer.common.internal.core.util.logging.AbstractHandlerFactory}
 * class.
 */
public final class AbstractHandlerFactoryTest {

    /**
     * Initializes a new instance of the {@code AbstractHandlerFactoryTest}
     * class.
     */
    public AbstractHandlerFactoryTest() {
        super();
    }

    /**
     * Ensures the constructor throws an exception when passed a {@code null}
     * component type.
     */
    @Test(expected = NullPointerException.class)
    public void testConstructor_Type_Null() {
        new AbstractHandlerFactory<MockHandler>(null) {
        };
    }
}
