package org.docflower.rcp;

import org.docflower.page.PageBuilder;
import org.docflower.resources.ResourceManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "org.docflower.engine";

    private static Activator plugin;

    private FormColors formColors;

    private PageBuilder pageBuilder;

    private FormToolkit formToolkit;

    /**
     * The constructor
     */
    public Activator() {
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    public void stop(BundleContext context) throws Exception {
        plugin = null;
        dispose();
        ResourceManager.tryDispose();
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

    public void dispose() {
        if (formToolkit != null) {
            formToolkit.dispose();
            formToolkit = null;
        }
        if (formColors != null) {
            formColors.dispose();
            formColors = null;
        }
    }

    /**
     * Returns an image descriptor for the image file at the given plug-in
     * relative path
     * 
     * @param path
     *                the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }

    public FormColors getFormColors() {
        if (formColors == null) {
            formColors = new FormColors(Display.getDefault());
            formColors.markShared();
        }
        return formColors;
    }

    public FormToolkit getToolkit() {
        if (formToolkit == null) {
            formToolkit = new FormToolkit(getFormColors());
        }
        return formToolkit;
    }

    public PageBuilder getPageBuilder() {
        if (pageBuilder == null) {
            pageBuilder = new PageBuilder(getToolkit());
        }
        return pageBuilder;
    }
}
