package com.ecmdeveloper.plugin.codemodule;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import com.ecmdeveloper.plugin.codemodule.util.ImageCache;
import com.ecmdeveloper.plugin.core.model.IObjectStoresManager;
import com.ecmdeveloper.plugin.core.model.tasks.ITaskManager;

public class Activator extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "com.ecmdeveloper.plugin.codemodule";

    private static Activator plugin;

    private static final ImageCache imageCache = new ImageCache();

    public Activator() {
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    public static Activator getDefault() {
        return plugin;
    }

    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }

    public static Image getImage(String path) {
        return imageCache.getImage(getImageDescriptor(path));
    }

    public IObjectStoresManager getObjectStoresManager() {
        return (IObjectStoresManager) getWorkbench().getService(IObjectStoresManager.class);
    }

    public ITaskManager getTaskManager() {
        return (ITaskManager) getWorkbench().getService(ITaskManager.class);
    }
}
