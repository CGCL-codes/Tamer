package org.lcx.scrumvision.ui.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.lcx.scrumvision.core.IScrumVisionConstants;
import org.lcx.scrumvision.core.ScrumVisionCorePlugin;

/**
 * @author Laurent Carbonnaux
 */
public class SVPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

    public static final String ID = "org.lcx.scrumvision.ui.preferences";

    private Button updateAllTask;

    public SVPreferencePage() {
        super();
        setPreferenceStore(ScrumVisionCorePlugin.getDefault().getPreferenceStore());
    }

    @Override
    protected Control createContents(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout(1, false);
        container.setLayout(layout);
        Label label = new Label(container, SWT.NULL);
        label.setText("Set the preferences for \"Scrum Vision\" below.");
        updateAllTask = new Button(container, SWT.CHECK);
        updateAllTask.setText("Update all tasks remaining hours when update of one task (Instead only remaining hours of the selected task will be updated)");
        updateAllTask.setSelection(getPreferenceStore().getBoolean(IScrumVisionConstants.PREF_UPDATE_ALL_TASK));
        return container;
    }

    public void init(IWorkbench workbench) {
    }

    @Override
    public boolean performOk() {
        getPreferenceStore().setValue(IScrumVisionConstants.PREF_UPDATE_ALL_TASK, updateAllTask.getSelection());
        return true;
    }

    @Override
    public boolean performCancel() {
        updateAllTask.setSelection(getPreferenceStore().getBoolean(IScrumVisionConstants.PREF_UPDATE_ALL_TASK));
        return true;
    }

    @Override
    public void performDefaults() {
        super.performDefaults();
        updateAllTask.setSelection(getPreferenceStore().getDefaultBoolean(IScrumVisionConstants.PREF_UPDATE_ALL_TASK));
    }
}
