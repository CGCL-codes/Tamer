package de.walware.statet.r.internal.core;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import de.walware.eclipsecommons.preferences.Preference;
import de.walware.eclipsecommons.preferences.PreferencesUtil;
import de.walware.statet.r.core.RCodeStyleSettings;

/**
 * Preference Initializer for the preferences of 'StatET R Core' plug-in
 */
public class RCorePreferenceInitializer extends AbstractPreferenceInitializer {

    @Override
    public void initializeDefaultPreferences() {
        final DefaultScope defaultScope = new DefaultScope();
        final Map<Preference, Object> defaults = new HashMap<Preference, Object>();
        new RCodeStyleSettings().deliverToPreferencesMap(defaults);
        for (final Preference<Object> unit : defaults.keySet()) {
            PreferencesUtil.setPrefValue(defaultScope, unit, defaults.get(unit));
        }
    }
}
