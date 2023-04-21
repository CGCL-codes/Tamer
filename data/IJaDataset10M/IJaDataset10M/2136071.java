package net.openchrom.chromatogram.msd.baseline.detector.ui.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import net.openchrom.chromatogram.msd.baseline.detector.ui.Activator;

public class BaselineDetectorPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public BaselineDetectorPreferencePage() {
        super(GRID);
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setDescription("Baseline Detector");
    }

    /**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
    public void createFieldEditors() {
    }

    public void init(IWorkbench workbench) {
    }
}
