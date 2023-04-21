package org.dyno.visual.swing.widgets.grouplayout.undo;

import java.awt.Component;
import java.awt.Insets;
import java.util.ArrayList;
import javax.swing.JComponent;
import org.dyno.visual.swing.layouts.Alignment;
import org.dyno.visual.swing.layouts.Bilateral;
import org.dyno.visual.swing.layouts.Constraints;
import org.dyno.visual.swing.layouts.GroupLayout;
import org.dyno.visual.swing.layouts.Leading;
import org.dyno.visual.swing.layouts.Trailing;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.widgets.grouplayout.GroupLayoutAdapter;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class CenterAlignmentOperation extends AlignmentOperation {

    public CenterAlignmentOperation(JComponent container, GroupLayoutAdapter glAdapter) {
        super(Messages.CenterAlignmentOperation_Align_Center, container, glAdapter);
    }

    @Override
    public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        GroupLayout layout = (GroupLayout) container.getLayout();
        CompositeAdapter containerAdapter = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
        WidgetAdapter post = widgets.get(0);
        Component postChild = post.getWidget();
        Constraints postConstraints = layout.getConstraints(postChild);
        Alignment postAlignment = postConstraints.getHorizontal();
        compcons = new ArrayList<CompCons>();
        Insets insets = container.getInsets();
        int innerWidth = container.getWidth() - insets.left - insets.right;
        if (postAlignment instanceof Leading) {
            Leading postLeading = (Leading) postAlignment;
            int postCenter = postLeading.getLeading() + postChild.getWidth() / 2;
            for (int i = 1; i < widgets.size(); i++) {
                WidgetAdapter adapter = widgets.get(i);
                Component child = adapter.getWidget();
                Constraints constraints = layout.getConstraints(child);
                CompCons cons = new CompCons();
                cons.component = child;
                cons.constraints = constraints;
                compcons.add(cons);
                int l = postCenter - child.getWidth() / 2;
                int t = innerWidth - postCenter - child.getWidth() / 2;
                Leading leading = new Leading(l, child.getWidth(), 10, t);
                constraints = new Constraints(leading, constraints.getVertical());
                layout.setConstraints(child, constraints);
            }
        } else if (postAlignment instanceof Bilateral) {
            Bilateral postBilateral = (Bilateral) postAlignment;
            int postCenter = postBilateral.getLeading() + postChild.getWidth() / 2;
            for (int i = 1; i < widgets.size(); i++) {
                WidgetAdapter adapter = widgets.get(i);
                Component child = adapter.getWidget();
                Constraints constraints = layout.getConstraints(child);
                CompCons cons = new CompCons();
                cons.component = child;
                cons.constraints = constraints;
                compcons.add(cons);
                int l = postCenter - child.getWidth() / 2;
                int t = innerWidth - postCenter - child.getWidth() / 2;
                Bilateral bilateral = new Bilateral(l, t, 10);
                constraints = new Constraints(bilateral, constraints.getVertical());
                layout.setConstraints(child, constraints);
            }
        } else if (postAlignment instanceof Trailing) {
            int postCenter = postChild.getX() + postChild.getWidth() / 2 - insets.left;
            for (int i = 1; i < widgets.size(); i++) {
                WidgetAdapter adapter = widgets.get(i);
                Component child = adapter.getWidget();
                Constraints constraints = layout.getConstraints(child);
                CompCons cons = new CompCons();
                cons.component = child;
                cons.constraints = constraints;
                compcons.add(cons);
                int l = postCenter - child.getWidth() / 2;
                int t = innerWidth - postCenter - child.getWidth() / 2;
                Trailing trailing = new Trailing(t, child.getWidth(), 10, l);
                constraints = new Constraints(trailing, constraints.getVertical());
                layout.setConstraints(child, constraints);
            }
        }
        container.invalidate();
        containerAdapter.doLayout();
        containerAdapter.setDirty(true);
        containerAdapter.repaintDesigner();
        return Status.OK_STATUS;
    }
}
