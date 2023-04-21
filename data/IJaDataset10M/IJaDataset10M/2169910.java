package com.inetmon.jn.vlananalyzerview;

import java.net.URL;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class VLAnalyzerViewActivator extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "com.inetmon.jn.VLANAnalyzerView";

    private static VLAnalyzerViewActivator plugin;

    public static final String START_ANALYZE = "start";

    public static final String STOP_ANALYZE = "stop";

    public static final String OPEN_ANALYZE = "open";

    public static final String IMG_FILTER = "filter";

    public static final String IMG_CHILD1 = "child1";

    public static final String IMG_CHILD2 = "child2";

    public static final String IMG_ROOT1 = "root1";

    public static final String IMG_SAVE = "save2";

    public static final String IMG_SCROLL = "scroll";

    public static final String IMG_COLOR = "edit1";

    public static final String IMG_LEFTA = "lefta";

    public static final String IMG_RIGTHA = "righta";

    public static final String IMG_SEARCH = "search";

    /**
	 * The constructor
	 */
    public VLAnalyzerViewActivator() {
        plugin = this;
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
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
    public static VLAnalyzerViewActivator getDefault() {
        return plugin;
    }

    /** Initialize Image Registry */
    protected void initializeImageRegistry(ImageRegistry registry) {
        registerImage(registry, START_ANALYZE, "start.gif");
        registerImage(registry, STOP_ANALYZE, "stop.gif");
        registerImage(registry, OPEN_ANALYZE, "open.gif");
        registerImage(registry, IMG_FILTER, "filter2.jpg");
        registerImage(registry, IMG_CHILD1, "child1.gif");
        registerImage(registry, IMG_CHILD2, "child2.gif");
        registerImage(registry, IMG_ROOT1, "root1.gif");
        registerImage(registry, IMG_SAVE, "save2.ico");
        registerImage(registry, IMG_SCROLL, "scroll.ico");
        registerImage(registry, IMG_COLOR, "edit1.png");
        registerImage(registry, IMG_RIGTHA, "righta2.jpg");
        registerImage(registry, IMG_LEFTA, "lefta2.gif");
        registerImage(registry, IMG_SEARCH, "search.ico");
    }

    /** register Image */
    private void registerImage(ImageRegistry registry, String key, String fileName) {
        try {
            IPath path = new Path("icons/" + fileName);
            URL url = find(path);
            if (url != null) {
                ImageDescriptor desc = ImageDescriptor.createFromURL(url);
                registry.put(key, desc);
            }
        } catch (Exception e) {
        }
    }

    /** Get the image */
    public Image getImage(String key) {
        return getImageRegistry().get(key);
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
}
