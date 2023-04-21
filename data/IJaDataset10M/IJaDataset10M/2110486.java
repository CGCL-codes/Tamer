package net.sf.pyconsole.preferences;

import net.sf.pyconsole.Activator;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

    @Override
    public void initializeDefaultPreferences() {
        IPreferenceStore store = Activator.getDefault().getPreferenceStore();
        store.setDefault(PreferenceConstants.P_INTERPRETER_PATH, "");
        store.setDefault(PreferenceConstants.P_COMMANDHISTORY_SIZE, 100);
        FontData data = new FontData();
        data.setHeight(12);
        data.setName("Courier New");
        data.setStyle(SWT.NORMAL);
        PreferenceConverter.setDefault(store, PreferenceConstants.P_CONSOLE_FONT, data);
        RGB color = new RGB(128, 0, 0);
        PreferenceConverter.setDefault(store, PreferenceConstants.P_CONSOLE_ERR_COLOR, color);
        color = new RGB(0, 0, 255);
        PreferenceConverter.setDefault(store, PreferenceConstants.P_CONSOLE_KW_COLOR, color);
    }
}
