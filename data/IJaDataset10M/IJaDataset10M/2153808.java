package com.ilog.translator.java2cs.plugin;

import java.util.Dictionary;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class TranslationPlugin extends AbstractUIPlugin {

    private static TranslationPlugin plugin;

    /**
	 * The constructor.
	 */
    public TranslationPlugin() {
        TranslationPlugin.plugin = this;
    }

    /**
	 * This method is called upon plug-in activation
	 */
    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
    }

    /**
	 * This method is called when the plug-in is stopped
	 */
    @Override
    public void stop(BundleContext context) throws Exception {
        super.stop(context);
        TranslationPlugin.plugin = null;
    }

    /**
	 * Returns the shared instance.
	 */
    public static TranslationPlugin getDefault() {
        return TranslationPlugin.plugin;
    }

    /**
	 * Returns the workspace instance.
	 */
    public static IWorkspace getWorkspace() {
        return ResourcesPlugin.getWorkspace();
    }

    /**
	 * Returns an import handler, i.e. a class responsible for importing
	 * projects.
	 */
    public static ITranslationHandler getTranslationHandler() {
        return new TranslationHandler();
    }

    public static void log(CoreException ce) {
        TranslationPlugin.getDefault().getLog().log(ce.getStatus());
    }

    /**
	 * Returns the plugin ID.
	 */
    public static String getID() {
        return getDefault().getBundle().getSymbolicName();
    }

    /**
	 * Returns the plugin version.
	 */
    @SuppressWarnings("unchecked")
    public static String getVersion() {
        final Dictionary map = getDefault().getBundle().getHeaders();
        final String version = (String) map.get("Bundle-Version");
        return version;
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
        return AbstractUIPlugin.imageDescriptorFromPlugin(getID(), path);
    }
}
