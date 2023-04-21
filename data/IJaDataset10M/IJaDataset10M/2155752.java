package org.eclipse.help.ui.internal;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.help.internal.HelpPlugin;
import org.eclipse.help.internal.base.BaseHelpSystem;
import org.eclipse.help.internal.base.HelpBasePlugin;
import org.eclipse.help.internal.base.HelpEvaluationContext;
import org.eclipse.help.internal.dynamic.FilterResolver;
import org.eclipse.help.internal.search.federated.IndexerJob;
import org.eclipse.help.ui.internal.dynamic.FilterResolverExtension;
import org.eclipse.help.ui.internal.util.ErrorUtil;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * This class is Help UI plugin.
 */
public class HelpUIPlugin extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "org.eclipse.help.ui";

    public static boolean DEBUG = false;

    public static boolean DEBUG_INFOPOP = false;

    private static HelpUIPlugin plugin;

    /**
	 * Logs an Error message with an exception. Note that the message should already be localized to
	 * proper locale. ie: Resources.getString() should already have been called
	 */
    public static synchronized void logError(String message, Throwable ex) {
        logError(message, ex, true, false);
    }

    public static synchronized void logError(String message, Throwable ex, boolean log, boolean openDialog) {
        if (message == null) message = "";
        Status errorStatus = new Status(IStatus.ERROR, PLUGIN_ID, IStatus.OK, message, ex);
        HelpPlugin.getDefault().getLog().log(errorStatus);
        if (openDialog) ErrorDialog.openError(null, null, null, errorStatus);
    }

    /**
	 * Provides access to singleton
	 * 
	 * @return HelpUIPlugin
	 */
    public static HelpUIPlugin getDefault() {
        return plugin;
    }

    public void stop(BundleContext context) throws Exception {
        plugin = null;
        Job.getJobManager().cancel(IndexerJob.FAMILY);
        super.stop(context);
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        FilterResolver.setExtension(new FilterResolverExtension());
        HelpEvaluationContext.setContext(HelpUIEvaluationContext.getContext());
        DEBUG = isDebugging();
        if (DEBUG) {
            DEBUG_INFOPOP = "true".equalsIgnoreCase(Platform.getDebugOption(PLUGIN_ID + "/debug/infopop"));
        }
        if (BaseHelpSystem.getMode() == BaseHelpSystem.MODE_WORKBENCH) BaseHelpSystem.setDefaultErrorUtil(new ErrorUtil());
        if (BaseHelpSystem.getMode() == BaseHelpSystem.MODE_WORKBENCH) {
            IWorkbench workbench = PlatformUI.getWorkbench();
            if (workbench != null) {
                HelpBasePlugin.setActivitySupport(new HelpActivitySupport(workbench));
            }
        }
    }
}
