package net.sourceforge.squirrel_sql.plugins.dbdiff.prefs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import net.sourceforge.squirrel_sql.client.Version;
import net.sourceforge.squirrel_sql.client.plugin.IPlugin;
import net.sourceforge.squirrel_sql.client.plugin.PluginException;
import net.sourceforge.squirrel_sql.client.plugin.PreferenceUtil;
import net.sourceforge.squirrel_sql.fw.util.FileWrapper;
import net.sourceforge.squirrel_sql.fw.util.log.ILogger;
import net.sourceforge.squirrel_sql.fw.util.log.LoggerController;
import net.sourceforge.squirrel_sql.fw.xml.XMLBeanReader;
import net.sourceforge.squirrel_sql.fw.xml.XMLBeanWriter;

/**
 * A default implementation of the PluginPreferencesManager interface which allows a plugin to manage it's
 * preferences using an XML bean.
 */
public class DefaultPluginPreferencesManager implements IPluginPreferencesManager {

    /** Logger for this class. */
    private static final ILogger s_log = LoggerController.createLogger(DefaultPluginPreferencesManager.class);

    /** Name of preferences file. */
    private final String USER_PREFS_FILE_NAME = "prefs.xml";

    /** Folder to store user settings in. */
    protected FileWrapper _userSettingsFolder;

    protected IPluginPreferenceBean _prefs = null;

    protected IPlugin plugin = null;

    protected Class<? extends IPluginPreferenceBean> preferenceBeanClass = null;

    /**
	 * @see net.sourceforge.squirrel_sql.plugins.dbdiff.prefs.IPluginPreferencesManager#
	 *      initialize(net.sourceforge.squirrel_sql.client.plugin.IPlugin, java.lang.Class)
	 */
    public void initialize(IPlugin thePlugin, Class<? extends IPluginPreferenceBean> preferenceBeanClass) throws PluginException {
        this.plugin = thePlugin;
        this.preferenceBeanClass = preferenceBeanClass;
        try {
            _userSettingsFolder = plugin.getPluginUserSettingsFolder();
        } catch (final IOException ex) {
            throw new PluginException(ex);
        }
        loadPrefs();
    }

    /**
	 * @see net.sourceforge.squirrel_sql.plugins.dbdiff.prefs.IPluginPreferencesManager#getPreferences()
	 */
    public IPluginPreferenceBean getPreferences() {
        return _prefs;
    }

    /**
	 * @see net.sourceforge.squirrel_sql.plugins.dbdiff.prefs.IPluginPreferencesManager#unload()
	 */
    public void unload() {
        savePrefs();
    }

    /**
	 * @see net.sourceforge.squirrel_sql.plugins.dbdiff.prefs.IPluginPreferencesManager#savePrefs()
	 */
    public void savePrefs() {
        try {
            final XMLBeanWriter wtr = new XMLBeanWriter(_prefs);
            wtr.save(new File(_userSettingsFolder.getAbsolutePath(), USER_PREFS_FILE_NAME));
        } catch (final Exception ex) {
            s_log.error("Error occured writing to preferences file: " + USER_PREFS_FILE_NAME, ex);
        }
    }

    /**
	 * Load from preferences file.
	 */
    private void loadPrefs() {
        FileWrapper prefFile = null;
        try {
            final XMLBeanReader doc = new XMLBeanReader();
            prefFile = PreferenceUtil.getPreferenceFileToReadFrom(plugin);
            doc.load(prefFile, preferenceBeanClass.getClassLoader());
            final Iterator<Object> it = doc.iterator();
            if (it.hasNext()) {
                _prefs = (DBDiffPreferenceBean) it.next();
            }
        } catch (final FileNotFoundException ignore) {
            s_log.info(USER_PREFS_FILE_NAME + "(" + prefFile.getAbsolutePath() + ") not found - will be created");
        } catch (final Exception ex) {
            s_log.error("Error occured reading from preferences file: " + USER_PREFS_FILE_NAME, ex);
        }
        if (_prefs == null) {
            _prefs = new DBDiffPreferenceBean();
        }
        _prefs.setClientName(Version.getApplicationName() + "/" + plugin.getDescriptiveName());
        _prefs.setClientVersion(Version.getShortVersion() + "/" + plugin.getVersion());
    }
}
