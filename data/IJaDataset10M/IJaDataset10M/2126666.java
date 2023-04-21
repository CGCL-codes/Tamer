package org.eclipse.pde.visualization.dependency;

import java.net.URL;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.pde.internal.ui.PDEPluginImages;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.eclipse.core.runtime.Path;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "org.eclipse.pde.visualization.dependency";

    public static final String FORWARD_ENABLED = "icons/obj16/forward_enabled.gif";

    public static final String BACKWARD_ENABLED = "icons/obj16/backward_enabled.gif";

    public static final String SNAPSHOT = "icons/obj16/snapshot.gif";

    public static final String SAVEEDIT = "icons/obj16/save_edit.gif";

    public static final String REQ_PLUGIN_OBJ = "icons/obj16/req_plugins_obj.gif";

    public static final String SEARCH_CANCEL = "icons/obj16/progress_rem.gif";

    public static final String PLUGIN_OBJ = "plugin_obj";

    private static Activator plugin;

    /**
	 * The constructor
	 */
    public Activator() {
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        addImage(FORWARD_ENABLED);
        addImage(BACKWARD_ENABLED);
        addImage(SNAPSHOT);
        addImage(SAVEEDIT);
        addImage(REQ_PLUGIN_OBJ);
        addImage(SEARCH_CANCEL);
        this.getImageRegistry().put(PLUGIN_OBJ, PDEPluginImages.DESC_PLUGIN_OBJ);
    }

    private void addImage(String imagePath) {
        String path = "$nl$/" + imagePath;
        URL url = FileLocator.find(this.getBundle(), new Path(path), null);
        ImageDescriptor imageDescriptor = ImageDescriptor.createFromURL(url);
        getImageRegistry().put(imagePath, imageDescriptor);
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
}
