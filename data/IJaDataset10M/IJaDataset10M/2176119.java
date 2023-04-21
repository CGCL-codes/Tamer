package de.intarsys.pdf.postscript;

import java.util.Stack;

public class Operator_ne implements IOperator {

    public static Operator_ne Instance;

    static {
        Instance = new Operator_ne();
    }

    private Operator_ne() {
        super();
    }

    public void execute(Stack stack) {
        throw new UnsupportedOperationException();
    }
}
