package net.sourceforge.squirrel_sql.plugins.dataimport.prefs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import net.sourceforge.squirrel_sql.client.plugin.IPlugin;
import net.sourceforge.squirrel_sql.client.plugin.PluginException;
import net.sourceforge.squirrel_sql.client.plugin.PreferenceUtil;
import net.sourceforge.squirrel_sql.fw.util.log.ILogger;
import net.sourceforge.squirrel_sql.fw.util.log.LoggerController;
import net.sourceforge.squirrel_sql.fw.xml.XMLBeanReader;
import net.sourceforge.squirrel_sql.fw.xml.XMLBeanWriter;

/**
 * This class manages the preferences.
 * 
 * @author Thorsten Mürell
 */
public class PreferencesManager {

    /** Logger for this class. */
    private static final ILogger s_log = LoggerController.createLogger(PreferencesManager.class);

    /** Name of preferences file. */
    private static final String USER_PREFS_FILE_NAME = "prefs.xml";

    /** Folder to store user settings in. */
    private static File _userSettingsFolder;

    private static DataImportPreferenceBean _prefs = null;

    private static IPlugin plugin = null;

    /**
     * Initializes the PreferencesManager
     * 
     * @param thePlugin
     * @throws PluginException
     */
    public static void initialize(IPlugin thePlugin) throws PluginException {
        plugin = thePlugin;
        try {
            _userSettingsFolder = plugin.getPluginUserSettingsFolder();
        } catch (IOException ex) {
            throw new PluginException(ex);
        }
        loadPrefs();
    }

    /**
     * Returns the preferences holder
     * 
     * @return The bean holding the preferences for the dataimport plugin.
     */
    public static DataImportPreferenceBean getPreferences() {
        return _prefs;
    }

    /**
     * Saves the preferences.
     */
    public static void unload() {
        savePrefs();
    }

    /**
     * Save preferences to disk.  Always write to the user settings folder, not
     * the application settings folder.
     */
    public static void savePrefs() {
        try {
            XMLBeanWriter wtr = new XMLBeanWriter(_prefs);
            wtr.save(new File(_userSettingsFolder, USER_PREFS_FILE_NAME));
        } catch (Exception ex) {
            s_log.error("Error occured writing to preferences file: " + USER_PREFS_FILE_NAME, ex);
        }
    }

    /**
     * Load from preferences file.
     */
    private static void loadPrefs() {
        try {
            XMLBeanReader doc = new XMLBeanReader();
            File prefFile = PreferenceUtil.getPreferenceFileToReadFrom(plugin);
            doc.load(prefFile, DataImportPreferenceBean.class.getClassLoader());
            Iterator<?> it = doc.iterator();
            if (it.hasNext()) {
                _prefs = (DataImportPreferenceBean) it.next();
            }
        } catch (FileNotFoundException ignore) {
            s_log.info(USER_PREFS_FILE_NAME + " not found - will be created");
        } catch (Exception ex) {
            s_log.error("Error occured reading from preferences file: " + USER_PREFS_FILE_NAME, ex);
        }
        if (_prefs == null) {
            _prefs = new DataImportPreferenceBean();
        }
    }
}
