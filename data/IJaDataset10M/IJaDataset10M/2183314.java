package com.hp.hpl.jena.rdql.parser;

import java.io.PrintWriter;
import com.hp.hpl.jena.graph.query.IndexValues;
import com.hp.hpl.jena.graph.query.Expression;
import com.hp.hpl.jena.rdql.*;

public class Q_UnaryPlus extends ExprNode implements ExprNumeric {

    Expr expr;

    private String printName = "unaryplus";

    private String opSymbol = "+";

    Q_UnaryPlus(int id) {
        super(id);
    }

    Q_UnaryPlus(RDQLParser p, int id) {
        super(p, id);
    }

    public NodeValue eval(Query q, IndexValues env) {
        NodeValue r = expr.eval(q, env);
        if (!r.isNumber()) throw new EvalTypeException("Q_UnaryPlus: Wanted a number: got " + expr);
        return r;
    }

    public void jjtClose() {
        int n = jjtGetNumChildren();
        if (n != 1) throw new QueryException("Q_UnaryPlus: Wrong number of children: " + n);
        expr = (Expr) jjtGetChild(0);
    }

    public boolean isApply() {
        return true;
    }

    public String getFun() {
        return super.constructURI(this.getClass().getName());
    }

    public int argCount() {
        return 1;
    }

    public Expression getArg(int i) {
        if (i == 0 && expr instanceof Expression) return (Expression) expr;
        return null;
    }

    public String asInfixString() {
        return QueryPrintUtils.asInfixString1(expr, printName, opSymbol);
    }

    public String asPrefixString() {
        return QueryPrintUtils.asPrefixString(expr, null, printName, opSymbol);
    }

    public void print(PrintWriter pw, int level) {
        QueryPrintUtils.print(pw, expr, null, printName, opSymbol, level);
    }

    public String toString() {
        return asInfixString();
    }
}
