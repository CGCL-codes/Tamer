package org.flexharmony.eclipse.nature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import java.util.List;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.actions.ActionDelegate;
import org.flexharmony.eclipse.HarmonyPlugin;
import org.flexharmony.eclipse.nature.wizard.AddProjectNatureWizard;

public class AddProjectNatureAction extends ActionDelegate implements IWorkbenchWindowActionDelegate {

    private IProject project;

    public AddProjectNatureAction() {
    }

    public void init(IWorkbenchWindow window) {
    }

    public void selectionChanged(IAction action, ISelection selection) {
        IStructuredSelection structuredSelection = (IStructuredSelection) selection;
        Object element = structuredSelection.getFirstElement();
        project = (IProject) element;
    }

    public void run(IAction action) {
        Shell parent = HarmonyPlugin.getInstance().getWorkbench().getDisplay().getActiveShell();
        AddProjectNatureWizard wizard = new AddProjectNatureWizard(project);
        wizard.init(HarmonyPlugin.getInstance().getWorkbench(), null);
        WizardDialog dialog = new WizardDialog(parent, wizard);
        dialog.setTitle("Add FlexHarmony Nature");
        dialog.create();
        dialog.open();
        if (wizard.addProjectNature()) {
            try {
                IProjectDescription projectDescription = project.getDescription();
                List natureIds = new ArrayList();
                natureIds.addAll(Arrays.asList(projectDescription.getNatureIds()));
                natureIds.add(HarmonyProjectNature.NATURE_ID);
                projectDescription.setNatureIds((String[]) natureIds.toArray(new String[natureIds.size()]));
                project.setDescription(projectDescription, null);
            } catch (CoreException cexc) {
                cexc.printStackTrace();
            } catch (Throwable exc) {
                exc.printStackTrace();
            }
        }
    }
}
