package jfb.examples.gmf.math.diagram.preferences;

import jfb.examples.gmf.math.diagram.part.MathDiagramEditorPlugin;
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
        DiagramGeneralPreferencePage.initDefaults(store);
        DiagramAppearancePreferencePage.initDefaults(store);
        DiagramConnectionsPreferencePage.initDefaults(store);
        DiagramPrintingPreferencePage.initDefaults(store);
        DiagramRulersAndGridPreferencePage.initDefaults(store);
    }

    /**
	 * @generated
	 */
    protected IPreferenceStore getPreferenceStore() {
        return MathDiagramEditorPlugin.getInstance().getPreferenceStore();
    }
}
