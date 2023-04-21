package com.doxological.doxquery.grammar;

public class XQUnionExpr extends SimpleNode {

    public XQUnionExpr(int id) {
        super(id);
    }

    public XQUnionExpr(XQueryGrammar p, int id) {
        super(p, id);
    }

    public String toQuery() {
        String result = "(";
        boolean addspace = false;
        for (Node n : children_) {
            if (addspace) result += " | "; else addspace = true;
            result += n.toQuery();
        }
        return result + ")";
    }

    /** Accept the visitor. **/
    public Object accept(GrammarVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
