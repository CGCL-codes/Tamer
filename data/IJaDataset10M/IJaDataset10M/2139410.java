package net.sf.orcc.ui.editor;

import net.sf.graphiti.model.DefaultRefinementPolicy;
import net.sf.graphiti.model.Vertex;
import net.sf.orcc.util.OrccUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * This class extends the default refinement policy with XDF-specific policy.
 * 
 * @author Matthieu Wipliez
 * 
 */
public class NetworkRefinementPolicy extends DefaultRefinementPolicy {

    @Override
    public String getNewRefinement(Vertex vertex) {
        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
        Shell shell = window.getShell();
        final String message = "The selected instance can be refined by an existing " + "actor or network.";
        MessageDialog dialog = new MessageDialog(shell, "Set/Update Refinement", null, message, MessageDialog.QUESTION, new String[] { "Select actor", "Select network" }, 0);
        int index = dialog.open();
        String newRefinement = null;
        if (index == 0) {
            newRefinement = selectActor(vertex, shell);
        } else if (index == 1) {
            newRefinement = selectNetwork(vertex, shell);
        }
        return newRefinement;
    }

    /**
	 * Returns the project to which the vertex belongs.
	 * 
	 * @param vertex
	 *            a vertex
	 * @return a project
	 */
    private IProject getProject(Vertex vertex) {
        return vertex.getParent().getFile().getProject();
    }

    @Override
    public IFile getRefinementFile(Vertex vertex) {
        String refinement = getRefinement(vertex);
        if (refinement == null) {
            return null;
        }
        IProject project = getProject(vertex);
        String qualifiedName = refinement.replace('.', '/');
        IFile file = OrccUtil.getFile(project, qualifiedName, "xdf");
        if (file != null) {
            return file;
        }
        file = OrccUtil.getFile(project, qualifiedName, "cal");
        if (file != null) {
            return file;
        }
        return null;
    }

    /**
	 * Selects the qualified identifier of an actor.
	 * 
	 * @param vertex
	 *            a vertex
	 * @param shell
	 *            shell
	 * @return the qualified identifier of an actor
	 */
    private String selectActor(Vertex vertex, Shell shell) {
        IProject project = getProject(vertex);
        FilteredRefinementDialog dialog = new FilteredRefinementDialog(project, shell, "cal");
        dialog.setTitle("Select actor");
        dialog.setMessage("&Select existing actor:");
        String refinement = getRefinement(vertex);
        if (refinement != null) {
            dialog.setInitialPattern(refinement);
        }
        int result = dialog.open();
        if (result == Window.OK) {
            return (String) dialog.getFirstResult();
        }
        return null;
    }

    /**
	 * Selects the qualified identifier of a network.
	 * 
	 * @param vertex
	 *            a vertex
	 * @param shell
	 *            shell
	 * @return the qualified identifier of a network
	 */
    private String selectNetwork(Vertex vertex, Shell shell) {
        IProject project = getProject(vertex);
        FilteredRefinementDialog dialog = new FilteredRefinementDialog(project, shell, "xdf");
        dialog.setTitle("Select network");
        dialog.setMessage("&Select existing network:");
        String refinement = getRefinement(vertex);
        if (refinement != null) {
            dialog.setInitialPattern(refinement);
        }
        int result = dialog.open();
        if (result == Window.OK) {
            return (String) dialog.getFirstResult();
        }
        return null;
    }
}
