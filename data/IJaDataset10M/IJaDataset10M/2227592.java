package org.tzi.use.uml.ocl.expr;

import org.tzi.use.uml.ocl.type.Type;
import org.tzi.use.uml.ocl.value.Value;

/**
 * OCL Let-expression.
 *
 * @version     $ProjectVersion: 0.393 $
 * @author  Mark Richters
 */
public final class ExpLet extends Expression {

    private String fVarname;

    private Type fVarType;

    private Expression fVarExpr;

    private Expression fInExpr;

    public ExpLet(String varname, Type varType, Expression varExpr, Expression inExpr) throws ExpInvalidException {
        super(inExpr.type());
        fVarname = varname;
        fVarType = varType;
        fVarExpr = varExpr;
        fInExpr = inExpr;
        if (!fVarExpr.type().isSubtypeOf(fVarType)) throw new ExpInvalidException("Type of variable expression `" + fVarExpr.type() + "' does not match declared type `" + fVarType + "'.");
    }

    /**
     * Evaluates expression and returns result value.
     */
    public Value eval(EvalContext ctx) {
        ctx.enter(this);
        Value res = null;
        Value varValue = fVarExpr.eval(ctx);
        ctx.pushVarBinding(fVarname, varValue);
        res = fInExpr.eval(ctx);
        ctx.popVarBinding();
        ctx.exit(this, res);
        return res;
    }

    public String toString() {
        return "let " + fVarname + " : " + fVarType + " = " + fVarExpr + " in " + fInExpr;
    }

    public String getVarname() {
        return fVarname;
    }

    public Type getVarType() {
        return fVarType;
    }

    public Expression getInExpression() {
        return fInExpr;
    }

    public Expression getVarExpression() {
        return fVarExpr;
    }
}
