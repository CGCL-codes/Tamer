package net.sourceforge.processdash.data.compiler;

import net.sourceforge.processdash.data.SimpleData;

abstract class UnaryOperator implements Instruction {

    private String operatorString = null;

    UnaryOperator(String op) {
        operatorString = op;
    }

    public void execute(Stack stack, ExpressionContext context) throws ExecutionException {
        Object operand = null;
        try {
            operand = stack.pop();
        } catch (Exception e) {
            throw new ExecutionException("execution stack is empty");
        }
        try {
            stack.push(operate((SimpleData) operand));
        } catch (ClassCastException cce) {
            throw new ExecutionException("ClassCastException");
        }
    }

    protected abstract SimpleData operate(SimpleData operand);

    public String toString() {
        return operatorString;
    }
}
