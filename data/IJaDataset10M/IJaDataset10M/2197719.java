package com.aptana.ide.scripting.menus;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

/**
 * @author Kevin Lindsey
 */
public abstract class MenuAction implements IWorkbenchWindowActionDelegate {

    /**
	 * Get the text name of this menu
	 * 
	 * @return String
	 */
    public abstract String getName();

    /**
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#dispose()
	 */
    public void dispose() {
        Menus.removeMenu(this.getName());
    }

    /**
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.IWorkbenchWindow)
	 */
    public void init(IWorkbenchWindow window) {
        Menus.addMenu(this.getName());
    }

    /**
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
    public void run(IAction action) {
        Menus.fireEventListeners(this.getName());
    }

    /**
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
    public void selectionChanged(IAction action, ISelection selection) {
    }
}
