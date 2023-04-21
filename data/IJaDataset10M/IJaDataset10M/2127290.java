package net.openchrom.chromatogram.msd.baseline.detector.supplier.smoothed.ui.preferences;

import net.openchrom.chromatogram.msd.baseline.detector.supplier.smoothed.ui.Activator;
import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;

public class SmoothedBaselineDetectorPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public SmoothedBaselineDetectorPreferencePage() {
        super(GRID);
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setDescription("Smoothed baseline detector");
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
