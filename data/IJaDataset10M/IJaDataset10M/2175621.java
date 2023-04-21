package it.xtypes.example.fj.typesystem.fjsel.rules;

import it.xtypes.runtime.RuntimeRule;
import it.xtypes.runtime.RuleFailedException;
import it.xtypes.runtime.TypingJudgmentEnvironment;
import it.xtypes.runtime.Variable;

public class TNewTypeRule extends FJSelTypeSystemRule {

    protected Variable<java.util.List<it.xtypes.example.fj.fj.Field>> var_fields = new Variable<java.util.List<it.xtypes.example.fj.fj.Field>>(createCollectionType(createEClassifierType(basicPackage.getField())));

    protected Variable<it.xtypes.example.fj.fj.ClassType> var_CT = new Variable<it.xtypes.example.fj.fj.ClassType>(createEClassifierType(basicPackage.getClassType()));

    protected Variable<Integer> var_i = new Variable<Integer>(createBasicType("Integer"));

    protected Variable<it.xtypes.example.fj.fj.Type> var_argType = new Variable<it.xtypes.example.fj.fj.Type>(createEClassifierType(basicPackage.getType()));

    protected Variable<it.xtypes.example.fj.fj.Type> var_fieldType = new Variable<it.xtypes.example.fj.fj.Type>(createEClassifierType(basicPackage.getType()));

    protected Variable<it.xtypes.example.fj.fj.New> var_e = new Variable<it.xtypes.example.fj.fj.New>(createEClassifierType(basicPackage.getNew()));

    protected Variable<it.xtypes.example.fj.fj.Type> var_t = new Variable<it.xtypes.example.fj.fj.Type>(createEClassifierType(basicPackage.getType()));

    protected TypingJudgmentEnvironment env_G = new TypingJudgmentEnvironment();

    public TNewTypeRule() {
        this("TNew", "|-", ":");
    }

    public TNewTypeRule(String ruleName, String typeJudgmentSymbol, String typeStatementRelation) {
        super(ruleName, typeJudgmentSymbol, typeStatementRelation);
    }

    @Override
    public Variable<it.xtypes.example.fj.fj.New> getLeft() {
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
        return new TNewTypeRule("TNew", "|-", ":");
    }

    @Override
    public void applyImpl() throws RuleFailedException {
        var_fields = new Variable<java.util.List<it.xtypes.example.fj.fj.Field>>(createCollectionType(createEClassifierType(basicPackage.getField())), new java.util.LinkedList<it.xtypes.example.fj.fj.Field>());
        assignment(var_fields, getAll(var_e.getValue().getType().getClassref(), "fields", "extends", it.xtypes.example.fj.fj.Field.class));
        try {
            equals(length(var_e.getValue().getArgs()), length(var_fields));
        } catch (RuleFailedException e) {
            e.setMessage("argument number " + stringRep(length(var_e.getValue().getArgs())) + " is not equal to field number " + stringRep(length(var_fields)));
            throw e;
        }
        register("forall " + stringRep(length(var_e.getValue().getArgs())));
        try {
            for (var_i.setValue(0); var_i.getValue() < length(var_e.getValue().getArgs()); var_i.setValue(var_i.getValue() + 1)) {
                var_argType = new Variable<it.xtypes.example.fj.fj.Type>(createEClassifierType(basicPackage.getType()), factory.createType());
                var_fieldType = new Variable<it.xtypes.example.fj.fj.Type>(createEClassifierType(basicPackage.getType()), factory.createType());
                applyTypeRule(env_G, var_e.getValue().getArgs().get(var_i.getValue()), var_argType);
                applyTypeRule(env_G, var_fields.getValue().get(var_i.getValue()), var_fieldType);
                try {
                    applySubtypeRule(env_G, var_argType, var_fieldType);
                } catch (RuleFailedException e) {
                    e.setMessage("argument type (" + stringRep(var_argType.getValue()) + ") is not a subtype of field type (" + stringRep(var_fieldType.getValue()) + ")");
                    throw e;
                }
            }
        } catch (Throwable e) {
            registerFailure(e);
            throw new RuleFailedException("failure in checking arguments " + stringRep(var_e.getValue().getArgs()));
        }
        register("end " + "forall " + stringRep(length(var_e.getValue().getArgs())));
        var_CT = new Variable<it.xtypes.example.fj.fj.ClassType>(createEClassifierType(basicPackage.getClassType()), factory.createClassType());
        if (var_CT.getValue() == null) {
            var_CT.setValue(factory.createClassType());
        }
        var_CT.getValue().setClassref(var_e.getValue().getType().getClassref());
        assignment(var_t, var_CT);
        if (var_t.getValue() == null) {
            var_t.setValue(factory.createType());
        }
    }

    @Override
    protected String failMessage() {
        return "cannot type";
    }
}
