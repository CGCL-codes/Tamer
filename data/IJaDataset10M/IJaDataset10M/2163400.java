package com.simonepezzano.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import com.simonepezzano.hshare.views.HelpView;

/**
 * Opens the help view
 * @author Simone Pezzano
 *
 */
public class HelpAction extends Action implements IAction {

    public void run() {
        try {
            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(HelpView.ID);
        } catch (PartInitException e) {
            e.printStackTrace();
        }
    }
}
