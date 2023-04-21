package org.gello.client.actions.projectsView;

import java.util.ArrayList;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.gello.client.Application;
import org.gello.client.IImageKeys;
import org.gello.client.manager.Project;
import org.gello.client.manager.ServerException_Exception;
import org.gello.client.views.Projects;

public class DeleteProjectAction extends Action implements ISelectionListener, ActionFactory.IWorkbenchAction {

    public static final String ID = "org.gello.client.actions.projectsView.DeleteProject";

    private IStructuredSelection selection;

    private IWorkbenchWindow window;

    public DeleteProjectAction(IWorkbenchWindow window) {
        this.window = window;
        setId(ID);
        setText("&Delete");
        setToolTipText("Delete Project");
        setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Application.PLUGIN_ID, IImageKeys.DELETE_PROJECT));
        window.getSelectionService().addSelectionListener(this);
    }

    public void selectionChanged(IWorkbenchPart part, ISelection incoming) {
        if (part instanceof Projects) {
            if (incoming instanceof IStructuredSelection) {
                selection = (IStructuredSelection) incoming;
                if (selection.getFirstElement() != null && selection.getFirstElement() instanceof Project) setEnabled(true); else setEnabled(false);
            }
        }
    }

    /**
	 * Deletes the selected project by removing it from the server.
	 * If successful, it is removed from the project list.
	 * 
	 */
    @SuppressWarnings("unchecked")
    public void run() {
        Project project = (Project) selection.getFirstElement();
        MessageBox messageBox = new MessageBox(window.getShell(), SWT.YES | SWT.NO | SWT.ICON_QUESTION);
        messageBox.setMessage("Are you sure you would like to delete this project?");
        if (messageBox.open() == SWT.YES) {
            boolean success = false;
            try {
                success = Application.getManager().removeProject(project);
            } catch (ServerException_Exception e) {
                e.printStackTrace();
            }
            if (success) {
                ArrayList<Project> projectList = (ArrayList<Project>) Projects.viewer.getInput();
                projectList.remove(project);
                Projects.viewer.setSelection(null);
                Projects.viewer.refresh();
            }
        }
    }

    public void dispose() {
    }
}
