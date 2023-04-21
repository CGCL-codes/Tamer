package de.hackerdan.projectcreator.internal.messages;

import org.eclipse.osgi.util.NLS;

/**
 * Lokalisierung.
 *
 * @author Daniel Hirscher
 */
public final class Messages extends NLS {

    /** Monitor. */
    public static String projectCreator_monitor_createProject;

    /** Monitor. */
    public static String projectCreator_monitor_CreateProjects;

    private static final String BUNDLE_NAME = "de.hackerdan.projectcreator.internal.messages.messages";

    static {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
        super();
    }
}
