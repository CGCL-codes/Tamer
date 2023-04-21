package ca.uwaterloo.fydp;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.*;
import org.eclipse.jface.dialogs.*;
import org.eclipse.swt.widgets.Display;
import org.eclipse.team.core.RepositoryProvider;
import org.eclipse.core.resources.IProject;
import org.eclipse.ui.*;
import ca.uwaterloo.fydp.xcde.eclipse.XCDERegisterProvider;
import ca.uwaterloo.fydp.xcde.eclipse.XCDEServerSelectDialog;
import ca.uwaterloo.fydp.xcde.eclipse.XCDEAPI;

public class XCDELinkToRepository implements IObjectActionDelegate {

    private IStructuredSelection currentSelection;

    public void run(IAction action) {
        if (XCDERegisterProvider.instance.getCurrentRegister() == null) {
            new XCDEServerSelectDialog(Display.getCurrent().getActiveShell()).openDialog();
            if (XCDERegisterProvider.instance.getCurrentRegister() == null) {
                MessageDialog.openError(Display.getCurrent().getActiveShell(), "XCDE Link Error", "No XCDE server was selected, please select a server to link to.  Link aborted!");
                return;
            }
        } else {
            boolean confirmed = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(), "Confirm Destination XCDE Server", "Are you sure you want to link the selected projects to XCDE server " + XCDERegisterProvider.instance.getCurrentRegister().getServer().toString() + " ?");
            if (!confirmed) return;
        }
        Object[] projects = currentSelection.toArray();
        String[] projectList = new String[projects.length];
        for (int j = 0; j < projects.length; j++) {
            projectList[j] = projects[j].toString().substring(2);
            if (RepositoryProvider.getProvider((IProject) projects[j]) != null) {
                MessageDialog error = new MessageDialog(Display.getCurrent().getActiveShell(), "XCDE Error", null, "The project /" + projectList[j] + " already has a repository provider associated with it.", MessageDialog.ERROR, new String[] { "OK" }, 0);
                error.open();
                return;
            }
        }
        ProgressMonitorDialog monitorDialog = new ProgressMonitorDialog(Display.getCurrent().getActiveShell());
        XCDEProjectCrawler crawler = new XCDEProjectCrawler(projectList);
        try {
            monitorDialog.run(true, true, crawler);
            if (crawler.hadProjectExistsError()) {
                MessageDialog error = new MessageDialog(Display.getCurrent().getActiveShell(), "XCDE Error", null, "The project /" + crawler.getErrorSource() + " already exists on the XCDE server. Please rename the project" + " and try again.", MessageDialog.ERROR, new String[] { "OK" }, 0);
                error.open();
            } else {
                for (int j = 0; j < projects.length; j++) {
                    RepositoryProvider.map((IProject) projects[j], "ca.uwaterloo.fydp.XCDERepositoryProvider");
                }
                FydpPlugin.getDefault().fireLabelProviderChangedEvent();
            }
        } catch (Exception e) {
        }
    }

    public void selectionChanged(IAction action, ISelection selection) {
        currentSelection = (IStructuredSelection) selection;
    }

    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
    }
}
