package org.exist.eclipse.internal.preferences;

import org.exist.eclipse.internal.BasePlugin;

/**
 * This class implements the PreferencePage for the standard-options of the
 * eXist Plugin.
 */
public class DefaultExistPluginPreferencePage extends ExistPluginPreferencePage {

    public DefaultExistPluginPreferencePage() {
        super();
        setPreferenceStore(BasePlugin.getDefault().getPreferenceStore());
    }
}
