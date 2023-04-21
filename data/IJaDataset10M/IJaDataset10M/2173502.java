package net.entropysoft.jmx.plugin.jmxdashboard.gef.command;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "net.entropysoft.jmx.plugin.jmxdashboard.gef.command.messages";

    public static String DeleteDashboardElementCommand_ComponentDeletion;

    public static String SetConstraintDashboardElementCommand_MoveResize;

    public static String SetImagePathCommand_SetImagePath;

    static {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
