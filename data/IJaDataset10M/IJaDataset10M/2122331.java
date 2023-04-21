package uk.co.q3c.deplan.rcp.action.resource;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.swt.widgets.Display;
import uk.co.q3c.deplan.rcp.model.Model;

public class RenameResourceAction extends ResourceAction {

    public RenameResourceAction(ColumnViewer viewer, String text) {
        super(viewer, text);
    }

    @Override
    public void doAction() {
        String newName = "new name";
        InputDialog dlg = new InputDialog(Display.getCurrent().getActiveShell(), "New Name", "Enter new name", "", null);
        int result = dlg.open();
        if (result == IDialogConstants.OK_ID) {
            newName = dlg.getValue();
        }
        Model.resourceManager().rename(selectedResource, newName);
    }
}
