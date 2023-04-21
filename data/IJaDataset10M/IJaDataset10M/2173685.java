package org.jampa.gui.handlers.menuactions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.jampa.controllers.LogController;

public class CopyLogToClipboardHandler extends AbstractHandler implements IHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        LogController.getInstance().copyLogToClipboard();
        return null;
    }
}
