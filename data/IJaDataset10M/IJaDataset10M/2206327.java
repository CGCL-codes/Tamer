package net.sf.beatrix.internal.module.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 * 
 * @author Christian Wressnegger <chwress@users.sourceforge.net>
 */
public class Activator extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "net.sf.beatrix.module.ui";

    private static Activator plugin;

    /**
   * The constructor
   */
    public Activator() {
    }

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    @Override
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
   * Returns an image descriptor for the image file at the given plug-in
   * relative path
   * 
   * @param path
   *          the path
   * @return the image descriptor
   */
    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }
}
