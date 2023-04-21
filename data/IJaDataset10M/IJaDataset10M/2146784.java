package org.eclipse.xtext.example.typesystem.fjtests.rules;

import org.eclipse.xtext.typesystem.runtime.RuntimeRule;
import org.eclipse.xtext.typesystem.runtime.RuleFailedException;
import org.eclipse.xtext.typesystem.runtime.TypingJudgmentEnvironment;
import org.eclipse.xtext.typesystem.runtime.Variable;

public class TTestExpectParameterTypeRule extends FJTestsTypeSystemRule {

    protected Variable<org.eclipse.xtext.example.fj.Field> var_p = new Variable<org.eclipse.xtext.example.fj.Field>(createEClassifierType(basicPackage.getField()));

    protected Variable<org.eclipse.xtext.example.fj.Parameter> var_p2 = new Variable<org.eclipse.xtext.example.fj.Parameter>(createEClassifierType(basicPackage.getParameter()));

    protected TypingJudgmentEnvironment env_G = new TypingJudgmentEnvironment();

    public TTestExpectParameterTypeRule() {
        this("TTestExpectParameter", "|-", "::");
    }

    public TTestExpectParameterTypeRule(String ruleName, String typeJudgmentSymbol, String typeStatementRelation) {
        super(ruleName, typeJudgmentSymbol, typeStatementRelation);
    }

    @Override
    public Variable<org.eclipse.xtext.example.fj.Field> getLeft() {
        return var_p;
    }

    @Override
    public Variable<org.eclipse.xtext.example.fj.Parameter> getRight() {
        return var_p2;
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
        return new TTestExpectParameterTypeRule("TTestExpectParameter", "|-", "::");
    }

    @Override
    public void applyImpl() throws RuleFailedException {
        applyType2Rule(env_G, var_p, var_p2);
        if (var_p2.getValue() == null) {
            var_p2.setValue(factory.createParameter());
        }
    }

    @Override
    protected String failMessage() {
        return "cannot type";
    }
}
