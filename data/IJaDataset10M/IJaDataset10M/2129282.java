package uk.ac.lkl.client;

import java.util.List;
import uk.ac.lkl.common.util.event.UpdateEvent;
import uk.ac.lkl.common.util.event.UpdateListener;
import uk.ac.lkl.common.util.expression.Expression;
import uk.ac.lkl.common.util.value.IntegerValue;
import uk.ac.lkl.migen.system.expresser.model.tiednumber.TiedNumberExpression;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * Displays the current evaluation of the expression of its panel
 * 
 * @author Ken Kahn
 *
 */
public class CalculatedTiedNumberPanel extends TiedNumberPanel {

    private CompoundExpressionPanel compoundExpressionPanel;

    public CalculatedTiedNumberPanel(final CompoundExpressionPanel compoundExpressionPanel, PickupDragController dragController) {
        super(new TiedNumber(compoundExpressionPanel.getExpressionInterface().evaluateToInt()), dragController);
        this.compoundExpressionPanel = compoundExpressionPanel;
        List<TiedNumberExpression<IntegerValue>> containedTiedNumbers = compoundExpressionPanel.getExpressionInterface().getExpression().getContainedTiedNumbers(true);
        for (TiedNumberExpression<IntegerValue> tiedNumber : containedTiedNumbers) {
            UpdateListener<Expression<IntegerValue>> updateListener = new UpdateListener<Expression<IntegerValue>>() {

                @Override
                public void objectUpdated(UpdateEvent<Expression<IntegerValue>> e) {
                    getTiedNumber().setValue(compoundExpressionPanel.getExpressionInterface().evaluateToInt());
                }
            };
            tiedNumber.addUpdateListener(updateListener);
        }
        setStylePrimaryName("expresser-calculated-tied-number-panel");
        addStyleName("expresser-unselectable");
    }

    @Override
    public void setDropTarget(boolean dropTarget) {
    }

    @Override
    protected void createPopUpMenu(ClickEvent event) {
        final PopupPanel popupMenu = new PopupPanel(true);
        MenuBar menu = new MenuBar(true);
        menu.setAnimationEnabled(true);
        popupMenu.setWidget(menu);
        popupMenu.setAnimationEnabled(true);
        final ExpresserCanvasPanel canvas = Utilities.getCanvas(this);
        Command addExpressionCommand = new Command() {

            @Override
            public void execute() {
                popupMenu.hide();
                CompoundExpressionPanel expressionPanel = new CompoundExpressionPanel((CompoundExpression) compoundExpressionPanel.getExpressionInterface(), getDragController());
                canvas.add(expressionPanel, getAbsoluteLeft() + getOffsetWidth() - canvas.getAbsoluteLeft(), getAbsoluteTop() - canvas.getAbsoluteTop());
                expressionPanel.setDraggable(true);
            }
        };
        MenuItem addExpressionMenuItem = new MenuItem(Expresser.messagesBundle.ShowExpression(), addExpressionCommand);
        menu.addItem(addExpressionMenuItem);
        Command deleteCommand = new Command() {

            @Override
            public void execute() {
                popupMenu.hide();
                removeFromContainer(canvas.getEventManager(), true);
            }
        };
        MenuItem deleteMenuItem = new MenuItem(Expresser.messagesBundle.Remove(), deleteCommand);
        menu.addItem(deleteMenuItem);
        ExpressionPanelContainer container = getContainer();
        deleteMenuItem.setEnabled(container != null && container.getExpressionPanelWhenRemoved() == null);
        popupMenu.show();
        canvas.setPopupPosition(event.getClientX(), event.getClientY(), popupMenu);
    }
}
