package com.hp.hpl.jena.rdql.parser;

import com.hp.hpl.jena.graph.query.Expression;
import com.hp.hpl.jena.graph.query.IndexValues;
import com.hp.hpl.jena.rdql.*;
import java.io.PrintWriter;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Q_StringMatch extends ExprNode implements Expr, ExprBoolean {

    Expr left;

    Expr right;

    Q_PatternLiteral regex = null;

    private String printName = "strMatch";

    private String opSymbol = "=~";

    Pattern pattern = null;

    Q_StringMatch(int id) {
        super(id);
    }

    Q_StringMatch(RDQLParser p, int id) {
        super(p, id);
    }

    public NodeValue eval(Query q, IndexValues env) {
        NodeValue x = left.eval(q, env);
        String xx = x.valueString();
        NodeValueSettable result = new WorkingVar();
        boolean b = pattern.matcher(xx).find();
        result.setBoolean(b);
        return result;
    }

    public void jjtClose() {
        int n = jjtGetNumChildren();
        if (n != 2) throw new QueryException("Q_StringMatch: Wrong number of children: " + n);
        left = (Expr) jjtGetChild(0);
        right = (Expr) jjtGetChild(1);
        if (!(right instanceof Q_PatternLiteral)) throw new EvalFailureException("Q_StringMatch: Pattern error");
        regex = (Q_PatternLiteral) right;
        try {
            pattern = Pattern.compile(regex.patternString, regex.mask);
        } catch (PatternSyntaxException pEx) {
            throw new EvalFailureException("Q_StringMatch: Pattern exception: " + pEx);
        }
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
