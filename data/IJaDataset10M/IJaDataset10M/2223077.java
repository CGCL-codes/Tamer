package it.xtypes.example.fj.typesystem.fjsep.rules;

import it.xtypes.runtime.RuntimeRule;
import it.xtypes.runtime.RuleFailedException;
import it.xtypes.runtime.TypingJudgmentEnvironment;
import it.xtypes.runtime.Variable;

public class TSelectionTypeRule extends FJSepTypeSystemRule {

    protected Variable<it.xtypes.example.fj.fj.Selection> var_e = new Variable<it.xtypes.example.fj.fj.Selection>(createEClassifierType(basicPackage.getSelection()));

    protected Variable<it.xtypes.example.fj.fj.Type> var_t = new Variable<it.xtypes.example.fj.fj.Type>(createEClassifierType(basicPackage.getType()));

    protected TypingJudgmentEnvironment env_G = new TypingJudgmentEnvironment();

    public TSelectionTypeRule() {
        this("TSelection", "|-", ":");
    }

    public TSelectionTypeRule(String ruleName, String typeJudgmentSymbol, String typeStatementRelation) {
        super(ruleName, typeJudgmentSymbol, typeStatementRelation);
    }

    @Override
    public Variable<it.xtypes.example.fj.fj.Selection> getLeft() {
        return var_e;
    }

    @Override
    public Variable<it.xtypes.example.fj.fj.Type> getRight() {
        return var_t;
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
        return new TSelectionTypeRule("TSelection", "|-", ":");
    }

    @Override
    public void applyImpl() throws RuleFailedException {
        applyTypeRule(env_G, var_e.getValue().getMessage(), var_t);
        if (var_t.getValue() == null) {
            var_t.setValue(factory.createType());
        }
    }

    @Override
    protected String failMessage() {
        return "cannot type";
    }
}
