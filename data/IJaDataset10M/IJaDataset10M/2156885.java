package com.ctb.diagram.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import com.ctb.diagram.part.CtbDiagramEditorPlugin;

/**
 * @generated
 */
public class DiagramPreferenceInitializer extends AbstractPreferenceInitializer {

    /**
	 * @generated
	 */
    public void initializeDefaultPreferences() {
        IPreferenceStore store = getPreferenceStore();
        DiagramPrintingPreferencePage.initDefaults(store);
        DiagramGeneralPreferencePage.initDefaults(store);
        DiagramAppearancePreferencePage.initDefaults(store);
        DiagramConnectionsPreferencePage.initDefaults(store);
        DiagramRulersAndGridPreferencePage.initDefaults(store);
    }

    /**
	 * @generated
	 */
    protected IPreferenceStore getPreferenceStore() {
        return CtbDiagramEditorPlugin.getInstance().getPreferenceStore();
    }
}
