package org.gamegineer.client.internal.ui.console.commandlets;

import org.eclipse.osgi.util.NLS;

/**
 * A utility class to manage localized messages for the built-in commandlets.
 */
public final class CommandletMessages extends NLS {

    /** The name of the associated resource bundle. */
    private static final String BUNDLE_NAME = "org.gamegineer.client.internal.ui.console.commandlets.CommandletMessages";

    /** Too few arguments were specified. */
    public static String Commandlet_execute_tooFewArgs;

    /** Too many arguments were specified. */
    public static String Commandlet_execute_tooManyArgs;

    /** Too few arguments were specified. */
    public static String Commandlet_output_tooFewArgs;

    /** Too many arguments were specified. */
    public static String Commandlet_output_tooManyArgs;

    /**
     * Initializes the {@code CommandletMessages} class.
     */
    static {
        NLS.initializeMessages(BUNDLE_NAME, CommandletMessages.class);
    }

    /**
     * Initializes a new instance of the {@code CommandletMessages} class.
     */
    private CommandletMessages() {
        super();
    }
}
