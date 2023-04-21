package org.gamegineer.client.internal.ui.console.commandlets.core;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Set;
import org.eclipse.osgi.util.NLS;

/**
 * A utility class to manage localized messages for the package.
 */
final class Messages extends NLS {

    /** The name of the associated resource bundle. */
    private static final String BUNDLE_NAME = "org.gamegineer.client.internal.ui.console.commandlets.core.Messages";

    /** An error occurred when creating the commandlet component. */
    public static String HelpCommandlet_getCommandletHelp_componentError;

    /** The component is not a commandlet. */
    public static String HelpCommandlet_getCommandletHelp_notCommandlet;

    /** The help detailed description. */
    public static String HelpCommandlet_help_detailedDescription;

    /** The help synopsis. */
    public static String HelpCommandlet_help_synopsis;

    /** The commandlet name is ambiguous. */
    public static String HelpCommandlet_output_ambiguousCommandlet;

    /** A help detailed description is not available. */
    public static String HelpCommandlet_output_noHelpDetailedDescriptionAvailable;

    /** A help synopsis is not available. */
    public static String HelpCommandlet_output_noHelpSynopsisAvailable;

    /** The commandlet does not exist. */
    public static String HelpCommandlet_output_unknownCommandlet;

    /** The help detailed description. */
    public static String QuitCommandlet_help_detailedDescription;

    /** The help synopsis. */
    public static String QuitCommandlet_help_synopsis;

    /**
     * Initializes the {@code CommandletMessages} class.
     */
    static {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    /**
     * Initializes a new instance of the {@code CommandletMessages} class.
     */
    private Messages() {
        super();
    }

    static String HelpCommandlet_getCommandletHelp_componentError(final String name) {
        return bind(HelpCommandlet_getCommandletHelp_componentError, name);
    }

    static String HelpCommandlet_getCommandletHelp_notCommandlet(final String name) {
        return bind(HelpCommandlet_getCommandletHelp_notCommandlet, name);
    }

    static String HelpCommandlet_output_ambiguousCommandlet(final String name, final Set<String> classNames) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        pw.println(bind(HelpCommandlet_output_ambiguousCommandlet, name));
        for (final Iterator<String> iter = classNames.iterator(); iter.hasNext(); ) {
            pw.print(iter.next());
            if (iter.hasNext()) {
                pw.println();
            }
        }
        return sw.toString();
    }

    static String HelpCommandlet_output_unknownCommandlet(final String name) {
        return bind(HelpCommandlet_output_unknownCommandlet, name);
    }
}
