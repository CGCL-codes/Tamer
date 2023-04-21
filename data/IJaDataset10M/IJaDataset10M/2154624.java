package com.ibm.tuningfork.core.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.actions.ActionDelegate;

/**
 * Action to connect the TuningFork receiver to a file containing a TuningFork feed.
 */
public class SaveAsGraphicAction extends ActionDelegate implements IWorkbenchWindowActionDelegate {

    private IWorkbenchWindow myWindow;

    private GraphicExportWorker worker;

    public void run(IAction action) {
        Shell shell = myWindow.getShell();
        worker.run(shell, shell);
    }

    public void init(IWorkbenchWindow window) {
        myWindow = window;
        worker = new GraphicExportWorker(false) {

            protected String getFigureKindName() {
                return "screen";
            }
        };
    }
}
