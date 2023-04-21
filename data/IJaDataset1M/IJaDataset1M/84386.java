package org.jampa.gui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jampa.gui.dialogs.CheckPlaylistsDialog;

public class CheckPlaylistsHandler extends AbstractHandler implements IHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        CheckPlaylistsDialog dialog = new CheckPlaylistsDialog(HandlerUtil.getActiveWorkbenchWindow(event).getShell());
        dialog.open();
        return null;
    }
}
