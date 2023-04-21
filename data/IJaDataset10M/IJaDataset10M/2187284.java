package org.eclipse.remus.application;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * @author Tom Seidel <tom.seidel@remus-software.org>
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

    private final IPreferenceStore preferenceStore;

    public static final String FIRST_START = "FIRST_START";

    /**
	 * 
	 */
    public PreferenceInitializer() {
        preferenceStore = ApplicationPlugin.getDefault().getPreferenceStore();
    }

    @Override
    public void initializeDefaultPreferences() {
        preferenceStore.setDefault(FIRST_START, true);
    }
}
