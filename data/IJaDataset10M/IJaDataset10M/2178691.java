package org.sodeja.explicit2;

class IfDecide implements CompiledExpression {

    private final CompiledExpression consequent;

    private final CompiledExpression alternative;

    public IfDecide(CompiledExpression consequent, CompiledExpression alternative) {
        this.consequent = consequent;
        this.alternative = alternative;
    }

    @Override
    public void eval(Machine machine) {
        machine.cont.restore();
        machine.env.restore();
        machine.exp.restore();
        Boolean test = (Boolean) machine.val.getValue();
        if (test) {
            machine.exp.setValue(consequent);
            return;
        }
        machine.exp.setValue(alternative);
    }
}
