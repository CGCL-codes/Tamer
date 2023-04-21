package net.sourceforge.squirrel_sql.plugins.refactoring.prefs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import net.sourceforge.squirrel_sql.client.Version;
import net.sourceforge.squirrel_sql.client.plugin.IPlugin;
import net.sourceforge.squirrel_sql.client.plugin.PluginException;
import net.sourceforge.squirrel_sql.client.plugin.PreferenceUtil;
import net.sourceforge.squirrel_sql.fw.util.log.ILogger;
import net.sourceforge.squirrel_sql.fw.util.log.LoggerController;
import net.sourceforge.squirrel_sql.fw.xml.XMLBeanReader;
import net.sourceforge.squirrel_sql.fw.xml.XMLBeanWriter;

public class RefactoringPreferencesManager {

    /** Logger for this class. */
    private static final ILogger s_log = LoggerController.createLogger(RefactoringPreferencesManager.class);

    /** Name of preferences file. */
    private static final String USER_PREFS_FILE_NAME = "prefs.xml";

    /** Folder to store user settings in. */
    private static File _userSettingsFolder;

    private static RefactoringPreferenceBean _prefs = null;

    private static IPlugin plugin = null;

    public static void initialize(IPlugin thePlugin) throws PluginException {
        plugin = thePlugin;
        try {
            _userSettingsFolder = plugin.getPluginUserSettingsFolder();
        } catch (IOException ex) {
            throw new PluginException(ex);
        }
        loadPrefs();
    }

    public static RefactoringPreferenceBean getPreferences() {
        return _prefs;
    }

    public static void unload() {
        savePrefs();
    }

    /**
     * Save preferences to disk.
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
            doc.load(prefFile, RefactoringPreferenceBean.class.getClassLoader());
            Iterator<?> it = doc.iterator();
            if (it.hasNext()) {
                _prefs = (RefactoringPreferenceBean) it.next();
            }
        } catch (FileNotFoundException ignore) {
            s_log.info(USER_PREFS_FILE_NAME + " not found - will be created");
        } catch (Exception ex) {
            s_log.error("Error occured reading from preferences file: " + USER_PREFS_FILE_NAME, ex);
        }
        if (_prefs == null) {
            _prefs = new RefactoringPreferenceBean();
        }
        _prefs.setClientName(Version.getApplicationName() + "/" + plugin.getDescriptiveName());
        _prefs.setClientVersion(Version.getShortVersion() + "/" + plugin.getVersion());
    }
}
