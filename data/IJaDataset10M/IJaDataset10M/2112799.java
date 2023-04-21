package org.objectstyle.wolips.pbserver.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.objectstyle.wolips.preferences.PreferencesPlugin;

/**
 * @author mike
 */
public class PBServerPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public PBServerPreferencePage() {
        super(GRID);
        setMessage("Changes will not be reflected until after a restart.");
    }

    protected IPreferenceStore doGetPreferenceStore() {
        return PreferencesPlugin.getDefault().getPreferenceStore();
    }

    protected void createFieldEditors() {
        addField(new BooleanFieldEditor(PreferenceConstants.PBSERVER_ENABLED, "ProjectBuilder Server Enabled", getFieldEditorParent()));
        addField(new IntegerFieldEditor(PreferenceConstants.PBSERVER_PORT, "ProjectBuilder Server Port", getFieldEditorParent(), 5));
    }

    public void init(IWorkbench _workbench) {
    }
}
