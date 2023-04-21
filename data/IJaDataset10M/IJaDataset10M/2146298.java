package org.t2framework.vili.eclipse.properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.dialogs.PropertyPage;
import org.t2framework.vili.eclipse.Activator;
import org.t2framework.vili.eclipse.ui.ViliProjectPreferencesControl;
import org.t2framework.vili.ViliProjectPreferences;

public class ViliProjectPreferencesPropertyPage extends PropertyPage {

    private ViliProjectPreferences preferences;

    private ViliProjectPreferencesControl control;

    /**
     * @see PreferencePage#createContents(Composite)
     */
    protected Control createContents(Composite parent) {
        try {
            preferences = Activator.getDefault().getViliProjectPreferences(getProject());
            control = new ViliProjectPreferencesControl(parent, preferences, true, true, true) {

                @Override
                public void setErrorMessage(String message) {
                    ViliProjectPreferencesPropertyPage.this.setErrorMessage(message);
                }
            };
        } catch (CoreException ex) {
            throw new RuntimeException(ex);
        }
        Control ctl = control.createControl();
        control.resumeValues();
        control.setVisible(true);
        return ctl;
    }

    protected void performDefaults() {
        control.setDefaultValues();
    }

    public boolean performOk() {
        try {
            preferences.save(getProject());
        } catch (CoreException ex) {
            Activator.getDefault().log("Can't save ViliProjectPreferences", ex);
            return false;
        }
        return true;
    }

    public IProject getProject() throws CoreException {
        IAdaptable element = getElement();
        if (element instanceof IJavaProject) {
            return ((IJavaProject) element).getProject();
        } else if (element instanceof IResource) {
            return ((IResource) element).getProject();
        } else {
            throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, 0, "Selected element is not a project", null));
        }
    }
}
