package uk.ac.lkl.client;

import java.util.ArrayList;
import uk.ac.lkl.common.util.event.UpdateEvent;
import uk.ac.lkl.common.util.expression.Expression;
import uk.ac.lkl.common.util.value.IntegerValue;
import uk.ac.lkl.migen.system.expresser.model.shape.block.PatternShape;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;

/**
 * Implements the wizard for creating patterns from building blocks
 * 
 * @author Ken Kahn
 *
 */
public class PatternWizard extends PropertyList {

    private HorizontalPanel buttonPanel;

    public PatternWizard(final PatternView patternView, final PickupDragController dragController) {
        super(Expresser.messagesBundle.WizardTitle() + " (1 " + Expresser.messagesBundle.of() + " 2)", patternView);
        contents.add(new HTML(Expresser.messagesBundle.WhereToPlaceSucessiveCopies()));
        Grid placeGrid = new PropertyListGrid(2, 2);
        placeGrid.setCellPadding(6);
        ExpressionInterface deltaXExpression = patternView.getDeltaXExpression();
        PickupDragController expressionDragController = Expresser.instance().getExpressionDragController();
        placeGrid.setWidget(0, 0, new PropertyItemDeltaX(patternView, deltaXExpression.getPanel(expressionDragController)));
        placeGrid.setWidget(0, 1, new IconImage(Expresser.images.deltaX()));
        ExpressionInterface deltaYExpression = patternView.getDeltaYExpression();
        placeGrid.setWidget(1, 0, new PropertyItemDeltaY(patternView, deltaYExpression.getPanel(expressionDragController)));
        placeGrid.setWidget(1, 1, new IconImage(Expresser.images.deltaY()));
        contents.add(placeGrid);
        contents.add(new HTML(Expresser.messagesBundle.HowManyBuildingBlocks()));
        ExpresserHorizontalPanel iterationPanel = new ExpresserHorizontalPanel();
        iterationPanel.setSpacing(6);
        iterationPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        ExpressionInterface iterationsExpression = patternView.getIterationsExpression();
        iterationPanel.add(new PropertyItemIterations(patternView, iterationsExpression.getPanel(expressionDragController)));
        iterationPanel.add(new ConstructionExpressionSymbol("&times;"));
        iterationPanel.add(new ThumbNailButton(patternView.getBuildingBlock(), 32));
        contents.add(iterationPanel);
        buttonPanel = new ExpresserHorizontalPanel();
        buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        final Button okButton = new Button(Expresser.messagesBundle.OK());
        ClickHandler okClickHandler = new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                patternView.getPatternShape().setBuildingBlockStatus(PatternShape.DO_NOT_TREAT_AS_A_BUILDING_BLOCK);
                ExpresserCanvasPanel canvas = Utilities.getCanvas(PatternWizard.this);
                if (canvas != null) {
                    int absoluteLeft = PatternWizard.this.getAbsoluteLeft();
                    int absoluteTop = PatternWizard.this.getAbsoluteTop();
                    canvas.remove(PatternWizard.this);
                    PatternPropertyList propertyList = new PatternPropertyList(patternView, dragController);
                    propertyList.setTitleBarText(Expresser.messagesBundle.WizardTitle() + " (2 " + Expresser.messagesBundle.of() + " 2)");
                    canvas.add(propertyList, absoluteLeft - canvas.getAbsoluteLeft(), absoluteTop - canvas.getAbsoluteTop());
                    dragController.makeDraggable(propertyList);
                }
            }
        };
        okButton.addClickHandler(okClickHandler);
        okButton.setEnabled(false);
        final ArrayList<Expression<IntegerValue>> unspecifiedExpressions = new ArrayList<Expression<IntegerValue>>();
        unspecifiedExpressions.add(deltaXExpression.getExpression());
        unspecifiedExpressions.add(deltaYExpression.getExpression());
        unspecifiedExpressions.add(iterationsExpression.getExpression());
        ExpressionListener updateListener = new ExpressionListener() {

            @Override
            public void objectUpdated(UpdateEvent<Expression<IntegerValue>> e) {
                unspecifiedExpressions.remove(e.getSource());
                if (unspecifiedExpressions.isEmpty()) {
                    okButton.setEnabled(true);
                }
            }
        };
        for (Expression<IntegerValue> expression : unspecifiedExpressions) {
            expression.addUpdateListener(updateListener);
        }
        buttonPanel.add(okButton);
        Button cancelButton = new Button(Expresser.messagesBundle.Cancel());
        ClickHandler cancelClickHandler = new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                patternView.undoPattern(true);
                PatternWizard.this.removeFromParent();
            }
        };
        cancelButton.addClickHandler(cancelClickHandler);
        buttonPanel.add(cancelButton);
        contents.add(buttonPanel);
        contents.setStylePrimaryName("expresser-pattern-property-list-panel");
    }

    @Override
    public void onLoad() {
        super.onLoad();
        buttonPanel.setWidth(getOffsetWidth() + "px");
    }
}
