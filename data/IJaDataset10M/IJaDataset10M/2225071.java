package org.eclipse.xtext.example.typesystem.fjtests.rules;

import org.eclipse.xtext.typesystem.runtime.RuntimeRule;
import org.eclipse.xtext.typesystem.runtime.RuleFailedException;
import org.eclipse.xtext.typesystem.runtime.TypingJudgmentEnvironment;
import org.eclipse.xtext.typesystem.runtime.Variable;

public class TNewNameTypeRule extends FJTestsTypeSystemRule {

    protected Variable<org.eclipse.xtext.example.fj.Class> var_e = new Variable<org.eclipse.xtext.example.fj.Class>(createEClassifierType(basicPackage.getClass_()));

    protected Variable<String> right_var;

    protected TypingJudgmentEnvironment env_G = new TypingJudgmentEnvironment();

    public TNewNameTypeRule() {
        this("TNewName", "|-", ":--:");
    }

    public TNewNameTypeRule(String ruleName, String typeJudgmentSymbol, String typeStatementRelation) {
        super(ruleName, typeJudgmentSymbol, typeStatementRelation);
    }

    @Override
    public Variable<org.eclipse.xtext.example.fj.Class> getLeft() {
        return var_e;
    }

    @Override
    public Variable<String> getRight() {
        if (right_var == null) right_var = new Variable<String>(createBasicType("String"), null);
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
        return new TNewNameTypeRule("TNewName", "|-", ":--:");
    }

    @Override
    public void applyImpl() throws RuleFailedException {
        if (var_e.getValue() == null) {
            var_e.setValue(factory.createClass());
        }
        var_e.getValue().setName(newName("Y"));
        getRight().setValue(newName("X"));
    }
}
