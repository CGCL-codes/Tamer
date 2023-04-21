package org.hibnet.lune.ui.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.hibnet.lune.ui.IndexInput;
import org.hibnet.lune.ui.LuneUIPlugin;
import org.hibnet.lune.ui.dialog.NewIndexDialog;

public class NewIndexInputAction extends Action {

    public NewIndexInputAction() {
        setText("New");
        setToolTipText("Create an index");
        setImageDescriptor(LuneUIPlugin.getDescriptor("newLocalIndex"));
    }

    @Override
    public void run() {
        NewIndexDialog dialog = new NewIndexDialog(LuneUIPlugin.getActiveWorkbenchShell());
        if (dialog.open() == Window.OK) {
            IndexInput index = dialog.getIndex();
            LuneUIPlugin.getIndexManager().add(index);
            LuneUIPlugin.getIndexesView().refresh();
        }
    }
}
