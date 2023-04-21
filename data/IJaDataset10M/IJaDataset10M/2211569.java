package it.xtypes.example.fj.typesystem.fjfirst.rules;

import it.xtypes.runtime.RuntimeRule;
import it.xtypes.runtime.RuleFailedException;
import it.xtypes.runtime.TypingJudgmentEnvironment;
import it.xtypes.runtime.Variable;

public class SubTypeBasicTypeRule extends FJFirstTypeSystemRule {

    protected Variable<it.xtypes.example.fj.fj.BasicType> var_b1 = new Variable<it.xtypes.example.fj.fj.BasicType>(createEClassifierType(basicPackage.getBasicType()));

    protected Variable<it.xtypes.example.fj.fj.BasicType> var_b2 = new Variable<it.xtypes.example.fj.fj.BasicType>(createEClassifierType(basicPackage.getBasicType()));

    protected TypingJudgmentEnvironment env_G = new TypingJudgmentEnvironment();

    public SubTypeBasicTypeRule() {
        this("SubTypeBasic", "|-", "<:");
    }

    public SubTypeBasicTypeRule(String ruleName, String typeJudgmentSymbol, String typeStatementRelation) {
        super(ruleName, typeJudgmentSymbol, typeStatementRelation);
    }

    @Override
    public Variable<it.xtypes.example.fj.fj.BasicType> getLeft() {
        return var_b1;
    }

    @Override
    public Variable<it.xtypes.example.fj.fj.BasicType> getRight() {
        return var_b2;
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
        return new SubTypeBasicTypeRule("SubTypeBasic", "|-", "<:");
    }

    @Override
    public void applyImpl() throws RuleFailedException {
        equals(var_b1.getValue().getBasic(), var_b2.getValue().getBasic());
        if (var_b2.getValue() == null) {
            var_b2.setValue(factory.createBasicType());
        }
    }

    @Override
    protected String ruleFailureMessage() {
        return stringRep(var_b1.getValue()) + " is not a subtype of " + stringRep(var_b2.getValue());
    }
}
