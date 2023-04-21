package jfb.examples.gmf.math.diagram.edit.parts.custom;

import jfb.examples.gmf.math.Number;
import jfb.examples.gmf.math.Operator;
import jfb.examples.gmf.math.OperatorInput;
import jfb.examples.gmf.math.PlusOperator;
import jfb.examples.gmf.math.Result;
import jfb.examples.gmf.math.diagram.util.CycleDetectionHelper;
import org.eclipse.emf.common.util.EList;

public class AutomaticComputationHelper {

    public static void numberValueChanged(Number number) {
        EList<OperatorInput> inputs = number.getOperatorInputs();
        for (OperatorInput operatorInput : inputs) {
            Operator op = operatorInput.getOperator();
            updateOperatorResult(op);
        }
    }

    public static void operatorOutputToResultConnectionChanged(Result result) {
        if (result.getOperatorOutput() == null) {
            result.setValue(0);
        } else {
            updateOperatorResult(result.getOperatorOutput().getOperator());
        }
    }

    public static void updateOperatorResult(Operator operator) {
        Result result = operator.getOutput().getResult();
        if (result != null) {
            if (CycleDetectionHelper.cycleDetected(result)) {
                result.setValue(0);
            } else {
                Number in1 = operator.getInputs().get(0).getNumber();
                Number in2 = operator.getInputs().get(1).getNumber();
                double _in1 = in1 != null ? in1.getValue() : 0;
                double _in2 = in2 != null ? in2.getValue() : 0;
                result.setValue(operator instanceof PlusOperator ? _in1 + _in2 : _in1 - _in2);
            }
        }
    }
}
