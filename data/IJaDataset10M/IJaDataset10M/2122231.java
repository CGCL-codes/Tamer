package org.j2eespider.templateeditor.velocityeditor.preferences;

import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.j2eespider.templateeditor.Plugin;
import org.j2eespider.templateeditor.velocityeditor.editors.ColorManager;
import org.j2eespider.templateeditor.velocityeditor.util.UIUtil;

/**
 * This class is based on project VelocityWebEdit with author permission.
 * http://velocitywebedit.sourceforge.net
 * 
 * @author joe.hudson, bruno.braga
 */
public class MainPreferences extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public MainPreferences() {
        super(GRID);
        setPreferenceStore(Plugin.getDefault().getPreferenceStore());
        initializeDefaults();
    }

    public void init(IWorkbench workbench) {
    }

    /**
	 * Initialize the default values
	 */
    private void initializeDefaults() {
    }

    /**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
    public void createFieldEditors() {
        addField(new ColorFieldEditor(ColorManager.COLOR_FOREACH_DIRECTIVE, UIUtil.getResourceLabel("ForeachDirective"), getFieldEditorParent()));
        addField(new ColorFieldEditor(ColorManager.COLOR_IF_DIRECTIVE, UIUtil.getResourceLabel("IfDirective"), getFieldEditorParent()));
        addField(new ColorFieldEditor(ColorManager.COLOR_MACRO_DIRECTIVE, UIUtil.getResourceLabel("MacroDirective"), getFieldEditorParent()));
        addField(new ColorFieldEditor(ColorManager.COLOR_SET_DIRECTIVE, UIUtil.getResourceLabel("SetDirective"), getFieldEditorParent()));
        addField(new ColorFieldEditor(ColorManager.COLOR_VARIABLE, UIUtil.getResourceLabel("VelocityVariable"), getFieldEditorParent()));
        addField(new ColorFieldEditor(ColorManager.COLOR_COMMENT, UIUtil.getResourceLabel("VelocityComment"), getFieldEditorParent()));
        addField(new ColorFieldEditor(ColorManager.COLOR_PROC_INSTR, UIUtil.getResourceLabel("XMLInstruction"), getFieldEditorParent()));
        addField(new ColorFieldEditor(ColorManager.COLOR_STRING, UIUtil.getResourceLabel("XMLString"), getFieldEditorParent()));
        addField(new ColorFieldEditor(ColorManager.COLOR_TAG, UIUtil.getResourceLabel("XMLTag"), getFieldEditorParent()));
        addField(new ColorFieldEditor(ColorManager.COLOR_XML_COMMENT, UIUtil.getResourceLabel("XMLComment"), getFieldEditorParent()));
    }
}
