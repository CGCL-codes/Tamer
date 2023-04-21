package com.ecmdeveloper.plugin.search.handlers;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import com.ecmdeveloper.plugin.search.ui.SearchPage;

/**
 * @author ricardo.belfor
 *
 */
public class ContentEngineSearchHandler implements IWorkbenchWindowActionDelegate {

    private IWorkbenchWindow window;

    @Override
    public void init(IWorkbenchWindow window) {
        this.window = window;
    }

    @Override
    public void dispose() {
        window = null;
    }

    @Override
    public void run(IAction action) {
        if (window == null || window.getActivePage() == null) {
            return;
        }
        NewSearchUI.openSearchDialog(window, SearchPage.ID);
    }

    @Override
    public void selectionChanged(IAction action, ISelection selection) {
    }
}
