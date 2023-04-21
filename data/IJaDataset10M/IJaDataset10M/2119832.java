package edu.vub.at.objects.natives.grammar;

import edu.vub.at.eval.Evaluator;
import edu.vub.at.eval.PartialBinder;
import edu.vub.at.exceptions.InterpreterException;
import edu.vub.at.objects.ATContext;
import edu.vub.at.objects.ATObject;
import edu.vub.at.objects.ATTable;
import edu.vub.at.objects.grammar.ATDefinition;
import edu.vub.at.objects.grammar.ATExpression;
import edu.vub.at.objects.grammar.ATMultiDefinition;
import edu.vub.at.objects.natives.NATText;
import edu.vub.util.TempFieldGenerator;
import java.util.HashSet;
import java.util.Set;

/**
 * @author tvc
 *
 * The native implementation of a multiple definition AG element.
 */
public class AGMultiDefinition extends AGDefinition implements ATMultiDefinition {

    private final ATTable parameters_;

    private final ATExpression valueExp_;

    private final PartialBinder binderPartialFunction_;

    public AGMultiDefinition(ATTable par, ATExpression val) throws InterpreterException {
        parameters_ = par;
        valueExp_ = val;
        binderPartialFunction_ = PartialBinder.calculateResidual("multi-definition", par);
    }

    public ATTable base_parameters() {
        return parameters_;
    }

    public ATExpression base_valueExpression() {
        return valueExp_;
    }

    /**
	 * To evaluate a multiple definition, evaluate the right hand side to a table
	 * and bind the parameters on the left hand side to the 'arguments' of the right hand side,
	 * exactly as if they were bound during a function call.
	 * 
	 * AGMULTIDEF(par,val).eval(ctx) = bind(ctx.scope, par, val.eval(ctx))
	 * 
	 * @return the evaluated arguments table
	 */
    public ATObject meta_eval(ATContext ctx) throws InterpreterException {
        ATTable args = valueExp_.meta_eval(ctx).asTable();
        PartialBinder.defineParamsForArgs(binderPartialFunction_, ctx, args);
        return args;
    }

    /**
	 * AGMULTIDEF(par,val).quote(ctx) = AGMULTIDEF(par.quote(ctx), val.quote(ctx))
	 */
    public ATObject meta_quote(ATContext ctx) throws InterpreterException {
        return new AGMultiDefinition(parameters_.meta_quote(ctx).asTable(), valueExp_.meta_quote(ctx).asExpression());
    }

    public NATText meta_print() throws InterpreterException {
        return NATText.atValue("def " + parameters_.meta_print().javaValue + " := " + valueExp_.meta_print().javaValue);
    }

    public NATText impl_asUnquotedCode(TempFieldGenerator objectMap) throws InterpreterException {
        return NATText.atValue("def " + parameters_.impl_asUnquotedCode(objectMap).javaValue + " := " + valueExp_.impl_asUnquotedCode(objectMap).javaValue);
    }

    /**
	 * IV(def [v1, v2 := exp, @ rest] := exp) = { v1, v2, rest }
	 */
    public Set impl_introducedVariables() throws InterpreterException {
        Set introducedVariables = new HashSet();
        ATObject[] params = parameters_.asNativeTable().elements_;
        for (int i = 0; i < params.length; i++) {
            if (params[i].isSymbol()) {
                introducedVariables.add(params[i].asSymbol());
            } else if (params[i].isVariableAssignment()) {
                introducedVariables.add(params[i].asVariableAssignment().base_name());
            } else if (params[i].isSplice()) {
                introducedVariables.add(params[i].asSplice().base_expression().asSymbol());
            }
        }
        return introducedVariables;
    }

    /**
	 * FV(def [v1, v2 := exp1, @ rest] := exp2) = FV(exp1) U (FV(exp2) \ { f1,f2 })
	 */
    public Set impl_freeVariables() throws InterpreterException {
        Set fvValueExp = valueExp_.impl_freeVariables();
        Evaluator.processFreeVariables(fvValueExp, parameters_);
        return fvValueExp;
    }

    public Set impl_quotedFreeVariables() throws InterpreterException {
        Set qfv = parameters_.impl_quotedFreeVariables();
        qfv.addAll(valueExp_.impl_quotedFreeVariables());
        return qfv;
    }
}
