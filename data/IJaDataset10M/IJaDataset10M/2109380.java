package org.mariella.rcp.problems;

import java.util.logging.Logger;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.mariella.rcp.resources.VResourcesPlugin;
import org.osgi.framework.BundleContext;

public class ProblemsPlugin extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "org.mariella.rcp.problems";

    public static final Logger logger = Logger.getLogger(PLUGIN_ID);

    private static ProblemsPlugin plugin;

    private static ProblemManager problemManager;

    public ProblemsPlugin() {
    }

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        problemManager = new ProblemManager();
        VResourcesPlugin.getResourcePool().addResourceChangeListener(problemManager);
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        super.stop(context);
        plugin = null;
        VResourcesPlugin.getResourcePool().removeResourceChangeListener(problemManager);
        problemManager.close();
        problemManager = null;
    }

    public static ProblemsPlugin getDefault() {
        return plugin;
    }

    public static void initializeForWindow(IWorkbenchWindow window) {
        VResourcesPlugin.getResourceSelectionManager(window).addSelectionListener(problemManager);
    }

    public static ProblemManager getProblemManager() {
        return problemManager;
    }
}
