package org.exist.eclipse.browse.internal.views.browse;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.exist.eclipse.IConnection;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.browse.internal.connection.OpenConnectionListener;
import org.exist.eclipse.browse.internal.views.document.DocumentBrowseListener;

/**
 * @author Pascal Schmidiger
 * 
 */
public class ActionDoubleClick extends Action {

    private final BrowseView _view;

    ActionDoubleClick(BrowseView view) {
        _view = view;
    }

    @Override
    public void run() {
        Object[] selection = ((IStructuredSelection) _view.getViewer().getSelection()).toArray();
        if (selection.length > 0) {
            Object data = selection[0];
            if (data instanceof IConnection) {
                new ActionConnectionListener(_view, new OpenConnectionListener()).run();
                _view.getViewer().expandToLevel(data, 1);
            } else if (data instanceof IBrowseItem) {
                new ActionBrowseListener(_view, new DocumentBrowseListener()).run();
            }
        }
    }
}
