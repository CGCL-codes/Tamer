package org.parallelj.tools.typeselector.ext.java;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "org.parallelj.tools.typeselector.ext.java";

    private static Activator plugin;

    /**
	 * The constructor
	 */
    public Activator() {
    }

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        Activator.plugin = this;
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        Activator.plugin.getImageRegistry().dispose();
        Activator.plugin = null;
        super.stop(context);
    }

    /**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
    public static Activator getDefault() {
        return Activator.plugin;
    }

    /**
	 * Logs message with INFO severity in Error Log
	 * 
	 * @param message
	 *            : String
	 */
    public void logInfo(String message) {
        this.getLog().log(new Status(IStatus.INFO, Activator.PLUGIN_ID, message));
    }

    /**
	 * Logs message with WARNING severity in Error Log
	 * 
	 * @param message
	 *            : String
	 */
    public void logWarning(String message) {
        this.getLog().log(new Status(IStatus.WARNING, Activator.PLUGIN_ID, message));
    }

    /**
	 * Logs message with ERROR severity in Error Log
	 * 
	 * @param message
	 *            : String
	 */
    public void logError(String message) {
        this.getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, message));
    }

    /**
	 * Logs message and Throwable with Error severity in Error Log
	 * 
	 * @param message
	 *            : String
	 * @param t
	 *            : Throwable
	 */
    public void logError(String message, Throwable t) {
        this.getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, message, t));
    }

    /**
	 * Returns image in current plugin
	 * 
	 * @param imageFilePath
	 *            : image File Path in plugin
	 * @return Image if exists
	 */
    public Image getImage(String imageFilePath) {
        return getImage(Activator.PLUGIN_ID, imageFilePath);
    }

    /**
	 * Returns image in plugin
	 * 
	 * @param pluginId
	 *            : Id of the plugin containing thie image
	 * @param imageFilePath
	 *            : image File Path in plugin
	 * @return Image if exists
	 */
    public Image getImage(String pluginId, String imageFilePath) {
        Image image = Activator.getDefault().getImageRegistry().get(pluginId + ":" + imageFilePath);
        if (image == null) {
            image = loadImage(pluginId, imageFilePath);
        }
        return image;
    }

    /**
	 * Loads image in Image Registry is not available in it
	 * @param pluginId
	 *            : Id of the plugin containing thie image
	 * @param imageFilePath
	 *            : image File Path in plugin
	 * @return Image if loaded
	 */
    private synchronized Image loadImage(String pluginId, String imageFilePath) {
        String id = pluginId + ":" + imageFilePath;
        Image image = Activator.getDefault().getImageRegistry().get(id);
        if (image != null) return image;
        ImageDescriptor imageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, imageFilePath);
        if (imageDescriptor != null) {
            image = imageDescriptor.createImage();
            Activator.getDefault().getImageRegistry().put(pluginId + ":" + imageFilePath, image);
        }
        return image;
    }
}
