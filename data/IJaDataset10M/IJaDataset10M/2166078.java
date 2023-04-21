package org.hoydaa.cs4eclipse.ui.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.hoydaa.cs4eclipse.ui.Activator;

/**
 * 
 * @author Utku Utkan
 * 
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

    @Override
    public void initializeDefaultPreferences() {
        IPreferenceStore store = Activator.getDefault().getPreferenceStore();
        store.setDefault(PreferenceConstants.ALLOW_WHITESPACES, false);
    }
}
