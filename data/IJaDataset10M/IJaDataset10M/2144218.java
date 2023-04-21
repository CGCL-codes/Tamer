package org.jampa.gui.handlers.menuactions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jampa.gui.dialogs.RemovableStorageDialog;

public class ManageRemovableStorageHandler extends AbstractHandler implements IHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        RemovableStorageDialog dialog = new RemovableStorageDialog(HandlerUtil.getActiveWorkbenchWindow(event).getShell());
        dialog.open();
        return null;
    }
}
