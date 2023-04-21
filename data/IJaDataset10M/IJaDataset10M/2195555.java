package com.ibm.celldt.managedbuilder.xl.ui.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import com.ibm.celldt.managedbuilder.xl.core.preferences.XlToolsProperties;
import com.ibm.celldt.managedbuilder.xl.ui.XlManagedBuilderUIPlugin;

/**
 * @author laggarcia
 * @since 3.0.0
 */
public class XlToolsPreferencesInitializer extends AbstractPreferenceInitializer {

    /**
	 * 
	 */
    public XlToolsPreferencesInitializer() {
    }

    public void initializeDefaultPreferences() {
        IPreferenceStore store = XlManagedBuilderUIPlugin.getDefault().getPreferenceStore();
        store.setDefault(XlToolsProperties.xlToolsPath, XlToolsProperties.xlToolsPathDefaultValue);
    }
}
