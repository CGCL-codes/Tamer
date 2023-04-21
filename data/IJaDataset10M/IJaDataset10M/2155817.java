package org.j2eespider;

import java.util.StringTokenizer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 * The manifest calls that class for initialize the project.
 */
public class SpiderPlugin extends AbstractUIPlugin {

    private static SpiderPlugin plugin;

    public static String ID = "org.spideronrails.core";

    private static String PREFERENCE_DELIMITER = ";";

    public static final String PREFERENCE_REPOSITORIES = "repositories";

    public static final String PREFERENCE_REPOSITORY_TIMEOUT = "repository_timeout";

    public static final String PREFERENCE_TEMPLATE_PATH = "template_path";

    public static final String PREFERENCE_MERGETOOL_PATH = "mergetool_path";

    public static final String DEFAULT_REPOSITORIES = "http://www.j2eespider.org/repository/repo.xml;http://www.j2eespider.org/repository/nightly/repo.xml;";

    public static final int DEFAULT_TIMEOUT_REPOSITORY = 3000;

    /**
	 * The constructor.
	 */
    public SpiderPlugin() {
        plugin = this;
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
    public static SpiderPlugin getDefault() {
        return plugin;
    }

    /**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path.
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
    public static ImageDescriptor getImageDescriptor(String path) {
        return AbstractUIPlugin.imageDescriptorFromPlugin(SpiderPlugin.ID, path);
    }

    @Override
    protected void initializeDefaultPreferences(IPreferenceStore store) {
        store.setDefault(PREFERENCE_REPOSITORIES, DEFAULT_REPOSITORIES);
        store.setDefault(PREFERENCE_REPOSITORY_TIMEOUT, DEFAULT_TIMEOUT_REPOSITORY);
        store.setDefault(PREFERENCE_TEMPLATE_PATH, "");
        store.setDefault(PREFERENCE_MERGETOOL_PATH, "");
    }

    /**
	 * Return the repositories preference default.
	 * as an array of Strings.
	 * @return String[]
	 */
    public String[] getDefaultRepositoryPreference() {
        return convert(getPreferenceStore().getDefaultString(PREFERENCE_REPOSITORIES));
    }

    /**
	 * Return timeout default to repositories.
	 * @return
	 */
    public int getDefaultRepositoryTimeoutPreference() {
        return 1000;
    }

    /**
	 * Return the repositories preference as an array of Strings.
	 * @return String[]
	 */
    public String[] getRepositoryPreference() {
        return convert(getPreferenceStore().getString(PREFERENCE_REPOSITORIES));
    }

    /**
	 * Return timeout to repositories.
	 * @return
	 */
    public int getRepositoryTimeoutPreference() {
        return getPreferenceStore().getInt(PREFERENCE_REPOSITORY_TIMEOUT);
    }

    /**
	 * Return template path.
	 * @return
	 */
    public String getTemplatePathPreference() {
        return getPreferenceStore().getString(PREFERENCE_TEMPLATE_PATH);
    }

    /**
	 * Return mergetool path.
	 * @return
	 */
    public String getMergeToolPreference() {
        return getPreferenceStore().getString(PREFERENCE_MERGETOOL_PATH);
    }

    /**
	 * Convert the supplied PREFERENCE_DELIMITER delimited
	 * String to a String array.
	 * @return String[]
	 */
    private String[] convert(String preferenceValue) {
        StringTokenizer tokenizer = new StringTokenizer(preferenceValue, PREFERENCE_DELIMITER);
        int tokenCount = tokenizer.countTokens();
        String[] elements = new String[tokenCount];
        for (int i = 0; i < tokenCount; i++) {
            elements[i] = tokenizer.nextToken();
        }
        return elements;
    }

    /**
	 * Set the repositories preference
	 * @param String [] elements - the Strings to be 
	 * 	converted to the preference value
	 */
    public void setRepositoryPreference(String[] elements) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < elements.length; i++) {
            buffer.append(elements[i]);
            buffer.append(PREFERENCE_DELIMITER);
        }
        getPreferenceStore().setValue(PREFERENCE_REPOSITORIES, buffer.toString());
    }

    /**
	 * Set timeout preference
	 * @param timeout
	 */
    public void setRepositoryTimeoutPreference(int timeout) {
        getPreferenceStore().setValue(PREFERENCE_REPOSITORY_TIMEOUT, timeout);
    }

    /**
	 * Set template path preference
	 * @param timeout
	 */
    public void setTemplatePathPreference(String path) {
        getPreferenceStore().setValue(PREFERENCE_TEMPLATE_PATH, path);
    }

    /**
	 * Set merge tool preference
	 * @param timeout
	 */
    public void setMergeToolPathPreference(String path) {
        getPreferenceStore().setValue(PREFERENCE_MERGETOOL_PATH, path);
    }
}
