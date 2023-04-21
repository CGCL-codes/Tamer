package uk.ac.essex.ia.pref;

import org.apache.log4j.Category;
import uk.ac.essex.common.gui.OptionsHandlerImpl;
import uk.ac.essex.common.pref.PreferenceObject;
import uk.ac.essex.common.lang.LanguageManager;
import uk.ac.essex.common.pref.PreferencePanel;
import uk.ac.essex.ia.lang.LanguageConstants;
import uk.ac.essex.ia.lang.LanguageManagerImpl;
import uk.ac.essex.ia.ImageApp;
import java.util.Locale;
import java.util.prefs.Preferences;

/**
 *
 * <br>
 * Date: 15-Jul-2002 <br>
 *
 * @author Laurence Smith
 *
 * You should have received a copy of GNU public license with this code.
 * If not please visit <a href="www.gnu.org/copyleft/gpl.html">this site </a>
 */
public class GlobalOptionsHandler extends OptionsHandlerImpl implements GlobalPreferences {

    /** The log4j logger */
    private static transient Category category = Category.getInstance(GlobalOptionsHandler.class);

    private LanguageManager languageManager;

    private Preferences preferences = Preferences.userNodeForPackage(GlobalOptionsHandler.class);

    /**
     *
     */
    public GlobalOptionsHandler() {
        preferencePanel = new GlobalOptionsPanel(this);
        readOptions();
    }

    /**
     * Save the pref for this manager
     */
    public void saveOptions() {
        GlobalOptionsPanel globalPanel = (GlobalOptionsPanel) preferencePanel;
        Locale lang = globalPanel.getLanguage();
        initLanguageManager();
        languageManager.setDefaultLocale(lang);
        String lAndFClassName = globalPanel.getLookAndFeelClassName();
        category.debug("Look and feel class name to save = " + lAndFClassName);
        preferences.put(LOOK_AND_FEEL, lAndFClassName);
        ImageApp.getInstance().setUI(lAndFClassName);
        category.debug("Preferences saved");
    }

    /**
     *
     * @return
     */
    public PreferenceObject readOptions() {
        initLanguageManager();
        Locale lang = languageManager.getDefaultLocale();
        PreferenceObject prefObject = preferencePanel.getPreferenceObject();
        prefObject.setPreference(LANGUAGE, lang);
        String lf = preferences.get(LOOK_AND_FEEL, DEFAULT_LOOK_AND_FEEL);
        prefObject.setPreference(LOOK_AND_FEEL, lf);
        preferencePanel.setPreferenceObject(prefObject);
        category.debug("Read preferences");
        return prefObject;
    }

    /**
     *
     */
    private void initLanguageManager() {
        if (languageManager == null) languageManager = LanguageManagerImpl.getInstance();
    }
}
