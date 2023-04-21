package org.xaware.schemanavigator.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.xaware.schemanavigator.SchemaNavigatorPlugin;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

    @Override
    public void initializeDefaultPreferences() {
        final IPreferenceStore store = SchemaNavigatorPlugin.getDefault().getPreferenceStore();
        store.setDefault(PreferenceConstants.P_CATEGORY, true);
    }
}
