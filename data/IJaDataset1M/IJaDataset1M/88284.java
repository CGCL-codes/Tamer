package net.zehrer.vse.controller.action;

import net.zehrer.vse.model.IStorageService;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class ConnectionTestAction extends Action {

    private Shell shell = null;

    private IStorageService service = null;

    public ConnectionTestAction() {
        setText("Test Connection");
    }

    @Override
    public void run() {
        String result = this.service.testConnection();
        if (result == null) {
            MessageDialog.openInformation(this.shell, "Connection Test", "Connection test was sucessful!");
        } else {
            MessageDialog.openError(this.shell, "Connection Test", "Error: " + result);
        }
    }

    public void setService(IStorageService service) {
        this.service = service;
    }

    public void setShell(Shell shell) {
        this.shell = shell;
    }
}
