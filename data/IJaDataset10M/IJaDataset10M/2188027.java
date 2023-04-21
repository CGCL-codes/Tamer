package org.gamegineer.table.internal.ui.util;

import net.jcip.annotations.ThreadSafe;
import org.eclipse.osgi.util.NLS;

/**
 * A utility class to manage localized messages for the application.
 */
@ThreadSafe
public final class CommonNlsMessages extends NLS {

    /** The application name. */
    public static String Common_application_name;

    /**
     * Initializes the {@code CommonNlsMessages} class.
     */
    static {
        NLS.initializeMessages(CommonNlsMessages.class.getName(), CommonNlsMessages.class);
    }

    /**
     * Initializes a new instance of the {@code CommonNlsMessages} class.
     */
    private CommonNlsMessages() {
        super();
    }
}
