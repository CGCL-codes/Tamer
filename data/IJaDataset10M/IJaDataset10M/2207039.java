package org.gamegineer.common.persistence.serializable;

import net.jcip.annotations.ThreadSafe;
import org.eclipse.osgi.util.NLS;

/**
 * A utility class to manage localized messages for the package.
 */
@ThreadSafe
final class Messages extends NLS {

    /**
     * The platform Serializable persistence delegate registry is not available.
     */
    public static String ObjectStreams_platformPersistenceDelegateRegistry_notAvailable;

    /**
     * Initializes the {@code Messages} class.
     */
    static {
        NLS.initializeMessages(Messages.class.getName(), Messages.class);
    }

    /**
     * Initializes a new instance of the {@code Messages} class.
     */
    private Messages() {
        super();
    }
}
