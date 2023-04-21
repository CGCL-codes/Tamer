package com.doxological.doxquery.grammar;

public class XQExtensionExpr extends SimpleNode {

    public XQExtensionExpr(int id) {
        super(id);
    }

    public XQExtensionExpr(XQueryGrammar p, int id) {
        super(p, id);
    }

    public String toQuery() {
        String result = "";
        int i = 0;
        for (; i < children_.size(); ++i) {
            if (!(getChild(i) instanceof XQPragma)) break;
            result += getChild(i).toQuery();
        }
        result += "{";
        if (i < children_.size()) result += getChild(i).toQuery();
        result += "}";
        return result;
    }

    /** Accept the visitor. **/
    public Object accept(GrammarVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
