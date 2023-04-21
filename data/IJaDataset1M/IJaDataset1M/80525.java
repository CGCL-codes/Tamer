package se.mdh.mrtc.saveccm.assembly.diagram.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import se.mdh.mrtc.saveccm.assembly.diagram.part.SaveccmDiagramEditorPlugin;

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
        return SaveccmDiagramEditorPlugin.getInstance().getPreferenceStore();
    }
}
