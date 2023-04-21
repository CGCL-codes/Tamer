package org.unitmetrics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.unitmetrics.internal.UnitMetricsFactory;

/**
 * The main plugin class to be used in the desktop.
 */
public class UnitMetricsPlugin extends AbstractUIPlugin {

    /** The plugin's id. */
    private static final String ID_PLUGIN = "org.unitmetrics";

    /** The plugin's preferences. */
    private static final Preferences preferences = new Preferences();

    private static UnitMetricsPlugin plugin;

    private ResourceBundle resourceBundle;

    private IAdvancedUnitMetrics unitMetrics;

    private static final String PERSIST_RESULT_STORE_FILE = "resultstore.data";

    public UnitMetricsPlugin() {
        super();
        plugin = this;
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
        if (getPreferences().isRememberResults()) restoreResultStore();
    }

    private void restoreResultStore() {
        File file = getResultStoreFile();
        if (file.exists()) {
            IWorkspace workspace = ResourcesPlugin.getWorkspace();
            restoreFromFile(file, workspace);
            deleteDataFile(file);
        }
    }

    private void restoreFromFile(File file, IWorkspace workspace) {
        try {
            FileInputStream in = new FileInputStream(file);
            getAdvancedUnitMetrics().restoreResults(workspace.getRoot(), in);
            in.close();
        } catch (Exception e) {
            log("Exception while restoring persisted results", e);
        }
    }

    private void deleteDataFile(File file) {
        file.delete();
    }

    public void stop(BundleContext context) throws Exception {
        if (getPreferences().isRememberResults()) storeResultStore();
        plugin = null;
        resourceBundle = null;
        super.stop(context);
    }

    private void storeResultStore() {
        File file = getResultStoreFile();
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            getAdvancedUnitMetrics().persistResults(out);
            out.close();
        } catch (Exception e) {
            log("Exception while persisting results", e);
        }
    }

    private File getResultStoreFile() {
        IPath stateLocation = getStateLocation();
        IPath path = stateLocation.append("/" + PERSIST_RESULT_STORE_FILE);
        File file = path.toFile();
        return file;
    }

    public static UnitMetricsPlugin getDefault() {
        return plugin;
    }

    public static String getPluginId() {
        return ID_PLUGIN;
    }

    public static IUnitMetrics getUnitMetrics() {
        return getAdvancedUnitMetrics();
    }

    /** Returns an advanced unit metrics object. A new one will be created if
	 * none exist using a two phase initialization process to avoid a recreation 
	 * in case of a propagated error. */
    public static IAdvancedUnitMetrics getAdvancedUnitMetrics() {
        UnitMetricsPlugin plugin = getDefault();
        synchronized (plugin) {
            if (plugin.unitMetrics == null) {
                plugin.unitMetrics = UnitMetricsFactory.createNonInitializedInstance();
                UnitMetricsFactory.setupFromContributions(plugin.unitMetrics);
            }
            return plugin.unitMetrics;
        }
    }

    /** Writes the given exception to the log. */
    public static void log(Throwable exception) {
        log("unexpected exception", exception);
    }

    /** Writes the given message and the given exception to the log. */
    public static void log(String message, Throwable exception) {
        log(new Status(IStatus.ERROR, getPluginId(), 0, message, exception));
    }

    /** Writes the status within the plugin's log. */
    public static void log(IStatus status) {
        getDefault().getLog().log(status);
    }

    public static String getResourceString(String key) {
        ResourceBundle bundle = UnitMetricsPlugin.getDefault().getResourceBundle();
        try {
            return (bundle != null) ? bundle.getString(key) : key;
        } catch (MissingResourceException e) {
            return key;
        }
    }

    public static Preferences getPreferences() {
        return preferences;
    }

    public ResourceBundle getResourceBundle() {
        try {
            if (resourceBundle == null) resourceBundle = ResourceBundle.getBundle("org.unitmetrics.UnitMetricsPluginResources");
        } catch (MissingResourceException x) {
            resourceBundle = null;
        }
        return resourceBundle;
    }

    public static class Preferences {

        private static final String PREFERENCE_PERSIST_RESULTS = "PERSIST_RESULTS";

        private IPreferenceStore getStore() {
            return getDefault().getPreferenceStore();
        }

        public boolean isRememberResults() {
            return getStore().getBoolean(PREFERENCE_PERSIST_RESULTS);
        }

        public void setRememberResults(boolean rememberResults) {
            getStore().setValue(PREFERENCE_PERSIST_RESULTS, rememberResults);
        }
    }
}
