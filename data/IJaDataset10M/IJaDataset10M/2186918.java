package uk.ac.lkl.client;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;
import com.google.gwt.user.client.ui.Widget;

/**
 * Drop controller to implement dropping expressions on expressions
 * 
 * @author Ken Kahn
 *
 */
public class ExpressionDropController extends SimpleDropController {

    public ExpressionDropController(Widget dropTarget) {
        super(dropTarget);
    }

    @Override
    public void onDrop(DragContext context) {
        super.onDrop(context);
        if (canDropExpressionOnExpression(context.draggable)) {
            ((ExpressionPanel) getDropTarget()).createPopupMenuExpressionOnExpression((ExpressionPanel) context.draggable, context.mouseX, context.mouseY);
        }
    }

    @Override
    public void onEnter(DragContext context) {
        super.onEnter(context);
        if (canDropExpressionOnExpression(context.draggable)) {
            ((ExpressionPanel) getDropTarget()).addDropFeedback();
        }
    }

    private boolean canDropExpressionOnExpression(Widget draggable) {
        if (draggable instanceof ExpressionPanel || draggable.getParent() instanceof ExpressionPanel) {
            Widget dropTarget = getDropTarget();
            if (!(dropTarget.getParent() instanceof TiedNumberPalette)) {
                return (dropTarget instanceof ExpressionPanel);
            }
        }
        return false;
    }

    @Override
    public void onLeave(DragContext context) {
        super.onLeave(context);
        Widget dropTarget = getDropTarget();
        if (dropTarget instanceof ExpressionPanel) {
            ((ExpressionPanel) dropTarget).removeDropFeedback();
        }
    }
}
