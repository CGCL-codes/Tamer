package org.pubcurator.core.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.pubcurator.core.managers.UIManager;
import org.pubcurator.core.views.OverallTreeView;

public class ModifyOverallTreeHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        String mode = event.getParameter("mode");
        if (mode.equals("expand_all")) {
            OverallTreeView view = (OverallTreeView) UIManager.INSTANCE.getView(OverallTreeView.class);
            view.expandAll();
        } else {
            OverallTreeView view = (OverallTreeView) UIManager.INSTANCE.getView(OverallTreeView.class);
            view.collapseAll();
        }
        return null;
    }
}
