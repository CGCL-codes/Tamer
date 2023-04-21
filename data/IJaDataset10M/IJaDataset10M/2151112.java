package net.tourbook.device.garmin;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

    public PreferenceInitializer() {
    }

    @Override
    public void initializeDefaultPreferences() {
        final IPreferenceStore store = Activator.getDefault().getPreferenceStore();
        store.setDefault(IPreferences.IS_IMPORT_INTO_DESCRIPTION_FIELD, true);
        store.setDefault(IPreferences.IS_IMPORT_INTO_TITLE_FIELD, false);
        store.setDefault(IPreferences.IS_TITLE_IMPORT_ALL, false);
        store.setDefault(IPreferences.NUMBER_OF_TITLE_CHARACTERS, 100);
    }
}
