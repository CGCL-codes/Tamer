package net.openchrom.support.ui.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import net.openchrom.support.preferences.SupportPreferences;
import net.openchrom.support.ui.Activator;

public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public PreferencePage() {
        super(GRID);
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setDescription("General settings");
    }

    public void createFieldEditors() {
        addField(new BooleanFieldEditor(SupportPreferences.P_CHROMATOGRAM_OPERATION_IS_UNDOABLE, "Chromatogram operations are undoable.", getFieldEditorParent()));
    }

    public void init(IWorkbench workbench) {
    }
}
