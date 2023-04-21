package org.groundsquirrel.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.groundsquirrel.Activator;
import org.groundsquirrel.union.Union;

public class NewFileAction extends Action {

    public static final String ID = "org.groundsquirrel.actions.NewFileAction";

    public static final String IMAGE_KEY = "icons/icon_newfile.gif";

    private Union union;

    public NewFileAction(Union union) {
        this.union = union;
        setText("New File");
        setId(NewFileAction.ID);
        setActionDefinitionId(NewFileAction.ID);
        setImageDescriptor(Activator.getImageDescriptor(NewFileAction.IMAGE_KEY));
    }

    public void run() {
        FileDialog fd = new FileDialog(union.getWindow().getShell(), SWT.SAVE);
        fd.setText("New File");
        fd.setOverwrite(true);
        String[] filterExt = { "*.nut", "*.txt", "*.*" };
        String[] filterNames = { "Squirrel script files (*.nut)", "Text files (*.txt)", "All files (*.*)" };
        fd.setFilterExtensions(filterExt);
        fd.setFilterNames(filterNames);
        String selected = fd.open();
        if (selected.length() == 0) {
            return;
        }
        union.openFile(selected, true);
    }
}
