package com.aptana.ide.editors.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import com.aptana.ide.editors.managers.FileContextManager;
import com.aptana.ide.editors.unified.FileService;

/**
 *  Determine if wetell content validators to return informational messages ?
 *  
 *  Modeled after ShowErrors and ShowWarnings.
 * 
 * @see IWorkbenchWindowActionDelegate
 */
public class ShowInfos extends BaseAction implements IWorkbenchWindowActionDelegate {

    private static boolean state = false;

    /**
	 * isInstanceChecked
	 *
	 * @return boolean
	 */
    public static boolean isInstanceChecked() {
        return state;
    }

    /**
	 * The constructor.
	 */
    public ShowInfos() {
    }

    /**
	 * The action has been activated. The argument of the method represents the 'real' action sitting in the workbench
	 * UI.
	 * 
	 * @param action
	 */
    public void run(IAction action) {
        state = action.isChecked();
        IEditorPart editor = getActiveEditor();
        if (editor != null) {
            FileService fc = FileContextManager.get(editor.getEditorInput());
            if (fc != null) {
                fc.forceContentChangedEvent();
            }
        }
    }

    /**
	 * Selection in the workbench has been changed. We can change the state of the 'real' action here if we want, but
	 * this can only happen after the delegate has been created.
	 * 
	 * @param action
	 * @param selection
	 */
    public void selectionChanged(IAction action, ISelection selection) {
    }

    /**
	 * We can use this method to dispose of any system resources we previously allocated.
	 * 
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
    public void dispose() {
    }

    /**
	 * We will cache window object in order to be able to provide parent shell for the message dialog.
	 * 
	 * @param window
	 * @see IWorkbenchWindowActionDelegate#init
	 */
    public void init(IWorkbenchWindow window) {
    }
}
