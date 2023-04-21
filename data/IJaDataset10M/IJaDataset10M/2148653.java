package org.gamegineer.common.core.util.logging;

import org.junit.Test;

/**
 * A fixture for testing the
 * {@link org.gamegineer.common.core.util.logging.FrameworkLogHandler} class.
 */
public final class FrameworkLogHandlerTest {

    /**
     * Initializes a new instance of the {@code FrameworkLogHandlerTest} class.
     */
    public FrameworkLogHandlerTest() {
        super();
    }

    /**
     * Ensures the constructor throws an exception when passed a {@code null}
     * framework log.
     */
    @Test(expected = NullPointerException.class)
    public void testConstructor_FrameworkLog_Null() {
        new FrameworkLogHandler(null);
    }
}
