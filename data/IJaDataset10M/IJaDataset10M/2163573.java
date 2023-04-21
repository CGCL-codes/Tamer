package org.dyno.visual.swing.widgets.grouplayout.undo;

import java.awt.Component;
import java.awt.Insets;
import javax.swing.JComponent;
import org.dyno.visual.swing.layouts.Constraints;
import org.dyno.visual.swing.layouts.Leading;
import org.dyno.visual.swing.layouts.Trailing;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class VerticalTrailingToLeading extends AbstractOperation {

    private Constraints oldconstraints;

    private JComponent container;

    private Component child;

    public VerticalTrailingToLeading(Constraints constraints, JComponent container, Component child) {
        super(Messages.VerticalTrailingToLeading_Set_Anchor0);
        this.oldconstraints = constraints;
        this.container = container;
        this.child = child;
    }

    @Override
    public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        Trailing trailing = (Trailing) oldconstraints.getVertical();
        int t = trailing.getTrailing();
        int h = child.getHeight();
        Insets insets = container.getInsets();
        int l = container.getHeight() - insets.top - insets.bottom - t - h;
        Constraints newconstraints = new Constraints(oldconstraints.getHorizontal(), new Leading(l, 10, t));
        container.remove(child);
        container.add(child, newconstraints);
        container.doLayout();
        container.invalidate();
        WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(container);
        adapter.repaintDesigner();
        return Status.OK_STATUS;
    }

    @Override
    public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        return execute(monitor, info);
    }

    @Override
    public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        container.remove(child);
        container.add(child, oldconstraints);
        container.doLayout();
        container.invalidate();
        WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(container);
        adapter.repaintDesigner();
        return Status.OK_STATUS;
    }
}
