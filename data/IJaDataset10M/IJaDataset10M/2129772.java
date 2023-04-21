package org.joogie.boogie.expressions;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.joogie.boogie.BoogieProcedure;
import org.joogie.boogie.types.BoogieType;
import org.joogie.boogie.types.BoogieTypeFactory;

/**
 * @author schaef
 *
 */
public class InvokeExpression extends Expression {

    private LinkedList<Expression> arguments;

    private String qualifiedName;

    private BoogieType returnType;

    private BoogieProcedure invokedProcedure;

    public InvokeExpression(BoogieProcedure proc, LinkedList<Expression> args) {
        invokedProcedure = proc;
        qualifiedName = proc.getName();
        if (proc.getReturnVariable() != null) {
            returnType = proc.getReturnVariable().getType();
        } else {
            returnType = BoogieTypeFactory.getVoidType();
        }
        arguments = new LinkedList<Expression>();
        int offset = 0;
        if (!proc.isStatic()) {
            offset = 1;
            arguments.add(args.getFirst());
        }
        for (int i = 0; i < args.size() - offset; i++) {
            Expression exp = args.get(i + offset);
            BoogieType t = proc.lookupParameter(i).getType();
            arguments.add(OperatorFunctionFactory.castIfNecessary(exp, t));
        }
    }

    public BoogieProcedure getInvokedProcedure() {
        return this.invokedProcedure;
    }

    public Collection<Variable> getModifiedVars() {
        return this.invokedProcedure.modifiesGlobals;
    }

    public List<Expression> getArguments() {
        return this.arguments;
    }

    @Override
    public String toBoogie() {
        StringBuilder sb = new StringBuilder();
        sb.append(qualifiedName + "(");
        boolean first = true;
        for (Expression exp : arguments) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append("(" + exp.toBoogie() + ")");
        }
        sb.append(")");
        return sb.toString();
    }

    public BoogieType getType() {
        return returnType;
    }

    @Override
    public Expression clone() {
        LinkedList<Expression> cargs = new LinkedList<Expression>();
        for (Expression e : arguments) {
            cargs.add(e.clone());
        }
        return new InvokeExpression(this.invokedProcedure, cargs);
    }
}
