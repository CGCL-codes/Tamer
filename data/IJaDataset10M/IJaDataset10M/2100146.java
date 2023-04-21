package uk.ac.lkl.client;

import com.google.gwt.user.client.ui.Widget;
import uk.ac.lkl.common.util.expression.Expression;
import uk.ac.lkl.common.util.value.IntegerValue;
import uk.ac.lkl.migen.system.expresser.model.Attribute;
import uk.ac.lkl.migen.system.expresser.model.ColorResourceAttributeHandle;
import uk.ac.lkl.migen.system.expresser.model.CorrectColorExpressionAttributeHandle;
import uk.ac.lkl.migen.system.expresser.model.ExpresserModel;
import uk.ac.lkl.migen.system.expresser.model.ModelColor;
import uk.ac.lkl.migen.system.expresser.model.ValueSource;
import uk.ac.lkl.migen.system.expresser.model.shape.block.BlockShape;
import uk.ac.lkl.migen.system.expresser.model.shape.block.ModelGroupShape;

public class ColorSpecificModelRule extends RulePanel {

    private ModelColor color;

    private ExpressionPanelContainer colorRuleExpressionPanelContainer;

    public ColorSpecificModelRule(ModelColor color, MyModelRulesPanel modelRulesPanel) {
        super(modelRulesPanel.getCanvas());
        this.color = color;
        ExpresserCanvas canvas = getCanvas();
        ModelRulesButton modelRulesButton = new ModelRulesButton(canvas.getModel(), modelRulesPanel);
        contents.add(modelRulesButton);
        TileStackIcon icon = new TileStackIcon(color, 3, 32);
        contents.add(icon);
        colorRuleExpressionPanelContainer = new ColorRuleExpressionPanelContainer(color, canvas);
        contents.add(colorRuleExpressionPanelContainer);
        ruleFeedBack = new RuleFeedBack(RuleFeedBackState.NEUTRAL);
        contents.add(ruleFeedBack);
        setStylePrimaryName("expresser-color-specific-rule-panel");
    }

    @Override
    public ModelColor getColor() {
        return color;
    }

    public boolean isSpecified() {
        ColorResourceAttributeHandle handle = BlockShape.colorResourceAttributeHandle(color);
        ModelGroupShape modelAsAGroup = getCanvas().getModel().getModelAsAGroup();
        Attribute<IntegerValue> attribute = modelAsAGroup.getAttribute(handle);
        return attribute.getValue().isSpecified();
    }

    public ExpressionPanelContainer getColorRuleExpressionPanelContainer() {
        return colorRuleExpressionPanelContainer;
    }

    @Override
    public Widget getIcon(int size, MyModelRulesPanel myModelRulesPanel) {
        return new TileStackIcon(color, 3, size);
    }

    @Override
    public Expression<IntegerValue> getExpression() {
        ExpresserModel model = getCanvas().getModel();
        ModelGroupShape modelAsAGroup = model.getModelAsAGroup();
        ColorResourceAttributeHandle handle = BlockShape.colorResourceAttributeHandle(color);
        return modelAsAGroup.getAttributeExpression(handle);
    }

    @Override
    public void initialise() {
        colorRuleExpressionPanelContainer.initialise(getCanvas());
    }

    @Override
    protected ExpressionPanel getExpressionPanel() {
        return colorRuleExpressionPanelContainer.getExpressionPanel();
    }

    @Override
    public boolean isCorrect(ExpresserModel model) {
        ModelGroupShape modelAsAGroup = model.getModelAsAGroup();
        CorrectColorExpressionAttributeHandle correctExpressionHandle = CorrectColorExpressionAttributeHandle.getHandle(color);
        ValueSource<IntegerValue> valueSource = modelAsAGroup.getValueSource(correctExpressionHandle);
        if (valueSource == null || !modelAsAGroup.isCorrectExpressionsUpToDate()) {
            modelAsAGroup.generateCorrectColorExpressions();
            valueSource = modelAsAGroup.getValueSource(correctExpressionHandle);
        }
        IntegerValue correctValue = valueSource.getExpression().evaluate();
        ColorResourceAttributeHandle handle = BlockShape.colorResourceAttributeHandle(color);
        IntegerValue userValue = modelAsAGroup.getAttributeExpression(handle).evaluate();
        return correctValue.equals(userValue);
    }
}
