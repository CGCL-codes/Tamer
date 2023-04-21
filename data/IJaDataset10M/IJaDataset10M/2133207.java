package it.xtypes.example.lambda.typesystem.lambda.rules;

import it.xtypes.runtime.RuntimeRule;
import it.xtypes.runtime.RuleFailedException;
import it.xtypes.runtime.TypingJudgmentEnvironment;
import it.xtypes.runtime.Variable;

public class UnifyArrowTypeRule extends LambdaTypeSystemRule {

    protected Variable<it.xtypes.example.lambda.lambda.ArrowType> var_a1 = new Variable<it.xtypes.example.lambda.lambda.ArrowType>(createEClassifierType(basicPackage.getArrowType()));

    protected Variable<it.xtypes.example.lambda.lambda.ArrowType> var_a2 = new Variable<it.xtypes.example.lambda.lambda.ArrowType>(createEClassifierType(basicPackage.getArrowType()));

    protected TypingJudgmentEnvironment env_G = new TypingJudgmentEnvironment();

    public UnifyArrowTypeRule() {
        this("UnifyArrow", "|-", "==");
    }

    public UnifyArrowTypeRule(String ruleName, String typeJudgmentSymbol, String typeStatementRelation) {
        super(ruleName, typeJudgmentSymbol, typeStatementRelation);
    }

    @Override
    public Variable<it.xtypes.example.lambda.lambda.ArrowType> getLeft() {
        return var_a1;
    }

    @Override
    public Variable<it.xtypes.example.lambda.lambda.ArrowType> getRight() {
        return var_a2;
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
        return new UnifyArrowTypeRule("UnifyArrow", "|-", "==");
    }

    @Override
    public void applyImpl() throws RuleFailedException {
        applyUnifyRule(env_G, var_a1.getValue().getLeft(), var_a2.getValue().getLeft());
        applyUnifyRule(env_G, var_a1.getValue().getRight(), var_a2.getValue().getRight());
        if (var_a2.getValue() == null) {
            var_a2.setValue(factory.createArrowType());
        }
    }
}
