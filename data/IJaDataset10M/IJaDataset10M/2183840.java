package org.junithelper.plugin.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.junithelper.plugin.action.OpenTestTargetAction;

public class OpenTestTargetHandler extends AbstractHandler {

    public OpenTestTargetHandler() {
    }

    public Object execute(ExecutionEvent event) throws ExecutionException {
        ISelection selection = HandlerUtil.getCurrentSelection(event);
        OpenTestTargetAction action = new OpenTestTargetAction();
        action.selectionChanged(null, selection);
        action.run(null);
        return null;
    }
}
