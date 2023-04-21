package org.eclipse.xtext.example.arithmetics.typesystem.arithmetics.rules;

import it.xtypes.runtime.RuntimeRule;
import it.xtypes.runtime.RuleFailedException;
import it.xtypes.runtime.TypingJudgmentEnvironment;
import it.xtypes.runtime.Variable;

public class FunctionCallTypeRule extends ArithmeticsTypeSystemRule {

    protected Variable<org.eclipse.xtext.example.arithmetics.arithmetics.Definition> var_function = new Variable<org.eclipse.xtext.example.arithmetics.arithmetics.Definition>(createEClassifierType(basicPackage.getDefinition()));

    protected Variable<org.eclipse.xtext.example.arithmetics.arithmetics.FunctionCall> var_e = new Variable<org.eclipse.xtext.example.arithmetics.arithmetics.FunctionCall>(createEClassifierType(basicPackage.getFunctionCall()));

    protected Variable<String> right_var;

    protected TypingJudgmentEnvironment env_G = new TypingJudgmentEnvironment();

    public FunctionCallTypeRule() {
        this("FunctionCall", "|-", ":");
    }

    public FunctionCallTypeRule(String ruleName, String typeJudgmentSymbol, String typeStatementRelation) {
        super(ruleName, typeJudgmentSymbol, typeStatementRelation);
    }

    @Override
    public Variable<org.eclipse.xtext.example.arithmetics.arithmetics.FunctionCall> getLeft() {
        return var_e;
    }

    @Override
    public Variable<String> getRight() {
        if (right_var == null) right_var = new Variable<String>(createBasicType("String"), "int");
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
        return new FunctionCallTypeRule("FunctionCall", "|-", ":");
    }

    @Override
    public void applyImpl() throws RuleFailedException {
        var_function = new Variable<org.eclipse.xtext.example.arithmetics.arithmetics.Definition>(createEClassifierType(basicPackage.getDefinition()), factory.createDefinition());
        assignment(var_function, castto(var_e.getValue().getFunc(), org.eclipse.xtext.example.arithmetics.arithmetics.Definition.class));
        equals(length(var_function.getValue().getArgs()), length(var_e.getValue().getArgs()));
    }
}
