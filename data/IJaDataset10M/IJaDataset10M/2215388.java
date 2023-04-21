package faneclipse.builder;

import java.util.Iterator;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author BEGAUDEAU stephane
 */
public class FanToggleNatureAction implements IObjectActionDelegate {

    /**
	 * The current selection.
	 */
    private ISelection selection;

    /**
	 * The constructor.
	 */
    public FanToggleNatureAction() {
        super();
    }

    /**
	 * @param arg0 The action.
	 * @param arg1 The workbench.
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction, org.eclipse.ui.IWorkbenchPart)
	 */
    public void setActivePart(final IAction arg0, final IWorkbenchPart arg1) {
    }

    /**
	 * @param arg0 The action.
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
    public final void run(final IAction arg0) {
        if (selection instanceof IStructuredSelection) {
            for (Iterator<?> it = ((IStructuredSelection) selection).iterator(); it.hasNext(); ) {
                Object element = it.next();
                IProject project = null;
                if (element instanceof IProject) {
                    project = (IProject) element;
                } else if (element instanceof IAdaptable) {
                    project = (IProject) ((IAdaptable) element).getAdapter(IProject.class);
                }
                if (project != null) {
                    toggleNature(project);
                }
            }
        }
    }

    /**
	 * @param action The action.
	 * @param selection The selection.
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
	 */
    public final void selectionChanged(final IAction action, final ISelection selection) {
        this.selection = selection;
    }

    /**
	 * @param project The project of the selection.
	 */
    private void toggleNature(final IProject project) {
        try {
            IProjectDescription description = project.getDescription();
            String[] natures = description.getNatureIds();
            for (int i = 0; i < natures.length; ++i) {
                if (FanNature.NATURE_ID.equals(natures[i])) {
                    String[] newNatures = new String[natures.length - 1];
                    System.arraycopy(natures, 0, newNatures, 0, i);
                    System.arraycopy(natures, i + 1, newNatures, i, natures.length - i - 1);
                    description.setNatureIds(newNatures);
                    project.setDescription(description, null);
                    return;
                }
            }
            String[] newNatures = new String[natures.length + 1];
            System.arraycopy(natures, 0, newNatures, 0, natures.length);
            newNatures[natures.length] = FanNature.NATURE_ID;
            description.setNatureIds(newNatures);
            project.setDescription(description, null);
        } catch (CoreException e) {
            e.printStackTrace();
        }
    }
}
