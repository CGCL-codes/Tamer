package org.idfl.javadoc.graphviz;

import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

/**
 * The activator class controls the plug-in life cycle
 */
public class GraphvizJavadocPlugin extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "org.idfl.javadoc.graphviz.GraphvizJavadocPlugin";

    public static final String PREF_TEMP_DIR = "PREF_TEMP_DIR";

    public static final String PREF_TEMP_FILE_LIFE_TIME = "PREF_TEMP_FILE_LIFE_TIME";

    public static final String PREF_ENGINE_PATH = "PREF_ENGINE_PATH";

    private static GraphvizJavadocPlugin plugin;

    /**
	 * The constructor
	 */
    public GraphvizJavadocPlugin() {
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
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
    public static GraphvizJavadocPlugin getDefault() {
        return plugin;
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

    public void initDefaultPreferences() {
        setTempPath(getDefaultTempDir());
        setOldFileMaxLifeInMin(10);
        setEnginePath("dot", "dot");
        setEnginePath("neato", "neato");
        setEnginePath("fdp", "fdp");
        setEnginePath("sfdp", "sfdp");
        setEnginePath("twopi", "twopi");
        setEnginePath("circo", "circo");
    }

    private String getDefaultTempDir() {
        return getStateLocation().append("temp").toOSString();
    }

    public String getTempPath() {
        IEclipsePreferences node = new DefaultScope().getNode(GraphvizJavadocPlugin.PLUGIN_ID);
        return node.get(PREF_TEMP_DIR, getDefaultTempDir());
    }

    public int getOldFileMaxLifeInMin() {
        IEclipsePreferences node = new DefaultScope().getNode(GraphvizJavadocPlugin.PLUGIN_ID);
        return node.getInt(PREF_TEMP_FILE_LIFE_TIME, 10);
    }

    public String getEnginePath(String engine) {
        IEclipsePreferences node = new DefaultScope().getNode(GraphvizJavadocPlugin.PLUGIN_ID);
        Preferences engines = node.node(PREF_ENGINE_PATH);
        return engines.get(engine, engine);
    }

    public void setEnginePath(String engine, String path) {
        IEclipsePreferences node = new DefaultScope().getNode(PLUGIN_ID);
        Preferences engines = node.node(PREF_ENGINE_PATH);
        engines.put(engine, path);
    }

    public String[] getEngines() {
        IEclipsePreferences node = new DefaultScope().getNode(PLUGIN_ID);
        Preferences engines = node.node(PREF_ENGINE_PATH);
        try {
            return engines.keys();
        } catch (BackingStoreException e) {
            return new String[] { "dot", "neato", "fdp", "sfdp", "twopi", "circo" };
        }
    }

    public void setTempPath(String tempDir) {
        IEclipsePreferences node = new DefaultScope().getNode(PLUGIN_ID);
        node.put(PREF_TEMP_DIR, tempDir);
    }

    public void setOldFileMaxLifeInMin(int min) {
        IEclipsePreferences node = new DefaultScope().getNode(PLUGIN_ID);
        node.putInt(PREF_TEMP_FILE_LIFE_TIME, min);
    }

    public void logError(String message) {
        getLog().log(new Status(Status.ERROR, PLUGIN_ID, message, null));
    }
}
