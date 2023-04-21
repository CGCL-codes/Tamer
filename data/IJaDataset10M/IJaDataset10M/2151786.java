package drarch.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.PlatformUI;
import drarch.Application;

public class LoadRuleFileAction extends Action {

    private String fileName = "";

    public LoadRuleFileAction() {
    }

    public void run() {
        FileDialog dialog = new FileDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
        dialog.open();
        fileName = dialog.getFileName();
        String path = dialog.getFilterPath() + "/";
        Application.getApplication().setExternalFilePath(path + fileName);
    }

    public String getFileName() {
        return fileName;
    }
}
