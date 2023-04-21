package com.google.gwt.dev.jjs.ast;

import com.google.gwt.dev.jjs.SourceInfo;

/**
 * Java method parameter reference expression.
 */
public class JParameterRef extends JVariableRef {

    /**
   * The referenced parameter.
   */
    private final JParameter param;

    public JParameterRef(SourceInfo info, JParameter param) {
        super(info, param);
        this.param = param;
    }

    public JParameter getParameter() {
        return param;
    }

    public boolean hasSideEffects() {
        return false;
    }

    public void traverse(JVisitor visitor, Context ctx) {
        if (visitor.visit(this, ctx)) {
        }
        visitor.endVisit(this, ctx);
    }
}
