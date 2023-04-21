package org.gamegineer.game.internal.ui;

import org.eclipse.osgi.util.NLS;

/**
 * A utility class to manage localized messages for the package.
 */
final class Messages extends NLS {

    /** The name of the associated resource bundle. */
    private static final String BUNDLE_NAME = "org.gamegineer.game.internal.ui.Messages";

    /** The game system user interface registry service tracker is not set. */
    public static String Services_gameSystemUiRegistryServiceTracker_notSet;

    /** The game system user interface service tracker is not set. */
    public static String Services_gameSystemUiServiceTracker_notSet;

    /** The package administration service tracker is not set. */
    public static String Services_packageAdminServiceTracker_notSet;

    /**
     * Initializes the {@code Messages} class.
     */
    static {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    /**
     * Initializes a new instance of the {@code Messages} class.
     */
    private Messages() {
        super();
    }
}
