package org.lcx.scrumvision.kanban;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author Laurent Carbonnaux
 */
public class ScrumVisionKanbanPlugin extends AbstractUIPlugin {

    public static final boolean isLogEnable = false;

    public static final String PLUGIN_ID = "org.lcx.scrumvision.kanban";

    public static final String REPOSITORY_KIND = "scrumvision";

    public static final String CLIENT_LABEL = "Scrum Vision (using Google SpreadSheet)";

    public static final String TITLE_MESSAGE_DIALOG = "Mylyn Scrum Vision Kanban";

    private static ScrumVisionKanbanPlugin plugin;

    /**
	 * The constructor
	 */
    public ScrumVisionKanbanPlugin() {
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
    public static ScrumVisionKanbanPlugin getDefault() {
        return plugin;
    }

    /**
	 * Convenience method for logging statuses to the plugin log
	 * 
	 * @param status
	 *            the status to log
	 */
    public static void log(IStatus status) {
        if (isLogEnable) getDefault().getLog().log(status);
    }

    /**
	 * Convenience method for logging exceptions to the plugin log
	 * 
	 * @param e
	 *            the exception to log
	 */
    public static void log(Exception e) {
        String message = e.getMessage();
        if (e.getMessage() == null) {
            message = e.getClass().toString();
        }
        log(new Status(Status.ERROR, ScrumVisionKanbanPlugin.PLUGIN_ID, 0, message, e));
    }
}
