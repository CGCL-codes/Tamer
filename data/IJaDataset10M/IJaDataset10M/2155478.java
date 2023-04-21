package org.gamegineer.common.internal.core.logging;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import net.jcip.annotations.ThreadSafe;

/**
 * Fake implementation of {@link java.util.logging.Formatter}.
 */
@ThreadSafe
public final class FakeFormatter extends Formatter {

    /**
     * Initializes a new instance of the {@code FakeFormatter} class.
     */
    public FakeFormatter() {
    }

    @Override
    public String format(@SuppressWarnings("unused") final LogRecord record) {
        return "";
    }
}
