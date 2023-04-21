package com.netprogress.rcp.ui.framework.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.update.ui.UpdateManagerUI;
import com.netprogress.rcp.ui.framework.commonUi.ResourceManager;

public class OpenUpdateManagerAction extends Action {

    private IWorkbenchWindow window;

    public OpenUpdateManagerAction(IWorkbenchWindow window) {
        this.window = window;
        setText("Mise � jour...");
        setImageDescriptor(ResourceManager.getImageDescriptor(this.getClass(), "/icons/list.gif"));
    }

    public void run() {
        BusyIndicator.showWhile(window.getShell().getDisplay(), new Runnable() {

            public void run() {
                UpdateManagerUI.openInstaller(window.getShell());
            }
        });
    }
}
