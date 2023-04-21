package org.gamegineer.common.internal.core.util.logging;

import java.util.logging.Handler;
import java.util.logging.LogRecord;
import org.gamegineer.common.core.services.component.IComponentFactory;
import org.gamegineer.common.internal.core.services.logging.AbstractLoggingComponentFactory;

/**
 * Mock implementation of {@link java.util.logging.Handler}.
 */
public final class MockHandler extends Handler {

    /** The component factory for this class. */
    public static final IComponentFactory FACTORY = new AbstractLoggingComponentFactory<MockHandler>(MockHandler.class) {
    };

    /**
     * Initializes a new instance of the {@code MockHandler} class.
     */
    public MockHandler() {
        super();
    }

    @Override
    public void close() throws SecurityException {
    }

    @Override
    public void flush() {
    }

    @Override
    public void publish(@SuppressWarnings("unused") final LogRecord record) {
    }
}
