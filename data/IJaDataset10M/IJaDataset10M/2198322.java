package org.acoveo.callcenter.sipclient;

import org.acoveo.callcenter.nls.Messages;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "org.acoveo.callcenter.sipclient";

    private static Activator plugin;

    private static Logger logger;

    /**
	 * The constructor
	 */
    public Activator() {
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        logger = Logger.getLogger("org.acoveo.callcenter.sipclient");
        PjsuaClient.initialisePjsua();
    }

    public void stop(BundleContext context) throws Exception {
        PjsuaClient.shutdownPjsua();
        plugin = null;
        super.stop(context);
    }

    public void showConnectionErrorMessage(Exception e) {
        getDefault().getWorkbench().getDisplay().syncExec(new Runnable() {

            @Override
            public void run() {
                MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), Messages.Activator_4, Messages.Activator_5);
            }
        });
    }

    /**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
    public static Activator getDefault() {
        return plugin;
    }

    /** Returns the shared logger
	 * @return The logger
	 */
    public static Logger getLogger() {
        return logger;
    }
}
