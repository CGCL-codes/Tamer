package de.intarsys.pdf.postscript;

import java.util.Stack;

public class Operator_sub implements IOperator {

    public static Operator_sub Instance;

    static {
        Instance = new Operator_sub();
    }

    private Operator_sub() {
        super();
    }

    public void execute(Stack stack) {
        Number element1;
        Number element2;
        element2 = (Number) stack.pop();
        element1 = (Number) stack.pop();
        if (element1 instanceof Integer && element2 instanceof Integer) {
            stack.push(new Integer(element1.intValue() - element2.intValue()));
            return;
        }
        stack.push(new Double(element1.doubleValue() - element2.doubleValue()));
    }
}
