package de.hu_berlin.sam.mmunit.diagram.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * @generated
 */
public class DiagramPreferenceInitializer extends AbstractPreferenceInitializer {

    /**
	 * @generated
	 */
    public void initializeDefaultPreferences() {
        IPreferenceStore store = getPreferenceStore();
        de.hu_berlin.sam.mmunit.diagram.preferences.DiagramGeneralPreferencePage.initDefaults(store);
        de.hu_berlin.sam.mmunit.diagram.preferences.DiagramAppearancePreferencePage.initDefaults(store);
        de.hu_berlin.sam.mmunit.diagram.preferences.DiagramConnectionsPreferencePage.initDefaults(store);
        de.hu_berlin.sam.mmunit.diagram.preferences.DiagramPrintingPreferencePage.initDefaults(store);
        de.hu_berlin.sam.mmunit.diagram.preferences.DiagramRulersAndGridPreferencePage.initDefaults(store);
    }

    /**
	 * @generated
	 */
    protected IPreferenceStore getPreferenceStore() {
        return de.hu_berlin.sam.mmunit.diagram.part.MMUnitDiagramEditorPlugin.getInstance().getPreferenceStore();
    }
}
