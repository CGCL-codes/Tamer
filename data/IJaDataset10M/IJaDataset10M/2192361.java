package de.hackerdan.projectcreator.ui.internal.prefs;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import de.hackerdan.projectcreator.ui.internal.Activator;

/**
 * Initialisierung der Standardwerte.
 * 
 * @author Daniel Hirscher
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

    @Override
    public void initializeDefaultPreferences() {
        final IPreferenceStore store = Activator.getDefault().getPreferenceStore();
        store.setDefault(PreferenceConstants.CONFIGURATION_TYPE, PreferenceConstants.CONFIGURATION_TYPE_WORKSPACERELATIVE);
        store.setDefault(PreferenceConstants.CONFIGURATION_URL, "");
        store.setDefault(PreferenceConstants.CONFIGURATION_WORKSPACERELATIVE, "");
    }
}
