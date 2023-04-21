package com.aptana.ide.rcp.main;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.ui.IStartup;
import com.aptana.ide.rcp.main.preferences.IPreferenceConstants;

/**
 * @author Ingo Muschenetz
 */
public class InitialStartup implements IStartup {

    /**
	 * earlyStartup
	 */
    public void earlyStartup() {
        Preferences prefs = MainPlugin.getDefault().getPluginPreferences();
        boolean hasRunFirstStartup = prefs.getBoolean(IPreferenceConstants.PREF_KEY_FIRST_STARTUP);
        if (!hasRunFirstStartup) {
            initForFirstTimeStartup();
            prefs.setValue(IPreferenceConstants.PREF_KEY_FIRST_STARTUP, true);
            MainPlugin.getDefault().savePluginPreferences();
        }
    }

    /**
	 * Runs the first time startup logic for this workspace.
	 */
    private void initForFirstTimeStartup() {
    }
}
