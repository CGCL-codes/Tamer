package com.aptana.ide.debug.internal.core.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import com.aptana.ide.debug.core.JSDebugPlugin;
import com.aptana.ide.debug.core.preferences.IJSDebugPreferenceNames;

/**
 * @author Max Stepanov
 */
public class JSDebugPreferenceInitializer extends AbstractPreferenceInitializer {

    /**
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
    public void initializeDefaultPreferences() {
        IEclipsePreferences node = new DefaultScope().getNode(JSDebugPlugin.getDefault().getBundle().getSymbolicName());
        node.putBoolean(IJSDebugPreferenceNames.SUSPEND_ON_FIRST_LINE, false);
        node.putBoolean(IJSDebugPreferenceNames.SUSPEND_ON_ERRORS, true);
        node.putBoolean(IJSDebugPreferenceNames.SUSPEND_ON_EXCEPTIONS, false);
        node.putBoolean(IJSDebugPreferenceNames.SUSPEND_ON_DEBUGGER_KEYWORD, true);
    }
}
