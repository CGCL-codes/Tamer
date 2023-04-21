package org.eclipse.xtext.example.typesystem.fjtests.rules;

import org.eclipse.xtext.typesystem.runtime.RuntimeRule;
import org.eclipse.xtext.typesystem.runtime.RuleFailedException;
import org.eclipse.xtext.typesystem.runtime.TypingJudgmentEnvironment;
import org.eclipse.xtext.typesystem.runtime.Variable;

public class WithExplicitFailure2TypeRule extends FJTestsTypeSystemRule {

    protected Variable<org.eclipse.xtext.example.fj.Parameter> var_C = new Variable<org.eclipse.xtext.example.fj.Parameter>(createEClassifierType(basicPackage.getParameter()));

    protected Variable<String> var_s = new Variable<String>(createBasicType("String"));

    protected TypingJudgmentEnvironment env_G = new TypingJudgmentEnvironment();

    public WithExplicitFailure2TypeRule() {
        this("WithExplicitFailure2", "|-", "::--::");
    }

    public WithExplicitFailure2TypeRule(String ruleName, String typeJudgmentSymbol, String typeStatementRelation) {
        super(ruleName, typeJudgmentSymbol, typeStatementRelation);
    }

    @Override
    public Variable<org.eclipse.xtext.example.fj.Parameter> getLeft() {
        return var_C;
    }

    @Override
    public Variable<String> getRight() {
        return var_s;
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
        return new WithExplicitFailure2TypeRule("WithExplicitFailure2", "|-", "::--::");
    }

    @Override
    public void applyImpl() throws RuleFailedException {
        fail("should not get here");
        if (var_s.getValue() == null) {
            var_s.setValue("");
        }
    }
}
