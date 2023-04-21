package org.gamegineer.common.internal.remoting;

import java.util.logging.Logger;
import org.gamegineer.common.core.services.logging.ILoggingService;
import org.osgi.framework.Bundle;

/**
 * Manages the loggers used by the bundle.
 */
public final class Loggers extends org.gamegineer.common.core.runtime.Loggers {

    /** The default logger for the bundle. */
    public static final Logger DEFAULT;

    /**
     * Initializes the {@code Loggers} class.
     */
    static {
        final Bundle bundle = Activator.getDefault().getBundleContext().getBundle();
        final ILoggingService loggingService = getLoggingService();
        DEFAULT = loggingService.getLogger(bundle);
    }

    /**
     * Initializes a new instance of the {@code Loggers} class.
     */
    private Loggers() {
        super();
    }
}
