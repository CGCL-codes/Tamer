package com.abso.sunlight.explorer.handlers;

import java.util.Iterator;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import com.abso.sunlight.api.SunlightException;
import com.abso.sunlight.explorer.CongressExplorerPlugin;
import com.abso.sunlight.explorer.LegislatorSearch;
import com.abso.sunlight.explorer.viewers.ExplorerTreeNode;

/**
 * Deletes a set of selected searches.
 */
public class DeleteLegislatorSearchHandler extends AbstractHandler implements IHandler {

    public Object execute(ExecutionEvent event) throws ExecutionException {
        ISelection sel = HandlerUtil.getCurrentSelection(event);
        if (sel instanceof IStructuredSelection) {
            IStructuredSelection ssel = (IStructuredSelection) sel;
            if (!MessageDialog.openQuestion(HandlerUtil.getActiveShell(event), "Confirm Delete", "Are you sure you want to delete the selected " + ((ssel.size() == 1) ? "search?" : "searches?"))) {
                return null;
            }
            for (Iterator<?> i = ssel.iterator(); i.hasNext(); ) {
                ExplorerTreeNode node = (ExplorerTreeNode) i.next();
                LegislatorSearch search = (LegislatorSearch) node.getData();
                try {
                    CongressExplorerPlugin.getSearchManager().deleteSearch(search);
                } catch (SunlightException e) {
                    ExecutionException e2 = new ExecutionException("Unable to remove", e);
                    ErrorDialog.openError(HandlerUtil.getActiveShell(event), "Error", e2.getMessage(), new Status(IStatus.ERROR, CongressExplorerPlugin.PLUGIN_ID, e.getMessage(), e));
                    throw e2;
                }
            }
        }
        return null;
    }
}
