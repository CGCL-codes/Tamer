package com.dfruits.ui.wizards.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import com.dfruits.ui.wizards.WizardsManager;

public class ShowWizard implements IWorkbenchWindowActionDelegate, IViewActionDelegate, IEditorActionDelegate {

    public void run(IAction action) {
        WizardsManager.getPlugin().showWizard(action.getId());
    }

    public void dispose() {
    }

    public void init(IWorkbenchWindow window) {
    }

    public void selectionChanged(IAction action, ISelection selection) {
    }

    public void init(IViewPart view) {
    }

    public void setActiveEditor(IAction action, IEditorPart targetEditor) {
    }
}
