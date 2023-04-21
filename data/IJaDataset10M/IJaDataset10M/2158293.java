package it.xtypes.example.lambda.typesystem.lambda.rules;

import it.xtypes.runtime.RuntimeRule;
import it.xtypes.runtime.RuleFailedException;
import it.xtypes.runtime.TypingJudgmentEnvironment;
import it.xtypes.runtime.Variable;

public class UnifyTypeTypeRule extends LambdaTypeSystemRule {

    protected Variable<it.xtypes.example.lambda.lambda.Type> var_t1 = new Variable<it.xtypes.example.lambda.lambda.Type>(createEClassifierType(basicPackage.getType()));

    protected Variable<it.xtypes.example.lambda.lambda.Type> var_t2 = new Variable<it.xtypes.example.lambda.lambda.Type>(createEClassifierType(basicPackage.getType()));

    protected TypingJudgmentEnvironment env_G = new TypingJudgmentEnvironment();

    public UnifyTypeTypeRule() {
        this("UnifyType", "|-", "==");
    }

    public UnifyTypeTypeRule(String ruleName, String typeJudgmentSymbol, String typeStatementRelation) {
        super(ruleName, typeJudgmentSymbol, typeStatementRelation);
    }

    @Override
    public Variable<it.xtypes.example.lambda.lambda.Type> getLeft() {
        return var_t1;
    }

    @Override
    public Variable<it.xtypes.example.lambda.lambda.Type> getRight() {
        return var_t2;
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
        return new UnifyTypeTypeRule("UnifyType", "|-", "==");
    }

    @Override
    public void applyImpl() throws RuleFailedException {
        equals(var_t1, var_t2);
        if (var_t2.getValue() == null) {
            var_t2.setValue(factory.createType());
        }
    }
}
