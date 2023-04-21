package org.jtestcase.plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class JTestCasePlugin extends AbstractUIPlugin {

    private static final String ICONS_DIRECTORY = "icons";

    public static JTestCasePlugin plugin;

    public ResourceBundle resourceBundle;

    public IResource currentResource = null;

    private IJavaSearchScope mSearchScope;

    /**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path starting from <code>ICONS_DIRECTORY</code> dir.
	 * @param path the path  
	 * @return the image descriptor
	 */
    public static ImageDescriptor getImageDescriptor(String filename) {
        ImageDescriptor descriptor = AbstractUIPlugin.imageDescriptorFromPlugin("org.jtestcase.plugin", ICONS_DIRECTORY + File.separator + filename);
        return descriptor;
    }

    /**
	 * constructs the plugin and get the resources as a bundle
	 * org.jtestcase.plugin.XmlViewerEditorPluginResources resourceBundle is null on
	 * exception
	 */
    public JTestCasePlugin() {
        plugin = this;
        try {
            resourceBundle = ResourceBundle.getBundle("org.jtestcase.plugin.resources.JTestCasePluginResources");
        } catch (MissingResourceException _ex) {
            resourceBundle = null;
        }
    }

    /**
	 * This method is called upon plug-in activation
	 */
    public void start(BundleContext context) throws Exception {
        super.start(context);
    }

    /**
	 * This method is called when the plug-in is stopped
	 */
    public void stop(BundleContext context) throws Exception {
        super.stop(context);
    }

    public static JTestCasePlugin getDefault() {
        return plugin;
    }

    /**
	 * gets <code>key</code> from bundle if bundle is not null. otherwise
	 * return <code>key</code>
	 * 
	 * @param key
	 * @return
	 */
    public static String getResourceString(String key) {
        ResourceBundle bundle = getDefault().getResourceBundle();
        try {
            return bundle == null ? key : bundle.getString(key);
        } catch (MissingResourceException _ex) {
            return key;
        }
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public InputStream getFileAsStream(String resource) {
        try {
            return getDefault().openStream(new Path(resource));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
