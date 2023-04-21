package org.gamegineer.table.internal.ui.util;

import net.jcip.annotations.ThreadSafe;
import org.eclipse.osgi.util.NLS;

/**
 * A utility class to manage localized messages for the package.
 */
@ThreadSafe
public final class CommonMessages extends NLS {

    /** The application name. */
    public static String Common_application_name;

    /**
     * Initializes the {@code CommonMessages} class.
     */
    static {
        NLS.initializeMessages(CommonMessages.class.getName(), CommonMessages.class);
    }

    /**
     * Initializes a new instance of the {@code CommonMessages} class.
     */
    private CommonMessages() {
        super();
    }
}
