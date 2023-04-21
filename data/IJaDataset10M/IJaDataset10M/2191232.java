package de.fh_zwickau.asmplugin.ui;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import de.fh_zwickau.asmplugin.ProgramExecuter;

/**
 * Action to open a console.
 * 
 * @author Daniel Mitte
 * @since 13.02.2006
 */
public class ConsoleAction extends Action implements IWorkbenchWindowActionDelegate {

    /**
   * {@inheritDoc}
   */
    public void dispose() {
    }

    /**
   * {@inheritDoc}
   */
    public void init(IWorkbenchWindow window) {
    }

    /**
   * {@inheritDoc}
   */
    public void run(IAction action) {
        ProgramExecuter.openConsole();
    }

    /**
   * {@inheritDoc}
   */
    public void selectionChanged(IAction action, ISelection selection) {
    }
}
