package net.sf.beatrix.ui.xmleditor.preferences;

import net.sf.beatrix.internal.ui.xmleditor.Activator;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class XMLEditorPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public XMLEditorPreferencePage() {
        super(GRID);
        setDescription("Beatrix Detector Configuration Editor (XML)");
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
    }

    @Override
    protected void createFieldEditors() {
        addField(new ColorFieldEditor(PreferenceValues.XMLEDITOR_COLOR_PROCESSING_INSTR, "Processing instructions:", getFieldEditorParent()));
        addField(new ColorFieldEditor(PreferenceValues.XMLEDITOR_COLOR_COMMENT, "Comments:", getFieldEditorParent()));
        addField(new ColorFieldEditor(PreferenceValues.XMLEDITOR_COLOR_TAG, "Tags:", getFieldEditorParent()));
        addField(new ColorFieldEditor(PreferenceValues.XMLEDITOR_COLOR_VALUES, "Tag attributes values:", getFieldEditorParent()));
        addField(new ColorFieldEditor(PreferenceValues.XMLEDITOR_COLOR_CONTENT, "General Content:", getFieldEditorParent()));
    }

    public void init(IWorkbench workbench) {
    }
}
