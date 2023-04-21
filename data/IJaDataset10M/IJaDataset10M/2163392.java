package com.aptana.ide.editor.js;

import java.util.Hashtable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class JSPlugin extends AbstractUIPlugin {

    private static Hashtable images = new Hashtable();

    private static JSPlugin plugin;

    /**
	 * The ID for this plug-in
	 */
    public static String ID = "com.aptana.ide.editor.js";

    /**
	 * The constructor.
	 */
    public JSPlugin() {
        plugin = this;
    }

    /**
	 * This method is called upon plug-in activation
	 * @param context 
	 * @throws Exception 
	 */
    public void start(BundleContext context) throws Exception {
        super.start(context);
    }

    /**
	 * This method is called when the plug-in is stopped
	 * @param context 
	 * @throws Exception 
	 */
    public void stop(BundleContext context) throws Exception {
        super.stop(context);
        plugin = null;
    }

    /**
	 * Returns the shared instance.
	 * @return JSPlugin
	 */
    public static JSPlugin getDefault() {
        return plugin;
    }

    /**
	 * getImage
	 * 
	 * @param path
	 * @return Image
	 */
    public static Image getImage(String path) {
        if (images.get(path) == null) {
            ImageDescriptor id = getImageDescriptor(path);
            if (id == null) {
                return null;
            }
            Image i = id.createImage();
            images.put(path, i);
            return i;
        } else {
            return (Image) images.get(path);
        }
    }

    /**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path.
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
    public static ImageDescriptor getImageDescriptor(String path) {
        return AbstractUIPlugin.imageDescriptorFromPlugin("com.aptana.ide.editor.js", path);
    }
}
