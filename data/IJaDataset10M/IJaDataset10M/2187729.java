package net.sf.beatrix.ide.preferences;

import net.sf.beatrix.internal.ui.Activator;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class NewWizardPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public NewWizardPreferencePage() {
        super(GRID);
        setDescription("New Beatrix Detector Configuration Wizard");
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
    }

    @Override
    protected void createFieldEditors() {
        addField(new BooleanFieldEditor(PreferenceValues.NEWDETECTORWIZARD_OVERWRITE, "Overwrite existing files when creating new configurations", getFieldEditorParent()));
    }

    public void init(IWorkbench workbench) {
    }
}
