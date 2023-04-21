package org.schemeway.plugins.schemescript.action;

import org.eclipse.jface.action.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.ui.*;
import org.schemeway.plugins.schemescript.*;
import org.schemeway.plugins.schemescript.interpreter.*;

public class StartInterpreterAction extends Action implements IWorkbenchWindowActionDelegate {

    public StartInterpreterAction() {
        setText("Start the Scheme interpreter");
        setToolTipText("Start interpreter");
    }

    public void run() {
        Interpreter interp = SchemeScriptPlugin.getDefault().getInterpreter();
        if (interp == null) return;
        interp.start();
    }

    public void dispose() {
    }

    public void init(IWorkbenchWindow window) {
    }

    public void run(IAction action) {
        run();
    }

    public void selectionChanged(IAction action, ISelection selection) {
    }
}
