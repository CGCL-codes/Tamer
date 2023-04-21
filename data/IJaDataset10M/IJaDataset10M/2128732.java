package org.gamegineer.client.internal.ui.console;

import org.eclipse.osgi.util.NLS;
import org.osgi.framework.Version;

/**
 * A utility class to manage localized messages for the package.
 */
final class Messages extends NLS {

    /** The name of the associated resource bundle. */
    private static final String BUNDLE_NAME = "org.gamegineer.client.internal.ui.console.Messages";

    /** The description for the "help" option. */
    public static String CommandLineOptions_help_description;

    /** The console banner message. */
    public static String Console_output_bannerMessage;

    /** An unexpected error occurred during commandlet execution. */
    public static String Console_output_unexpectedCommandletException;

    /** An error occurred during commandlet execution. */
    public static String Console_run_commandletException;

    /** An unexpected error occurred during commandlet execution. */
    public static String Console_run_unexpectedCommandletException;

    /** The console is not pristine. */
    public static String Console_state_notPristine;

    /** The attribute does not exist. */
    public static String Statelet_attribute_absent;

    /** The attribute already exists. */
    public static String Statelet_attribute_present;

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

    static String Console_output_bannerMessage(final Version version) {
        return bind(Console_output_bannerMessage, version.toString());
    }

    static String Statelet_attribute_absent(final String attributeName) {
        return bind(Statelet_attribute_absent, attributeName);
    }

    static String Statelet_attribute_present(final String attributeName) {
        return bind(Statelet_attribute_present, attributeName);
    }
}
