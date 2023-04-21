package org.echarts.edt.ui;

import org.echarts.edt.ui.wizards.EChartsBuildPathWizard;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class EChartsBuildPath implements IObjectActionDelegate {

    private ISelection selection;

    private Shell parent;

    public EChartsBuildPath() {
    }

    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        parent = targetPart.getSite().getShell();
    }

    public void run(IAction action) {
        IProject project = (IProject) ((IStructuredSelection) selection).getFirstElement();
        try {
            EChartsBuildPathWizard bpw = new EChartsBuildPathWizard(project);
            WizardDialog dialog = new WizardDialog(parent, bpw);
            dialog.open();
        } catch (CoreException e) {
            ErrorDialog.openError(parent, null, null, e.getStatus());
        }
    }

    public void selectionChanged(IAction action, ISelection selection) {
        this.selection = selection;
    }
}
