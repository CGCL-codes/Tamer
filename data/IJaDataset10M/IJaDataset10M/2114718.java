package com.aptana.ide.editors;

import java.util.Hashtable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import com.aptana.ide.editors.profiles.ProfileManager;
import com.aptana.ide.editors.views.actions.ActionsManager;
import com.aptana.ide.editors.views.outline.OutlineManager;

/**
 * The main plugin class to be used in the desktop.
 */
public class UnifiedEditorsPlugin extends AbstractUIPlugin {

    private static Hashtable images = new Hashtable();

    private static UnifiedEditorsPlugin plugin;

    private boolean enableThreading = true;

    private OutlineManager outlineManager;

    /**
	 * CLASS_ATTR
	 */
    public static final String CLASS_ATTR = "class";

    /**
	 * OUTLINE_ATTR
	 */
    public static final String OUTLINE_ATTR = "outline";

    /**
	 * LABEL_NODE
	 */
    public static final String LABEL_NODE = "label";

    /**
	 * OS_ATTR
	 */
    public static final String OS_ATTR = "os";

    /**
	 * VALUE_ATTR
	 */
    public static final String VALUE_ATTR = "value";

    /**
	 * EXTENSION_POINT
	 */
    public static final String EXTENSION_POINT = "com.aptana.ide.editors.browser";

    /**
	 * The constructor.
	 */
    public UnifiedEditorsPlugin() {
        plugin = this;
    }

    /**
	 * This method is called upon plug-in activation
	 * 
	 * @param context
	 * @throws Exception
	 */
    public void start(BundleContext context) throws Exception {
        super.start(context);
    }

    /**
	 * This method is called when the plug-in is stopped
	 * 
	 * @param context
	 * @throws Exception
	 */
    public void stop(BundleContext context) throws Exception {
        super.stop(context);
        plugin = null;
    }

    /**
	 * Returns the shared instance.
	 * 
	 * @return returns default plugin
	 */
    public static UnifiedEditorsPlugin getDefault() {
        return plugin;
    }

    /**
	 * Returns an image descriptor for the image file at the given plug-in relative path.
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
    public static ImageDescriptor getImageDescriptor(String path) {
        return AbstractUIPlugin.imageDescriptorFromPlugin("com.aptana.ide.editors", path);
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

    ProfileManager profileManager;

    /**
	 * getProfileManager
	 * 
	 * @return ProfileManager
	 */
    public ProfileManager getProfileManager() {
        if (profileManager == null) {
            profileManager = new ProfileManager(enableThreading);
        }
        return profileManager;
    }

    ActionsManager actionsManager;

    /**
	 * getActionsManager
	 * 
	 * @return ActionsManager
	 */
    public ActionsManager getActionsManager() {
        if (actionsManager == null) {
            actionsManager = new ActionsManager();
        }
        return actionsManager;
    }

    /**
	 * getOutlineManager
	 * 
	 * @return OutlineManager
	 */
    public OutlineManager getOutlineManager() {
        if (outlineManager == null) {
            outlineManager = new OutlineManager();
        }
        return outlineManager;
    }

    /**
	 * @return Returns true if threading is enabled (true by default).
	 */
    public boolean isThreadingEnabled() {
        return enableThreading;
    }

    /**
	 * @param enableThreading
	 *            Turns threading on or off.
	 */
    public void setEnableThreading(boolean enableThreading) {
        this.enableThreading = enableThreading;
    }
}
