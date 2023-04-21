package de.intarsys.pdf.postscript;

import java.util.Stack;

public class Operator_round implements IOperator {

    public static Operator_round Instance;

    static {
        Instance = new Operator_round();
    }

    private Operator_round() {
        super();
    }

    public void execute(Stack stack) {
        Number element;
        double operand;
        long result;
        element = (Number) stack.peek();
        if (element instanceof Integer) {
            return;
        }
        operand = element.doubleValue();
        result = Math.round(operand);
        if (result == operand) {
            return;
        }
        stack.pop();
        stack.push(new Double(result));
    }
}
