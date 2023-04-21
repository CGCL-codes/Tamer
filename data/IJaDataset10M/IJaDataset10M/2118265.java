package it.xtypes.example.fj.typesystem.fj.rules;

import it.xtypes.runtime.RuntimeRule;
import it.xtypes.runtime.RuleFailedException;
import it.xtypes.runtime.TypingJudgmentEnvironment;
import it.xtypes.runtime.Variable;

public class TVariableTypeRule extends FJTypeSystemRule {

    protected Variable<it.xtypes.example.fj.fj.Variable> var_v = new Variable<it.xtypes.example.fj.fj.Variable>(createEClassifierType(basicPackage.getVariable()));

    protected Variable<it.xtypes.example.fj.fj.Type> right_var;

    protected TypingJudgmentEnvironment env_G = new TypingJudgmentEnvironment();

    public TVariableTypeRule() {
        this("TVariable", "|-", ":");
    }

    public TVariableTypeRule(String ruleName, String typeJudgmentSymbol, String typeStatementRelation) {
        super(ruleName, typeJudgmentSymbol, typeStatementRelation);
    }

    @Override
    public Variable<it.xtypes.example.fj.fj.Variable> getLeft() {
        return var_v;
    }

    @Override
    public Variable<it.xtypes.example.fj.fj.Type> getRight() {
        if (right_var == null) right_var = new Variable<it.xtypes.example.fj.fj.Type>(createEClassifierType(basicPackage.getType()), null);
        return right_var;
    }

    @Override
    public TypingJudgmentEnvironment getEnvironment() {
        return env_G;
    }

    @Override
    public void setEnvironment(TypingJudgmentEnvironment environment) {
        if (environment != null) env_G = environment;
    }

    @Override
    public RuntimeRule newInstance() {
        return new TVariableTypeRule("TVariable", "|-", ":");
    }

    @Override
    public void applyImpl() throws RuleFailedException {
        getRight().setValue(var_v.getValue().getParamref().getType());
    }

    @Override
    protected String failMessage() {
        return "cannot type";
    }
}
