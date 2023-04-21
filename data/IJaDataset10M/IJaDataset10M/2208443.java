package net.sf.amemailchecker.app.model.io;

import net.sf.amemailchecker.app.model.Settings;
import net.sf.amemailchecker.app.model.UserData;
import net.sf.amemailchecker.app.model.UserPreferences;
import net.sf.amemailchecker.gui.messageviewer.MessageViewerPreferences;
import java.io.*;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserPreferencesHelper {

    private static final Logger logger = Logger.getLogger(UserPreferencesHelper.class.getName());

    public void loadUserPreferences(Settings settings, UserData userData) throws IOException {
        File dataDir = new File(settings.getUserDataDirPath());
        if (!dataDir.exists()) dataDir.mkdir();
        try {
            loadPreferences(settings, userData);
        } catch (Exception e) {
            logger.info("user.preferences file is corrupt, deprecated or contains incorrect values. Replacing with defaults...");
            new File(settings.getUserPreferencesFilePath()).delete();
            loadPreferences(settings, userData);
        }
    }

    private void loadPreferences(Settings settings, UserData userData) throws IOException {
        UserPreferences preferences = new UserPreferences();
        MessageViewerPreferences messageViewerPreferences = new MessageViewerPreferences();
        userData.setPreferences(preferences);
        userData.setMessageViewerPreferences(messageViewerPreferences);
        Properties properties = new Properties();
        int languageCode = -1;
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(settings.getUserPreferencesFilePath());
        } catch (FileNotFoundException e) {
            inputStream = this.getClass().getClassLoader().getResourceAsStream(settings.getUserPreferencesFileDefault());
            languageCode = settings.findLanguageIdByLocale(Locale.getDefault());
        }
        properties.load(inputStream);
        inputStream.close();
        preferences.setMinCheckInterval(Integer.parseInt(properties.getProperty("minCheckInterval")));
        preferences.setNotificationDisplayDelay(Integer.parseInt(properties.getProperty("notificationDisplayDelay")));
        preferences.setEntryPointUsed(Integer.parseInt(properties.getProperty("entryPointUsed")));
        preferences.setNotificationTypeUsed(Integer.parseInt(properties.getProperty("mailNotificationTypeUsed")));
        preferences.setNotificationAnimatingType(Integer.valueOf(properties.getProperty("notificationAnimatingType")));
        preferences.setNotificationAlignmentX(Integer.valueOf(properties.getProperty("notificationAlignmentX")));
        preferences.setNotificationAlignmentY(Integer.valueOf(properties.getProperty("notificationAlignmentY")));
        preferences.setLanguageCode((languageCode == -1) ? Integer.parseInt(properties.getProperty("displayLanguage")) : languageCode);
        preferences.setEmptyCacheAccountInactive(Boolean.valueOf(properties.getProperty("emptyCacheAccountInactive")));
        preferences.setLook(Integer.valueOf(properties.getProperty("look")));
        String downloadsDirPath = properties.getProperty("downloadsDirPath");
        preferences.setDownloadsDirPath((downloadsDirPath != null) ? downloadsDirPath : settings.getUserDownloadsDirDefaultPath());
        messageViewerPreferences.setCloseMessageViewerToEntryPoint(Boolean.valueOf(properties.getProperty("closeMessageViewerToEntryPoint")));
        messageViewerPreferences.setRemoveDisabledFromAccountsView(Boolean.valueOf(properties.getProperty("removeDisabledFromAccountsView")));
        messageViewerPreferences.setRemoveDisabledFromFoldersView(Boolean.valueOf(properties.getProperty("removeDisabledFromFoldersView")));
    }

    public void writeUserPreferences(String path, UserData userData) {
        UserPreferences preferences = userData.getPreferences();
        MessageViewerPreferences messageViewerPreferences = userData.getMessageViewerPreferences();
        Properties properties = new Properties();
        properties.setProperty("minCheckInterval", Integer.toString(preferences.getMinCheckInterval()));
        properties.setProperty("notificationDisplayDelay", Integer.toString(preferences.getNotificationDisplayDelay()));
        properties.setProperty("entryPointUsed", Integer.toString(preferences.getEntryPointUsed()));
        properties.setProperty("mailNotificationTypeUsed", Integer.toString(preferences.getNotificationTypeUsed()));
        properties.setProperty("displayLanguage", Integer.toString(preferences.getLanguageCode()));
        properties.setProperty("notificationAnimatingType", Integer.toString(preferences.getNotificationAnimatingType()));
        properties.setProperty("notificationAlignmentX", Integer.toString(preferences.getNotificationAlignmentX()));
        properties.setProperty("notificationAlignmentY", Integer.toString(preferences.getNotificationAlignmentY()));
        properties.setProperty("emptyCacheAccountInactive", Boolean.toString(preferences.isEmptyCacheAccountInactive()));
        properties.setProperty("look", Integer.toString(preferences.getLook()));
        properties.setProperty("downloadsDirPath", preferences.getDownloadsDirPath());
        properties.setProperty("closeMessageViewerToEntryPoint", Boolean.toString(messageViewerPreferences.isCloseMessageViewerToEntryPoint()));
        properties.setProperty("removeDisabledFromAccountsView", Boolean.toString(messageViewerPreferences.isRemoveDisabledFromAccountsView()));
        properties.setProperty("removeDisabledFromFoldersView", Boolean.toString(messageViewerPreferences.isRemoveDisabledFromFoldersView()));
        try {
            FileWriter writer = new FileWriter(path);
            properties.store(writer, "Ame Mail Checker - user preferences");
        } catch (IOException e) {
            logger.log(Level.INFO, "Unable to write user.preferences file!");
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
