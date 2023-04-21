package com.doxological.doxquery.grammar;

import com.doxological.doxquery.types.*;
import com.doxological.doxquery.context.MutableStaticContext;
import com.doxological.doxquery.XQueryException;

public class XQParenthesizedExpr extends SimpleNode {

    public XQParenthesizedExpr(int id) {
        super(id);
    }

    public XQParenthesizedExpr() {
        super(XQueryGrammarTreeConstants.JJTPARENTHESIZEDEXPR);
    }

    public XQParenthesizedExpr(XQueryGrammar p, int id) {
        super(p, id);
    }

    public String toQuery() {
        return "()";
    }

    /** Accept the visitor. **/
    public Object accept(GrammarVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
