package net.openchrom.chromatogram.msd.filter.supplier.denoising.ui.preferences;

import net.openchrom.chromatogram.msd.filter.supplier.denoising.preferences.FilterPreferences;
import net.openchrom.chromatogram.msd.filter.supplier.denoising.settings.SegmentWidth;
import net.openchrom.chromatogram.msd.filter.supplier.denoising.ui.Activator;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;

public class FilterPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public FilterPreferencePage() {
        super(GRID);
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setDescription("Set the denoising filter settings.");
    }

    /**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
    public void createFieldEditors() {
        addField(new IonListEditor(FilterPreferences.P_IONS_TO_REMOVE, "Ions to remove in all cases", getFieldEditorParent()));
        addField(new IonListEditor(FilterPreferences.P_IONS_TO_PRESERVE, "Ions to preserve in all cases", getFieldEditorParent()));
        addField(new BooleanFieldEditor(FilterPreferences.P_USE_CHROMATOGRAM_SPECIFIC_IONS, "Use chromatogram specific ions.", getFieldEditorParent()));
        addField(new BooleanFieldEditor(FilterPreferences.P_ADJUST_THRESHOLD_TRANSITIONS, "Adjust threshold transitions.", getFieldEditorParent()));
        addField(new ComboFieldEditor(FilterPreferences.P_SEGMENT_WIDTH, "Segment width to determine noise.", SegmentWidth.getElements(), getFieldEditorParent()));
    }

    public void init(IWorkbench workbench) {
    }
}
