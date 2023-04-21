package org.eclipse.xtext.example.typesystem.fjsel.rules;

import org.eclipse.xtext.typesystem.runtime.RuntimeRule;
import org.eclipse.xtext.typesystem.runtime.RuleFailedException;
import org.eclipse.xtext.typesystem.runtime.TypingJudgmentEnvironment;
import org.eclipse.xtext.typesystem.runtime.Variable;

public class SubTypeTypeRule extends FJSelTypeSystemRule {

    protected Variable<org.eclipse.xtext.example.fj.Type> var_t1 = new Variable<org.eclipse.xtext.example.fj.Type>(createEClassifierType(basicPackage.getType()));

    protected Variable<org.eclipse.xtext.example.fj.Type> var_t2 = new Variable<org.eclipse.xtext.example.fj.Type>(createEClassifierType(basicPackage.getType()));

    protected TypingJudgmentEnvironment env_G = new TypingJudgmentEnvironment();

    public SubTypeTypeRule() {
        this("SubType", "|-", "<:");
    }

    public SubTypeTypeRule(String ruleName, String typeJudgmentSymbol, String typeStatementRelation) {
        super(ruleName, typeJudgmentSymbol, typeStatementRelation);
    }

    @Override
    public Variable<org.eclipse.xtext.example.fj.Type> getLeft() {
        return var_t1;
    }

    @Override
    public Variable<org.eclipse.xtext.example.fj.Type> getRight() {
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
        return new SubTypeTypeRule("SubType", "|-", "<:");
    }

    @Override
    public void applyImpl() throws RuleFailedException {
        boolean or_temp_1 = false;
        try {
            notEquals(var_t1.getValue().getBasic(), null);
            equals(var_t1.getValue().getBasic(), var_t2.getValue().getBasic());
            or_temp_1 = true;
        } catch (Throwable e) {
            registerFailure(e);
        }
        if (!or_temp_1) {
            try {
                notEquals(var_t1.getValue().getClassref(), null);
                applySubtypeRule(env_G, var_t1.getValue().getClassref(), var_t2.getValue().getClassref());
            } catch (Throwable e) {
                registerAndThrowFailure(e);
            }
        }
        if (var_t2.getValue() == null) {
            var_t2.setValue(factory.createType());
        }
    }

    @Override
    protected String ruleFailureMessage() {
        return stringRep(var_t1.getValue()) + " is not a subtype of " + stringRep(var_t2.getValue());
    }
}
