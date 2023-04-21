package com.ivis.xprocess.ui.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

/**
 * A simple utility class to open the Help window.
 *
 */
public class LaunchHelp implements IWorkbenchWindowActionDelegate {

    public void dispose() {
    }

    public void init(IWorkbenchWindow window) {
    }

    public void run(IAction action) {
        PlatformUI.getWorkbench().getHelpSystem().displayHelp();
    }

    public void selectionChanged(IAction action, ISelection selection) {
    }
}
