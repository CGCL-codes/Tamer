package org.processmining.framework.models.hlprocess.expr.operator;

import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import org.processmining.framework.models.hlprocess.att.HLAttributeValue;
import org.processmining.framework.models.hlprocess.att.HLBooleanValue;
import org.processmining.framework.models.hlprocess.expr.HLExpressionElement;
import org.processmining.framework.models.hlprocess.expr.HLExpressionOperand;
import org.processmining.framework.models.hlprocess.expr.HLExpressionOperator;
import org.processmining.framework.models.hlprocess.expr.operand.HLValueOperand;

/**
 * Logical = operator.
 * <p>
 * Requires at least two input operands to be evaluated.
 */
public class HLEqualOperator extends HLExpressionOperator {

    /**
	 * Default constructor.
	 */
    public HLEqualOperator() {
        minNumberInputs = 2;
    }

    protected HLExpressionOperand evaluateOperands(List<HLExpressionOperand> operands) {
        boolean result = false;
        HLAttributeValue lastValue = operands.get(0).getValue();
        for (HLExpressionOperand op : operands) {
            result = lastValue.equals(op.getValue());
            lastValue = op.getValue();
        }
        return new HLValueOperand(new HLBooleanValue(result));
    }

    public String toString() {
        return "=";
    }

    public boolean hasMaxNumberInputs() {
        return false;
    }

    public String evaluateToString() {
        String result = "";
        for (int i = 0; i < getExpressionNode().getChildCount(); i++) {
            HLExpressionElement childExpr = (HLExpressionElement) ((DefaultMutableTreeNode) getExpressionNode().getChildAt(i)).getUserObject();
            if (result == "") {
                result = result + childExpr.evaluateToString();
            } else {
                result = result + " == " + childExpr.evaluateToString();
            }
        }
        return result;
    }
}
