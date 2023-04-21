package de.humanfork.treemerge.treeeditor.actions;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import de.humanfork.treemerge.treeeditor.LifeCycle;

/**
 * Our sample action implements workbench action delegate.
 * The action proxy will be created by the workbench and
 * shown in the UI. When the user tries to use the action,
 * this delegate will be created and execution will be
 * delegated to it.
 * @see IWorkbenchWindowActionDelegate
 */
public class Open3WMergeDialogAction implements IWorkbenchWindowActionDelegate {

    private static Logger logger = Logger.getLogger(Open3WMergeDialogAction.class);

    /**
     * The constructor.
     */
    public Open3WMergeDialogAction() {
    }

    static class FileLabelProvider extends LabelProvider {

        public String getText(Object element) {
            if (element instanceof IFile) {
                IPath path = ((IFile) element).getFullPath();
                return path != null ? path.toString() : "";
            }
            return super.getText(element);
        }
    }

    /**
     * The action has been activated. The argument of the
     * method represents the 'real' action sitting
     * in the workbench UI.
     * @see IWorkbenchWindowActionDelegate#run
     */
    public void run(IAction action) {
        if (logger.isTraceEnabled()) logger.trace("Open 3 Way Merge Dialog");
        LifeCycle.create().run();
    }

    /**
     * Selection in the workbench has been changed. We
     * can change the state of the 'real' action here
     * if we want, but this can only happen after
     * the delegate has been created.
     * @see IWorkbenchWindowActionDelegate#selectionChanged
     */
    public void selectionChanged(IAction action, ISelection selection) {
    }

    /**
     * We can use this method to dispose of any system
     * resources we previously allocated.
     * @see IWorkbenchWindowActionDelegate#dispose
     */
    public void dispose() {
    }

    /**
     * We will cache window object in order to
     * be able to provide parent shell for the message dialog.
     * @see IWorkbenchWindowActionDelegate#init
     */
    public void init(IWorkbenchWindow window) {
    }
}
