package com.aptana.ide.scripting.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import com.aptana.ide.scripting.ScriptingEngine;
import com.aptana.ide.scripting.ScriptingPlugin;

/**
 * @since 3.1
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

    /**
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
    public void initializeDefaultPreferences() {
        IPreferenceStore store = ScriptingPlugin.getDefault().getPreferenceStore();
        store.setDefault(IPreferenceConstants.SCRIPTING_SERVER_START_PORT, ScriptingEngine.SCRIPTING_SERVER_START_PORT);
    }
}
