package org.jlense.uiworks.preference;

import org.jlense.uiworks.dialogs.IDialogSettings;
import org.jlense.uiworks.forms.FieldEditorFormPage;
import org.jlense.uiworks.internal.WorkbenchPlugin;
import org.jlense.uiworks.workbench.IWorkbench;
import org.jlense.uiworks.workbench.IWorkbenchPreferencePage;

/**
 * General Preference page for UIWorks.
 * 
 * @author ted stockwell [emorning@sourceforge.net]
 */
public class GeneralPreferencePage extends FieldEditorFormPage implements IWorkbenchPreferencePage {

    private static final WorkbenchPlugin PLUGIN = WorkbenchPlugin.getDefault();

    public GeneralPreferencePage() {
        super(GRID);
    }

    protected IDialogSettings doGetDialogSettings() {
        return PLUGIN.getDefault().getPreferenceStore();
    }

    /**
     * Implementation of FieldEditorFormPage.createFieldEditors()
     */
    public void createFieldEditors() {
        addField(new OutlookStartViewFieldEditor());
        addField(new ToolBarButtonDisplayOptionsFieldEditor());
        addField(new HorizontalGridLinesFieldEditor());
        addField(new VerticalGridLinesFieldEditor());
    }

    public void init(IWorkbench workbench) {
    }
}
