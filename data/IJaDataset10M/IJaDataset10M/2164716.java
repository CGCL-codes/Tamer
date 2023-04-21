package org.dyno.visual.swing.widgets.grouplayout;

import java.awt.Component;
import javax.swing.JComponent;
import org.dyno.visual.swing.layouts.Alignment;
import org.dyno.visual.swing.layouts.Bilateral;
import org.dyno.visual.swing.layouts.Constraints;
import org.dyno.visual.swing.layouts.Leading;
import org.dyno.visual.swing.layouts.Trailing;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.widgets.grouplayout.undo.HorizontalBilateralToLeading;
import org.dyno.visual.swing.widgets.grouplayout.undo.HorizontalBilateralToTrailing;
import org.dyno.visual.swing.widgets.grouplayout.undo.HorizontalLeadingToBilateral;
import org.dyno.visual.swing.widgets.grouplayout.undo.HorizontalLeadingToTrailing;
import org.dyno.visual.swing.widgets.grouplayout.undo.HorizontalTrailingToBilteral;
import org.dyno.visual.swing.widgets.grouplayout.undo.HorizontalTrailingToLeading;
import org.dyno.visual.swing.widgets.grouplayout.undo.VerticalBilateralToLeading;
import org.dyno.visual.swing.widgets.grouplayout.undo.VerticalBilateralToTrailing;
import org.dyno.visual.swing.widgets.grouplayout.undo.VerticalLeadingToBilateral;
import org.dyno.visual.swing.widgets.grouplayout.undo.VerticalLeadingToTrailing;
import org.dyno.visual.swing.widgets.grouplayout.undo.VerticalTrailingToBilteral;
import org.dyno.visual.swing.widgets.grouplayout.undo.VerticalTrailingToLeading;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.PlatformUI;

class SetAnchorAction extends Action {

    private JComponent container;

    private String anchor;

    private boolean horizontal;

    private Component child;

    public SetAnchorAction(JComponent container, boolean horizontal, String anchor, Component child) {
        super("", AS_RADIO_BUTTON);
        setId("#ANCHOR_" + (horizontal ? "HORIZONTAL" : "VERTICAL") + "_" + anchor);
        setText(anchor);
        this.container = container;
        this.horizontal = horizontal;
        this.anchor = anchor;
        this.child = child;
        setChecked(shouldChecked());
    }

    private boolean shouldChecked() {
        CompositeAdapter containerAdapter = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
        Constraints constraints = (Constraints) containerAdapter.getChildConstraints(child);
        Alignment alignment;
        if (horizontal) {
            alignment = constraints.getHorizontal();
        } else {
            alignment = constraints.getVertical();
        }
        String oldanchor = "leading";
        if (alignment instanceof Leading) {
            oldanchor = "leading";
        } else if (alignment instanceof Bilateral) {
            oldanchor = "bilateral";
        } else if (alignment instanceof Trailing) {
            oldanchor = "trailing";
        }
        return oldanchor.equals(anchor);
    }

    public void run() {
        if (!shouldChecked()) {
            IUndoableOperation operation = null;
            CompositeAdapter containerAdapter = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
            Constraints constraints = (Constraints) containerAdapter.getChildConstraints(child);
            if (horizontal) {
                Alignment alignment = constraints.getHorizontal();
                if (alignment instanceof Leading) {
                    if (anchor.equals("bilateral")) {
                        operation = new HorizontalLeadingToBilateral(constraints, container, child);
                    } else if (anchor.equals("trailing")) {
                        operation = new HorizontalLeadingToTrailing(constraints, container, child);
                    }
                } else if (alignment instanceof Bilateral) {
                    if (anchor.equals("leading")) {
                        operation = new HorizontalBilateralToLeading(constraints, container, child);
                    } else if (anchor.equals("trailing")) {
                        operation = new HorizontalBilateralToTrailing(constraints, container, child);
                    }
                } else if (alignment instanceof Trailing) {
                    if (anchor.equals("leading")) {
                        operation = new HorizontalTrailingToLeading(constraints, container, child);
                    } else if (anchor.equals("bilateral")) {
                        operation = new HorizontalTrailingToBilteral(constraints, container, child);
                    }
                }
            } else {
                Alignment alignment = constraints.getVertical();
                if (alignment instanceof Leading) {
                    if (anchor.equals("bilateral")) {
                        operation = new VerticalLeadingToBilateral(constraints, container, child);
                    } else if (anchor.equals("trailing")) {
                        operation = new VerticalLeadingToTrailing(constraints, container, child);
                    }
                } else if (alignment instanceof Bilateral) {
                    if (anchor.equals("leading")) {
                        operation = new VerticalBilateralToLeading(constraints, container, child);
                    } else if (anchor.equals("trailing")) {
                        operation = new VerticalBilateralToTrailing(constraints, container, child);
                    }
                } else if (alignment instanceof Trailing) {
                    if (anchor.equals("leading")) {
                        operation = new VerticalTrailingToLeading(constraints, container, child);
                    } else if (anchor.equals("bilateral")) {
                        operation = new VerticalTrailingToBilteral(constraints, container, child);
                    }
                }
            }
            if (operation != null) {
                WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(container);
                operation.addContext(adapter.getUndoContext());
                IOperationHistory operationHistory = PlatformUI.getWorkbench().getOperationSupport().getOperationHistory();
                try {
                    operationHistory.execute(operation, null, null);
                } catch (ExecutionException e) {
                    GroupLayoutPlugin.getLogger().error(e);
                }
            }
        }
    }
}
