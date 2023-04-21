package com.mousefeed.eclipse.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import com.mousefeed.client.OnWrongInvocationMode;
import com.mousefeed.eclipse.Activator;

/**
 * Initializes default preference values.
 *
 * @author Andriy Palamarchuk
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

    /** {@inheritDoc} */
    @Override
    public void initializeDefaultPreferences() {
        final IPreferenceStore store = Activator.getDefault().getPreferenceStore();
        store.setDefault(PreferenceConstants.P_DEFAULT_ON_WRONG_INVOCATION_MODE, OnWrongInvocationMode.DEFAULT.name());
        store.setDefault(PreferenceConstants.P_INVOCATION_CONTROL_ENABLED, PreferenceConstants.INVOCATION_CONTROL_ENABLED_DEFAULT);
    }
}
