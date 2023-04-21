package com.dokumentarchiv.filter.FilterEditor.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import com.dokumentarchiv.filter.FilterEditor.TreeView;

/**
 * @author Carsten Burghardt
 * @version $Id: FilterOpenAction.java 547 2007-11-21 19:52:58Z carsten $
 */
public class FilterOpenAction implements IWorkbenchWindowActionDelegate {

    private IWorkbenchWindow window;

    /**
     * @param text
     */
    public FilterOpenAction() {
    }

    public void dispose() {
    }

    public void init(IWorkbenchWindow window) {
        this.window = window;
    }

    public void run(IAction action) {
        FileDialog dialog = new FileDialog(window.getShell(), SWT.OPEN);
        dialog.setFilterExtensions(new String[] { "*.xml" });
        dialog.open();
        if (dialog.getFileName() == null || dialog.getFileName().length() < 1) {
            return;
        }
        String file = dialog.getFilterPath() + "/" + dialog.getFileName();
        TreeView view = (TreeView) window.getActivePage().findView(TreeView.ID);
        view.initTreeFromXML(file);
    }

    public void selectionChanged(IAction action, ISelection selection) {
    }
}
