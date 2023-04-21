package it.xtypes.example.fj.typesystem.fjsel.rules;

import it.xtypes.runtime.RuntimeRule;
import it.xtypes.runtime.RuleFailedException;
import it.xtypes.runtime.TypingJudgmentEnvironment;
import it.xtypes.runtime.Variable;

public class TMethodOkTypeRule extends FJSelTypeSystemRule {

    protected Variable<it.xtypes.example.fj.fj.ClassType> var_C = new Variable<it.xtypes.example.fj.fj.ClassType>(createEClassifierType(basicPackage.getClassType()));

    protected Variable<it.xtypes.example.fj.fj.Type> var_BodyType = new Variable<it.xtypes.example.fj.fj.Type>(createEClassifierType(basicPackage.getType()));

    protected Variable<it.xtypes.example.fj.fj.Method> var_inhMethod = new Variable<it.xtypes.example.fj.fj.Method>(createEClassifierType(basicPackage.getMethod()));

    protected Variable<it.xtypes.example.fj.fj.Parameter> var_param = new Variable<it.xtypes.example.fj.fj.Parameter>(createEClassifierType(basicPackage.getParameter()));

    protected Variable<it.xtypes.example.fj.fj.Method> var_m = new Variable<it.xtypes.example.fj.fj.Method>(createEClassifierType(basicPackage.getMethod()));

    protected Variable<String> right_var;

    protected TypingJudgmentEnvironment env_G = new TypingJudgmentEnvironment();

    protected TypingJudgmentEnvironment env_Gprime = new TypingJudgmentEnvironment();

    public TMethodOkTypeRule() {
        this("TMethodOk", "|-", ":");
    }

    public TMethodOkTypeRule(String ruleName, String typeJudgmentSymbol, String typeStatementRelation) {
        super(ruleName, typeJudgmentSymbol, typeStatementRelation);
    }

    @Override
    public Variable<it.xtypes.example.fj.fj.Method> getLeft() {
        return var_m;
    }

    @Override
    public Variable<String> getRight() {
        if (right_var == null) right_var = new Variable<String>(createBasicType("String"), "OK");
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
        return new TMethodOkTypeRule("TMethodOk", "|-", ":");
    }

    @Override
    public void applyImpl() throws RuleFailedException {
        var_C = new Variable<it.xtypes.example.fj.fj.ClassType>(createEClassifierType(basicPackage.getClassType()), factory.createClassType());
        var_BodyType = new Variable<it.xtypes.example.fj.fj.Type>(createEClassifierType(basicPackage.getType()), factory.createType());
        if (var_C.getValue() == null) {
            var_C.setValue(factory.createClassType());
        }
        var_C.getValue().setClassref(castto(container(var_m.getValue()), it.xtypes.example.fj.fj.Class.class));
        register("foreach " + stringRep(getAll(var_C.getValue().getClassref().getExtends(), "methods", "extends", it.xtypes.example.fj.fj.Method.class)));
        try {
            for (it.xtypes.example.fj.fj.Method var_inhMethod_temp : (java.util.List<it.xtypes.example.fj.fj.Method>) getAll(var_C.getValue().getClassref().getExtends(), "methods", "extends", it.xtypes.example.fj.fj.Method.class)) {
                var_inhMethod.setValue(var_inhMethod_temp);
                boolean or_temp_1 = false;
                try {
                    notEquals(var_inhMethod.getValue().getName(), var_m.getValue().getName());
                    or_temp_1 = true;
                } catch (Throwable e) {
                    registerFailure(e);
                }
                if (!or_temp_1) {
                    try {
                        applyOverrideRule(env_G, var_m, var_inhMethod);
                    } catch (Throwable e) {
                        registerAndThrowFailure(e);
                    }
                }
            }
        } catch (Throwable e) {
            registerAndThrowFailure(e);
        }
        register("end " + "foreach " + stringRep(getAll(var_C.getValue().getClassref().getExtends(), "methods", "extends", it.xtypes.example.fj.fj.Method.class)));
        env_Gprime = new TypingJudgmentEnvironment(null);
        register("foreach " + stringRep(var_m.getValue().getParams()));
        try {
            for (it.xtypes.example.fj.fj.Parameter var_param_temp : var_m.getValue().getParams()) {
                var_param.setValue(var_param_temp);
                env_Gprime.increment(newRuntimeEnvironmentEntry(var_param.getValue().getName(), var_param.getValue().getType()));
            }
        } catch (Throwable e) {
            registerAndThrowFailure(e);
        }
        register("end " + "foreach " + stringRep(var_m.getValue().getParams()));
        applyTypeRule(union(env_Gprime, newRuntimeEnvironmentEntry("this", var_C.getValue())), var_m.getValue().getBody().getExpression(), var_BodyType);
        applySubtypeRule(env_G, var_BodyType, var_m.getValue().getReturntype());
    }

    @Override
    protected String failMessage() {
        return "cannot type";
    }
}
