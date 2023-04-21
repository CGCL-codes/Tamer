package org.jtestcase.plugin.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.jtestcase.plugin.JTestCasePlugin;

public class MorePreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public static final String P_TEST = "test";

    public MorePreferencePage() {
        super(GRID);
        setPreferenceStore(JTestCasePlugin.getDefault().getPreferenceStore());
        setDescription("ToString plug-in settings page advanced");
        initializeDefaults();
    }

    /**
	 * Sets the default values of the preferences.
	 */
    private void initializeDefaults() {
        IPreferenceStore store = getPreferenceStore();
    }

    protected void createFieldEditors() {
        createLabel(getFieldEditorParent(), "No yet implemented");
        addField(new BooleanFieldEditor(P_TEST, "No yet implemented", getFieldEditorParent()));
    }

    public void init(IWorkbench workbench) {
    }

    /**
	 * Utility method that creates a label instance
	 * and sets the default layout data.
	 *
	 * @param parent  the parent for the new label
	 * @param text  the text for the new label
	 * @return the new label
	 */
    private Label createLabel(Composite parent, String text) {
        Label label = new Label(parent, SWT.LEFT);
        label.setText(text);
        GridData data = new GridData();
        data.horizontalSpan = 2;
        data.horizontalAlignment = GridData.FILL;
        label.setLayoutData(data);
        return label;
    }
}
