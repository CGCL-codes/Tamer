package ch.unisi.inf.pfii.teammagenta.jcalc.core;

public abstract class UnaryOperation implements Expression {

    private final Expression operator;

    public UnaryOperation(final Expression operator) {
        this.operator = operator;
    }

    protected Expression getOperator() {
        return operator;
    }
}
