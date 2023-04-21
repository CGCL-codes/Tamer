package org.eclipse.xtext.example.typesystem.fjfirst.rules;

import org.eclipse.xtext.typesystem.runtime.RuntimeRule;
import org.eclipse.xtext.typesystem.runtime.RuleFailedException;
import org.eclipse.xtext.typesystem.runtime.TypingJudgmentEnvironment;
import org.eclipse.xtext.typesystem.runtime.Variable;

public class SubType4TypeRule extends FJFirstTypeSystemRule {

    protected Variable<org.eclipse.xtext.example.fj.Type> var_argType = new Variable<org.eclipse.xtext.example.fj.Type>(createEClassifierType(basicPackage.getType()));

    protected Variable<org.eclipse.xtext.example.fj.Argument> var_a = new Variable<org.eclipse.xtext.example.fj.Argument>(createEClassifierType(basicPackage.getArgument()));

    protected Variable<org.eclipse.xtext.example.fj.Type> var_T = new Variable<org.eclipse.xtext.example.fj.Type>(createEClassifierType(basicPackage.getType()));

    protected TypingJudgmentEnvironment env_G = new TypingJudgmentEnvironment();

    public SubType4TypeRule() {
        this("SubType4", "|-", "<:");
    }

    public SubType4TypeRule(String ruleName, String typeJudgmentSymbol, String typeStatementRelation) {
        super(ruleName, typeJudgmentSymbol, typeStatementRelation);
    }

    @Override
    public Variable<org.eclipse.xtext.example.fj.Argument> getLeft() {
        return var_a;
    }

    @Override
    public Variable<org.eclipse.xtext.example.fj.Type> getRight() {
        return var_T;
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
        return new SubType4TypeRule("SubType4", "|-", "<:");
    }

    @Override
    public void applyImpl() throws RuleFailedException {
        var_argType = new Variable<org.eclipse.xtext.example.fj.Type>(createEClassifierType(basicPackage.getType()), factory.createType());
        applyTypeRule(env_G, var_a, var_argType);
        applySubtypeRule(env_G, var_argType, var_T);
        if (var_T.getValue() == null) {
            var_T.setValue(factory.createType());
        }
    }
}
