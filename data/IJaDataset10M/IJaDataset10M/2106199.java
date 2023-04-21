package com.rapidminer.gui.properties;

import java.util.Properties;
import com.rapidminer.tools.ParameterService;

/**
 * This change listener listens for settings changes, i.e. for changes
 * of the global program settings.
 * 
 * This change listener has been replaced by the ParameterChangeListener
 * @author Ingo Mierswa, Sebastian Land
 */
@Deprecated
public interface SettingsChangeListener {

    /** This method will be called after a settings change.
     * 
     * Attention! Please take into account that this method signature only remains
     * for compatibility reasons. Please don't use the given properties but the
     * {@link ParameterService} instead to see which settings have been changed.
     * 
     * The given properties (the system properties) will reflect the changes, too, but
     * only for ensuring compatibility and this might be removed soon.
     * */
    public void settingsChanged(Properties properties);
}
