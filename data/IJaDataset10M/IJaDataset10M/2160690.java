package org.remus.infomngmnt.ui.views.action;

import java.util.List;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;

/**
 * @author Tom Seidel <tom.seidel@remus-software.org>
 */
public class RefreshTreeAction extends Action {

    private Viewer viewer;

    public RefreshTreeAction() {
        super("Refresh");
    }

    public void setViewer(final Viewer viewer) {
        this.viewer = viewer;
    }

    @Override
    public void run() {
        if (this.viewer != null) {
            if (this.viewer instanceof StructuredViewer) {
                List list = ((IStructuredSelection) this.viewer.getSelection()).toList();
                if (list.size() > 0) {
                    for (Object object : list) {
                        ((StructuredViewer) this.viewer).refresh(object, true);
                    }
                } else {
                    this.viewer.refresh();
                }
            } else {
                this.viewer.refresh();
            }
        }
        super.run();
    }
}
