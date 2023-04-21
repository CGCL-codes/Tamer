package com.tensegrity.palorcp;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import com.tensegrity.palorcp.preferences.PreferencesMessages;

/**
 * <p>
 * The central singleton for the com.tensegrity.palorcp plugin
 * </p>
 * 
 * @author Stepan Rutz
 * @version $Id: PalorcpPlugin.java,v 1.19 2007/05/10 06:59:55 PhilippBouillon Exp $
 */
public class PalorcpPlugin extends AbstractUIPlugin {

    private static PalorcpPlugin plugin;

    private static boolean runsAsRcp;

    private BundleContext context;

    /**
     * The constructor.
     */
    public PalorcpPlugin() {
        plugin = this;
    }

    public static final boolean runsAsRcp() {
        return runsAsRcp;
    }

    public static final void setRunsAsRcp(boolean b) {
        runsAsRcp = b;
    }

    /**
	 * This method is called upon plug-in activation
	 */
    public void start(BundleContext context) throws Exception {
        super.start(context);
        this.context = context;
        PreferencesMessages.getString("ApplicationPreferencePage.Description");
    }

    /**
	 * This method is called when the plug-in is stopped
	 */
    public void stop(BundleContext context) throws Exception {
        super.stop(context);
        this.context = null;
        plugin = null;
    }

    /**
	 * Returns the bundle-context.
	 * 
	 * @return the bundle-context.
	 */
    public BundleContext getBundleContext() {
        return context;
    }

    /**
	 * Returns the shared instance.
	 */
    public static PalorcpPlugin getDefault() {
        return plugin;
    }

    /**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path.
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
    public static ImageDescriptor getImageDescriptor(String path) {
        return AbstractUIPlugin.imageDescriptorFromPlugin("com.tensegrity.palorcp", path);
    }

    /**
	 * Returns the active window shell or null if no workbench window has been
	 * created yet.
	 * 
	 * @return
	 */
    public final Shell getActiveShell() {
        Shell activeShell = null;
        try {
            activeShell = getWorkbench().getActiveWorkbenchWindow().getShell();
        } catch (Exception ex) {
        }
        return activeShell;
    }
}
