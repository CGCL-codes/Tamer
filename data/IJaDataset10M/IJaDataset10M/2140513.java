package de.uniwue.dltk.textmarker.internal.core;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.dltk.compiler.task.TaskTagUtils;
import de.uniwue.dltk.textmarker.core.TextMarkerCorePreferences;
import de.uniwue.dltk.textmarker.core.TextMarkerPlugin;

public class TextMarkerPreferenceInitializer extends AbstractPreferenceInitializer {

    public TextMarkerPreferenceInitializer() {
    }

    @Override
    public void initializeDefaultPreferences() {
        Preferences store = TextMarkerPlugin.getDefault().getPluginPreferences();
        TaskTagUtils.initializeDefaultValues(store);
        store.setDefault(TextMarkerCorePreferences.BUILDER_IMPORT_BY_NAME, false);
        store.setDefault(TextMarkerCorePreferences.BUILDER_RESOLVE_IMPORTS, false);
        store.setDefault(TextMarkerCorePreferences.BUILDER_IGNORE_DUPLICATE_SHORTNAMES, false);
    }
}
