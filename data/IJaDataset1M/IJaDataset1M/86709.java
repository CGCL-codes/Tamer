package com.hp.hpl.jena.rdql.parser;

import java.io.PrintWriter;
import com.hp.hpl.jena.graph.query.Expression;
import com.hp.hpl.jena.graph.query.IndexValues;
import com.hp.hpl.jena.rdql.*;

/** 
 * @author Automatically generated class: Operator: Add
 * @version $Id: Q_Add.java,v 1.10 2006/03/22 13:52:21 andy_seaborne Exp $
 */
public class Q_Add extends ExprNode implements Expr, ExprNumeric {

    Expr left;

    Expr right;

    private String printName = "add";

    private String opSymbol = "+";

    Q_Add(int id) {
        super(id);
    }

    Q_Add(RDQLParser p, int id) {
        super(p, id);
    }

    public NodeValue eval(Query q, IndexValues env) {
        NodeValue x = left.eval(q, env);
        NodeValue y = right.eval(q, env);
        if (!x.isNumber()) throw new EvalTypeException("Q_Add: Wanted a number: " + x);
        if (!y.isNumber()) throw new EvalTypeException("Q_Add: Wanted a number: " + y);
        NodeValueSettable result;
        if (x instanceof NodeValueSettable) result = (NodeValueSettable) x; else if (y instanceof NodeValueSettable) result = (NodeValueSettable) y; else result = new WorkingVar();
        if (x.isInt() && y.isInt()) result.setInt(x.getInt() + y.getInt()); else result.setDouble(x.getDouble() + y.getDouble());
        return result;
    }

    public void jjtClose() {
        int n = jjtGetNumChildren();
        if (n != 2) throw new QueryException("Q_Add: Wrong number of children: " + n);
        left = (Expr) jjtGetChild(0);
        right = (Expr) jjtGetChild(1);
    }

    public boolean isApply() {
        return true;
    }

    public String getFun() {
        return super.constructURI(this.getClass().getName());
    }

    public int argCount() {
        return 2;
    }

    public Expression getArg(int i) {
        if (i == 0 && left instanceof Expression) return (Expression) left;
        if (i == 1 && right instanceof Expression) return (Expression) right;
        return null;
    }

    public String asInfixString() {
        return QueryPrintUtils.asInfixString2(left, right, printName, opSymbol);
    }

    public String asPrefixString() {
        return QueryPrintUtils.asPrefixString(left, right, printName, opSymbol);
    }

    public void print(PrintWriter pw, int level) {
        QueryPrintUtils.print(pw, left, right, printName, opSymbol, level);
    }

    public String toString() {
        return asInfixString();
    }
}
