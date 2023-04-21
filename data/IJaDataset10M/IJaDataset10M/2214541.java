package net.sf.flophase.app.pref;

import com.google.inject.Inject;

/**
 * This class allows for retrieval and storage of application-specific settings.
 */
public class FloSettings implements Settings {

    /**
     * The preference manager where the settings are stored.
     */
    private final PreferenceManager preferences;

    /**
     * Creates a new FloSettings instance.
     * 
     * @param preferences The preference manager.
     */
    @Inject
    public FloSettings(PreferenceManager preferences) {
        this.preferences = preferences;
    }

    @Override
    public void addArguments(String[] args) {
        if (args.length > 0) {
            if (args.length >= 2) {
                for (int i = 0; i < args.length - 1; i += 2) {
                    String flag = args[i];
                    String value = args[i + 1];
                    preferences.put(flag, value);
                }
            }
            if (args.length % 2 == 1) {
                preferences.put(PreferenceConstants.OPEN_FILE, args[args.length - 1]);
            }
        }
    }

    @Override
    public String getOpenFile() {
        return preferences.get(PreferenceConstants.OPEN_FILE, null);
    }

    @Override
    public int getWindowHeight() {
        return preferences.getInt(PreferenceConstants.WINDOW_HEIGHT, 600);
    }

    @Override
    public int getWindowWidth() {
        return preferences.getInt(PreferenceConstants.WINDOW_WIDTH, 500);
    }

    @Override
    public void persist() {
        preferences.persist();
    }

    @Override
    public void setOpenFile(String openFile) {
        if (openFile == null) {
            preferences.remove(PreferenceConstants.OPEN_FILE);
        } else {
            preferences.put(PreferenceConstants.OPEN_FILE, openFile);
        }
    }

    @Override
    public void setShowHistoric(boolean show) {
        preferences.put(PreferenceConstants.VIEW_HISTORIC, show);
    }

    @Override
    public void setShowLongTerm(boolean show) {
        preferences.put(PreferenceConstants.VIEW_LONG_TERM, show);
    }

    @Override
    public void setShowRecent(boolean show) {
        preferences.put(PreferenceConstants.VIEW_RECENT, show);
    }

    @Override
    public void setShowUpcoming(boolean show) {
        preferences.put(PreferenceConstants.VIEW_UPCOMING, show);
    }

    @Override
    public void setSystemAccounts(boolean show) {
        preferences.put(PreferenceConstants.VIEW_SYSTEM_ACCOUNTS, show);
    }

    @Override
    public void setWindowHeight(int windowHeight) {
        preferences.put(PreferenceConstants.WINDOW_HEIGHT, windowHeight);
    }

    @Override
    public void setWindowWidth(int windowWidth) {
        preferences.put(PreferenceConstants.WINDOW_WIDTH, windowWidth);
    }

    @Override
    public boolean showHistoric() {
        return preferences.getBoolean(PreferenceConstants.VIEW_HISTORIC, false);
    }

    @Override
    public boolean showLongTerm() {
        return preferences.getBoolean(PreferenceConstants.VIEW_LONG_TERM, false);
    }

    @Override
    public boolean showRecent() {
        return preferences.getBoolean(PreferenceConstants.VIEW_RECENT, true);
    }

    @Override
    public boolean showSystemAccounts() {
        return preferences.getBoolean(PreferenceConstants.VIEW_SYSTEM_ACCOUNTS, true);
    }

    @Override
    public boolean showUpcoming() {
        return preferences.getBoolean(PreferenceConstants.VIEW_UPCOMING, true);
    }
}
