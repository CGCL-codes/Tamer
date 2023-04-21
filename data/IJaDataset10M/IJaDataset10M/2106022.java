package gov.sns.apps.launcher;

import gov.sns.tools.apputils.PathPreferenceSelector;
import java.util.prefs.*;
import java.net.*;

/**
 * PreferenceController is a convenience class for displaying a PathPreferenceSelector for 
 * the default document to open when the application launches.
 *
 * @author  tap
 */
public class PreferenceController {

    /** The key for the default document URL preference */
    private static final String DOCUMENT_KEY;

    /** User preferences for the Main class */
    private static final Preferences DEFAULTS;

    /** The suffix of the document for filtering the default document open browser */
    private static final String SUFFIX = ".launch";

    /** Description to appear in the default document open browser */
    private static final String DESCRIPTION = "Default launcher document";

    /**
	 * Static initializer
	 */
    static {
        DOCUMENT_KEY = "DEFAULT_DOCUMENT";
        DEFAULTS = getDefaults();
    }

    /**
	 * Constructor which is hidden since this class only has static methods.
	 */
    protected PreferenceController() {
    }

    /**
	 * Get the user preferences for this class
	 * @return the user preferences for this class
	 */
    protected static Preferences getDefaults() {
        return Preferences.userNodeForPackage(Main.class);
    }

    /**
	 * Get the URL of the default connection dictionary properties file
	 * @return the URL of the default connection dictionary properties file
	 * @throws java.net.MalformedURLException if the default URL spec cannot form a valid URL
	 */
    public static URL getDefaultDocumentURL() throws MalformedURLException {
        String urlSpec = getDefaultDocumentURLSpec();
        if (urlSpec == "" || urlSpec == null) {
            return null;
        } else {
            return new URL(urlSpec);
        }
    }

    /**
	 * Get the URL Spec of the default connection dictionary's properties file
	 * @return the URL Spec of the connection dictionary properties file
	 */
    public static String getDefaultDocumentURLSpec() {
        return getDefaults().get(DOCUMENT_KEY, "");
    }

    /**
	 * Display the PathPreferenceSelector with the specified Frame as the owner.
	 * @param owner The owner of the PathPreferenceSelector dialog.
	 */
    public static void displayPathPreferenceSelector(java.awt.Frame owner) {
        final PathPreferenceSelector selector;
        selector = new PathPreferenceSelector(owner, DEFAULTS, DOCUMENT_KEY, SUFFIX, DESCRIPTION);
        selector.setLocationRelativeTo(owner);
        selector.setVisible(true);
    }

    /**
	 * Display the PathPreferenceSelector with the specified Dialog as the owner.
	 * @param owner The owner of the PathPreferenceSelector dialog.
	 */
    public static void displayPathPreferenceSelector(java.awt.Dialog owner) {
        final PathPreferenceSelector selector;
        selector = new PathPreferenceSelector(owner, DEFAULTS, DOCUMENT_KEY, SUFFIX, DESCRIPTION);
        selector.setLocationRelativeTo(owner);
        selector.setVisible(true);
    }

    /**
	 * Display the PathPreferenceSelector with no owner.
	 */
    public static void displayPathPreferenceSelector() {
        final PathPreferenceSelector selector;
        selector = new PathPreferenceSelector(DEFAULTS, DOCUMENT_KEY, SUFFIX, DESCRIPTION);
        selector.setVisible(true);
    }
}
