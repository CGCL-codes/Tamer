package freemarker.core.ast;

import java.util.List;
import java.io.Writer;
import freemarker.core.Environment;
import freemarker.template.*;
import java.io.IOException;

/**
 * A unary operator that calls a TemplateMethodModel or function
 * specified in FTL.  It associates with the <tt>Identifier</tt> 
 * or <tt>Dot</tt> to its left.
 */
public class MethodCall extends Expression {

    private Expression target;

    private final ArgsList arguments;

    public MethodCall(Expression target, ArgsList args) {
        this.target = target;
        target.parent = this;
        this.arguments = args;
        args.parent = this;
    }

    public ArgsList getArgs() {
        return arguments;
    }

    public Expression getTarget() {
        return target;
    }

    TemplateModel _getAsTemplateModel(Environment env) throws TemplateException {
        TemplateModel targetModel = target.getAsTemplateModel(env);
        if (targetModel instanceof TemplateMethodModel) {
            TemplateMethodModel targetMethod = (TemplateMethodModel) targetModel;
            List argumentStrings = arguments.getParameterSequence(targetMethod, env);
            Object result = targetMethod.exec(argumentStrings);
            return env.getObjectWrapper().wrap(result);
        } else if (targetModel instanceof Macro) {
            Macro func = (Macro) targetModel;
            env.setLastReturnValue(null);
            if (!func.isFunction()) {
                throw new TemplateException("A macro cannot be called in an expression.", env);
            }
            Writer prevOut = env.getOut();
            try {
                env.setOut(Environment.NULL_WRITER);
                env.render(func, arguments, null, null);
            } catch (IOException ioe) {
                throw new InternalError("This should be impossible.");
            } finally {
                env.setOut(prevOut);
            }
            return env.getLastReturnValue();
        } else if (targetModel instanceof Curry.Operator) {
            return ((Curry.Operator) targetModel).curry(arguments, env);
        } else {
            throw invalidTypeException(targetModel, target, env, "method");
        }
    }

    TemplateModel getConstantValue() {
        return null;
    }

    boolean isLiteral() {
        return false;
    }

    Expression _deepClone(String name, Expression subst) {
        return new MethodCall(target.deepClone(name, subst), arguments.deepClone(name, subst));
    }
}
