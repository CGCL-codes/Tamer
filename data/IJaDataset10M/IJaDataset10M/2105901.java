package ti.plato.ui.views.explorer;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import ti.plato.ui.u.WorkspaceSaveParticipant;
import ti.plato.ui.views.explorer.templates.WorkspaceSaveContainer;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "ti.plato.ui.views.explorer";

    private static Activator plugin;

    private WorkspaceSaveContainer workspaceSaveContainer;

    /**
	 * The constructor
	 */
    public Activator() {
        plugin = this;
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
        workspaceSaveContainer = new WorkspaceSaveContainer();
        new WorkspaceSaveParticipant(workspaceSaveContainer, this, 0);
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
    public static Activator getDefault() {
        return plugin;
    }

    /**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }

    public WorkspaceSaveContainer getWorkspaceSaveContainer() {
        return workspaceSaveContainer;
    }
}
