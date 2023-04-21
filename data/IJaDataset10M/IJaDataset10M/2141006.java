package org.formaria.editor.eclipse;

import javax.swing.UIManager;
import org.formaria.editor.eclipse.project.pages.EditorUtility;
import org.formaria.editor.project.EditorProjectManager;
import org.formaria.editor.project.pages.PageDesigner;
import org.formaria.editor.project.pages.PageResource;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class AriaEditorPlugin extends AbstractUIPlugin {

    private static AriaEditorPlugin plugin;

    /**
   * The constructor.
   */
    public AriaEditorPlugin() {
        plugin = this;
        EditorUtility eu = new EditorUtility();
        PageDesigner.setEditorUtility(eu, false);
        PageResource.setEditorUtility(eu, false);
        EditorProjectManager.setEditorUtility(eu, true);
        try {
            String lafName = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
            UIManager.setLookAndFeel(lafName);
        } catch (Exception e) {
            System.err.println("Can't set look & feel:" + e);
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
        plugin = null;
    }

    /**
   * Returns the shared instance.
   */
    public static AriaEditorPlugin getDefault() {
        return plugin;
    }

    /**
   * Returns an image descriptor for the image file at the given plug-in
   * relative path.
   * 
   * @param path
   *          the path
   * @return the image descriptor
   */
    public static ImageDescriptor getImageDescriptor(String path) {
        return AbstractUIPlugin.imageDescriptorFromPlugin("AriaEditor", path);
    }
}
