package org.modss.facilitator.shared.init;

import org.modss.facilitator.shared.pref.UserPreferences;
import org.modss.facilitator.shared.resource.ConfigurableResourceProvider;
import org.modss.facilitator.shared.singleton.Singleton;
import javax.swing.*;
import java.util.Locale;

/**
 * Language functionality.
 */
public class Language {

    /** Bundles associated with languages. */
    public static final String[] BUNDLES = new String[] { "org.modss.facilitator.properties.facilitator_en", "org.modss.facilitator.properties.facilitator_es" };

    /** Human readable language options. */
    public static final String[] OPTIONS = new String[] { "English", "Spanish" };

    /** Language properties. */
    public static final String LANGUAGE_KEY = "dss.language";

    /**
     * Initialise the language settings.  This may involve obtaining the
     * language selection from the user via a UI dialog.
     */
    public static void init(ConfigurableResourceProvider rp) {
        String lang = rp.getProperty(LANGUAGE_KEY);
        if (lang == null) lang = getLanguageFromUser();
        for (int i = 0; i < OPTIONS.length; i++) {
            if (lang.equalsIgnoreCase(OPTIONS[i])) {
                if (lang.equals("spanish")) Locale.setDefault(new Locale("es", "MX"));
                rp.addResourceBundle(BUNDLES[i]);
                break;
            }
        }
    }

    /**
     * Obtain the language from the user via UI.
     */
    private static String getLanguageFromUser() {
        JFrame frame = new JFrame();
        int selection = JOptionPane.showOptionDialog(frame, "Select a language:", "Specify Language", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, OPTIONS, null);
        if (selection == JOptionPane.CLOSED_OPTION) {
            System.err.println("You must specify a language to run the Facilitator under");
            System.exit(1);
        }
        String lang = OPTIONS[selection].toLowerCase();
        up.setProperty(LANGUAGE_KEY, lang, frame);
        return lang;
    }

    /** User preferences reference. */
    private static final UserPreferences up = Singleton.Factory.getInstance().getUserPreferences();
}
