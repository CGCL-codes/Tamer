package com.google.gwt.dev.jjs.impl.gflow.cfg;

import com.google.gwt.dev.jjs.ast.JDoStatement;
import com.google.gwt.dev.jjs.ast.JExpression;

/**
 * Node corresponding to while statement.
 */
public class CfgDoNode extends CfgConditionalNode<JDoStatement> {

    public CfgDoNode(CfgNode<?> parent, JDoStatement node) {
        super(parent, node);
    }

    @Override
    public void accept(CfgVisitor visitor) {
        visitor.visitDoNode(this);
    }

    @Override
    public JExpression getCondition() {
        return getJNode().getTestExpr();
    }

    @Override
    protected CfgNode<?> cloneImpl() {
        return new CfgDoNode(getParent(), getJNode());
    }
}
