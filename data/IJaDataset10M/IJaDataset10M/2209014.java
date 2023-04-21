package com.celtrum.xs.preferences;

import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import com.celtrum.xs.Activator;

public class ColorsPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public ColorsPage() {
        super(GRID);
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setDescription("Syntax Highlighting Preferences");
    }

    @Override
    protected void createFieldEditors() {
        addColorField(IPreferences.XS_COLOR_DEFAULT, "Normal Text");
        addColorField(IPreferences.XS_COLOR_COMMENT, "Comments");
        addColorField(IPreferences.XS_COLOR_STRING, "Strings");
        addColorField(IPreferences.XS_COLOR_CONSTANT, "Constants");
        addColorField(IPreferences.XS_COLOR_FUNCTION, "Functions");
    }

    protected void addColorField(String preferenceID, String name) {
        ColorFieldEditor editor = new ColorFieldEditor(preferenceID, name, this.getFieldEditorParent());
        this.addField(editor);
    }

    @Override
    public void init(IWorkbench workbench) {
    }
}
