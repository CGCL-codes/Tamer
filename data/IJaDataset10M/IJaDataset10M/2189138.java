package com.cronopista.lightpacker.IDE;

import java.util.Properties;
import javax.swing.JFrame;
import javax.swing.UIManager;
import resources.Resources;
import com.cronopista.lightpacker.IDE.ui.MainFrame;
import com.cronopista.utils.Utils;

/**
 * @author Eduardo Rodr�guez
 * 
 */
public class MainIDE {

    private static MainIDE mainIDE;

    private String basePath;

    private UserPreferences userPreferences;

    private MainFrame mainFrame;

    private Properties language;

    public MainIDE() {
        mainIDE = this;
        basePath = Utils.getApplicationPath();
        userPreferences = new UserPreferences();
        language = new Properties();
        try {
            language.load(Resources.class.getResourceAsStream("language_" + userPreferences.getLanguage() + ".properties"));
        } catch (Exception e) {
        }
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
        }
        mainFrame = new MainFrame();
    }

    public String getBasePath() {
        return basePath;
    }

    public UserPreferences getUserPreferences() {
        return userPreferences;
    }

    public Properties getLanguage() {
        return language;
    }

    public static String translate(String key) {
        return getInstance().getLanguage().getProperty(key);
    }

    public static MainIDE getInstance() {
        if (mainIDE == null) mainIDE = new MainIDE();
        return mainIDE;
    }

    public void exit() {
        userPreferences.setMaximized(mainFrame.getExtendedState() == JFrame.MAXIMIZED_BOTH);
        if (!userPreferences.isMaximized()) {
            userPreferences.setWidth(mainFrame.getWidth());
            userPreferences.setHeight(mainFrame.getHeight());
        }
        userPreferences.save();
        System.exit(0);
    }

    public static void main(String[] args) {
        new MainIDE();
    }
}
