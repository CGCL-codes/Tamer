package org.sodeja.explicit2;

public class If implements CompiledExpression {

    private final CompiledExpression predicate;

    private final CompiledExpression consequent;

    private final CompiledExpression alternative;

    public If(CompiledExpression predicate, CompiledExpression consequent, CompiledExpression alternative) {
        this.predicate = predicate;
        this.consequent = consequent;
        this.alternative = alternative;
    }

    @Override
    public void eval(Machine machine) {
        machine.exp.save();
        machine.env.save();
        machine.cont.save();
        machine.cont.setValue(new IfDecide(consequent, alternative));
        machine.exp.setValue(predicate);
    }
}
