package com.rise.rois.server;

import com.rise.rois.server.preferences.Preferences;
import com.rise.rois.server.preferences.ServerPreferences;

public class PreferenceManager {

    public Preferences getPreferences() {
        Preferences preferences = new Preferences();
        preferences.setLongWarning(ServerPreferences.getLongWarning());
        preferences.setShortWarning(ServerPreferences.getShortWarning());
        preferences.setBreakWarningDelay(ServerPreferences.getBreakWarningDelay());
        preferences.setBreakWarningMessage(ServerPreferences.getBreakWarningMessage());
        preferences.setBreakWarningActive(ServerPreferences.isBreakWarningActive());
        return preferences;
    }

    public void setPreferences(Preferences preferences) {
        if (preferences != null) {
            ServerPreferences.setLongWarning(preferences.getLongWarning());
            ServerPreferences.setShortWarning(preferences.getShortWarning());
            ServerPreferences.setBreakWarningActive(preferences.getBreakWarningActive());
            ServerPreferences.setBreakWarningDelay(preferences.getBreakWarningDelay());
            ServerPreferences.setBreakWarningMessage(preferences.getBreakWarningMessage());
            ServerPreferences.savePreferences();
        }
    }

    public void restoreDefaults() {
        ServerPreferences.restoreDefaults();
    }
}
