package org.tzi.use.uml.ocl.expr;

import org.tzi.use.uml.ocl.type.Type;

/** 
 * A Variable declaration associates a variable name with a type.
 *
 * @version     $ProjectVersion: 0.393 $
 * @author  Mark Richters
 */
public class VarInitializer {

    protected VarDecl fVarDecl;

    protected Expression fInitExpr;

    public VarInitializer(String v, Type t, Expression initExpr) throws ExpInvalidException {
        fVarDecl = new VarDecl(v, t);
        fInitExpr = initExpr;
        if (!initExpr.type().isSubtypeOf(t)) throw new ExpInvalidException("Type mismatch. Initialization expression has type `" + initExpr.type() + "', expected type `" + t + "'.");
    }

    public String name() {
        return fVarDecl.name();
    }

    public Type type() {
        return fVarDecl.type();
    }

    public Expression initExpr() {
        return fInitExpr;
    }

    public String toString() {
        return fVarDecl + " = " + fInitExpr;
    }
}
