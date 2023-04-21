package org.gamegineer.table.internal.net.node.common;

import net.jcip.annotations.ThreadSafe;
import org.eclipse.osgi.util.NLS;

/**
 * A utility class to manage localized messages for the package.
 */
@ThreadSafe
final class Messages extends NLS {

    /** Failed to create an authentication response. */
    public static String Authenticator_createResponse_failed;

    /** Failed to create a secret key. */
    public static String Authenticator_createSecretKey_failed;

    /** Failed to create a secure random byte buffer. */
    public static String Authenticator_createSecureRandomBytes_failed;

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
