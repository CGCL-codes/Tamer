package it.xtypes.example.fj.typesystem.fjsep.rules;

import it.xtypes.runtime.RuntimeRule;
import it.xtypes.runtime.RuleFailedException;
import it.xtypes.runtime.TypingJudgmentEnvironment;
import it.xtypes.runtime.Variable;

public class TMethodCallTypeRule extends FJSepTypeSystemRule {

    protected Variable<it.xtypes.example.fj.fj.MethodCall> var_m = new Variable<it.xtypes.example.fj.fj.MethodCall>(createEClassifierType(basicPackage.getMethodCall()));

    protected Variable<it.xtypes.example.fj.fj.Type> right_var;

    protected TypingJudgmentEnvironment env_G = new TypingJudgmentEnvironment();

    public TMethodCallTypeRule() {
        this("TMethodCall", "|-", ":");
    }

    public TMethodCallTypeRule(String ruleName, String typeJudgmentSymbol, String typeStatementRelation) {
        super(ruleName, typeJudgmentSymbol, typeStatementRelation);
    }

    @Override
    public Variable<it.xtypes.example.fj.fj.MethodCall> getLeft() {
        return var_m;
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
        return new TMethodCallTypeRule("TMethodCall", "|-", ":");
    }

    @Override
    public void applyImpl() throws RuleFailedException {
        getRight().setValue(var_m.getValue().getName().getReturntype());
    }

    @Override
    protected String failMessage() {
        return "cannot type";
    }
}
