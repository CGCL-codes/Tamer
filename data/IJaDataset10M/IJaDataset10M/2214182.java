package org.gamegineer.table.internal.net;

import java.util.logging.Logger;
import net.jcip.annotations.ThreadSafe;

/**
 * Manages the loggers used by the bundle.
 */
@ThreadSafe
public final class Loggers extends org.gamegineer.common.core.runtime.Loggers {

    /**
     * Initializes a new instance of the {@code Loggers} class.
     */
    private Loggers() {
    }

    public static Logger getDefaultLogger() {
        return getLogger(Activator.getDefault().getBundleContext().getBundle(), null);
    }
}
