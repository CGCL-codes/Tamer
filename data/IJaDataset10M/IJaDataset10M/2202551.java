package net.sf.grudi.persistence.pages;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.ScaleFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class ConfigurationPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public ConfigurationPage() {
    }

    @Override
    public void init(IWorkbench workbench) {
    }

    @Override
    protected void createFieldEditors() {
        addField(new ScaleFieldEditor("TESTE", "Teste", getFieldEditorParent()));
    }
}
