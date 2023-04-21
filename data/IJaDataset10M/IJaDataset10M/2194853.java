package org.dyno.visual.swing.widgets.undo;

import java.awt.Component;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class RightAlignmentOperation extends AbstractOperation {

    private List<WidgetAdapter> selection;

    private List<Integer> boundx;

    public RightAlignmentOperation(List<WidgetAdapter> selected) {
        super(Messages.RightAlignmentOperation_Align_Right);
        selection = new ArrayList<WidgetAdapter>();
        for (WidgetAdapter adapter : selected) {
            selection.add(adapter);
        }
    }

    @Override
    public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        int x = -1;
        boundx = new ArrayList<Integer>();
        for (int i = 0; i < selection.size(); i++) {
            WidgetAdapter childAdapter = selection.get(i);
            Component child = childAdapter.getWidget();
            if (x == -1) {
                boundx.add(child.getX());
                x = child.getX() + child.getWidth();
            } else {
                Rectangle bounds = child.getBounds();
                boundx.add(bounds.x);
                bounds.x = x - bounds.width;
                child.setBounds(bounds);
            }
        }
        return Status.OK_STATUS;
    }

    @Override
    public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        return execute(monitor, info);
    }

    @Override
    public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        for (int i = 0; i < selection.size(); i++) {
            WidgetAdapter childAdapter = selection.get(i);
            Component child = childAdapter.getWidget();
            Rectangle bounds = child.getBounds();
            bounds.x = boundx.get(i);
            child.setBounds(bounds);
        }
        return Status.OK_STATUS;
    }
}
