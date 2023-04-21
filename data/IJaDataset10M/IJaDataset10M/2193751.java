package edu.vub.at.objects.natives.grammar;

import edu.vub.at.eval.Evaluator;
import edu.vub.at.eval.InvocationStack;
import edu.vub.at.exceptions.InterpreterException;
import edu.vub.at.objects.ATClosure;
import edu.vub.at.objects.ATContext;
import edu.vub.at.objects.ATObject;
import edu.vub.at.objects.ATTable;
import edu.vub.at.objects.grammar.ATApplication;
import edu.vub.at.objects.grammar.ATExpression;
import edu.vub.at.objects.natives.NATTable;
import edu.vub.at.objects.natives.NATText;
import edu.vub.util.TempFieldGenerator;
import java.util.Set;

/**
 * The native implementation of an application AG element.
 * 
 * @author tvcutsem
 */
public final class AGApplication extends AGExpression implements ATApplication {

    private final ATExpression funExp_;

    private final ATTable arguments_;

    public AGApplication(ATExpression fun, ATTable arg) {
        funExp_ = fun;
        arguments_ = arg;
    }

    public ATExpression base_function() {
        return funExp_;
    }

    public ATTable base_arguments() {
        return arguments_;
    }

    /**
	 * To evaluate a function application, evaluate the receiver expression to a function, then evaluate the arguments
	 * to the function application eagerly and apply the function.
	 * 
	 * AGAPL(fun,arg).eval(ctx) = fun.eval(ctx).apply(map eval(ctx) over arg)
	 * 
	 * @return the return value of the applied function.
	 */
    public ATObject meta_eval(ATContext ctx) throws InterpreterException {
        if (funExp_.isSymbol()) {
            NATTable args = Evaluator.evaluateArguments(arguments_.asNativeTable(), ctx);
            ATObject result = null;
            InvocationStack stack = InvocationStack.getInvocationStack();
            try {
                stack.functionCalled(this, null, args);
                result = ctx.base_lexicalScope().impl_callAccessor(funExp_.asSymbol(), args);
            } finally {
                stack.funcallReturned(result);
            }
            return result;
        } else {
            ATClosure clo = funExp_.meta_eval(ctx).asClosure();
            NATTable args = Evaluator.evaluateArguments(arguments_.asNativeTable(), ctx);
            ATObject result = null;
            InvocationStack stack = InvocationStack.getInvocationStack();
            try {
                stack.functionCalled(this, clo, args);
                result = clo.base_apply(args);
            } finally {
                stack.funcallReturned(result);
            }
            return result;
        }
    }

    /**
	 * Quoting an application results in a new quoted application.
	 * 
	 * AGAPL(sel,arg).quote(ctx) = AGAPL(sel.quote(ctx), arg.quote(ctx))
	 */
    public ATObject meta_quote(ATContext ctx) throws InterpreterException {
        return new AGApplication(funExp_.meta_quote(ctx).asExpression(), arguments_.meta_quote(ctx).asTable());
    }

    public NATText meta_print() throws InterpreterException {
        return NATText.atValue(funExp_.meta_print().javaValue + Evaluator.printAsList(arguments_).javaValue);
    }

    public NATText impl_asUnquotedCode(TempFieldGenerator objectMap) throws InterpreterException {
        return NATText.atValue(funExp_.impl_asUnquotedCode(objectMap).javaValue + Evaluator.codeAsList(objectMap, arguments_).javaValue);
    }

    /**
	 * FV(fExp(args)) = FV(fExp) U FV(args)
	 */
    public Set impl_freeVariables() throws InterpreterException {
        Set fvFunExp = funExp_.impl_freeVariables();
        fvFunExp.addAll(arguments_.impl_freeVariables());
        return fvFunExp;
    }

    public Set impl_quotedFreeVariables() throws InterpreterException {
        Set fvFunExp = funExp_.impl_quotedFreeVariables();
        fvFunExp.addAll(arguments_.impl_quotedFreeVariables());
        return fvFunExp;
    }
}
