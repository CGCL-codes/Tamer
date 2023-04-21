package com.msli.mapp3d.client.rcp.wizard;

import com.msli.core.exception.NotYetImplementedException;
import com.msli.mapp3d.client.rcp.nature.CatalogNature;
import com.msli.rcp.util.RcpIdentifiable;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

/**
 * Wizard for creating a new "catalog" project.
 * @author jonb
 */
public class NewCatalogWizard extends BasicNewProjectResourceWizard implements RcpIdentifiable {

    @Override
    public boolean performFinish() {
        boolean success = super.performFinish();
        if (!success) return success;
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IProject project = getNewProject();
        try {
            IProjectDescription description = project.getDescription();
            String[] natures = description.getNatureIds();
            String[] newNatures = new String[natures.length + 1];
            System.arraycopy(natures, 0, newNatures, 0, natures.length);
            newNatures[natures.length] = CatalogNature.RCP_ID;
            IStatus status = workspace.validateNatureSet(natures);
            if (status.getCode() == IStatus.OK) {
                description.setNatureIds(newNatures);
                project.setDescription(description, null);
            } else {
                NotYetImplementedException.doThrow(this);
            }
        } catch (CoreException e) {
            NotYetImplementedException.doThrow(this);
        }
        return true;
    }

    @Override
    public String getRcpId() {
        return RCP_ID;
    }

    public static final String RCP_ID = "com.msli.mapp3d.client.NEW_CATALOG_WIZARD";
}
