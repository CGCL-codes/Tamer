package com.cjssolutions.plex.plugin.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import com.cjssolutions.plex.plugin.PlexPlugin;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

    @Override
    public void initializeDefaultPreferences() {
        IPreferenceStore store = PlexPlugin.getDefault().getPreferenceStore();
        store.setDefault(PrefConstants.P_RESOURCE_FOLDER, PrefConstants.P_RESOURCE_FOLDER_DEFAULT);
        store.setDefault(PrefConstants.P_PLEX_PROPERTY_FOLDER, "");
        store.setDefault(PrefConstants.P_BMP_FOLDER, "");
        store.setDefault(PrefConstants.P_SERVICE_PORT, PrefConstants.P_SERVICE_PORT_DEFAULT);
        store.setDefault(PrefConstants.P_SERVICE_HOST, PrefConstants.P_SERVICE_HOST_DEFAULT);
    }
}
