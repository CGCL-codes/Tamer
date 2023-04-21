package org.sablecc.java.node;

import org.sablecc.java.analysis.*;

@SuppressWarnings("nls")
public final class AConditionalExpressionAssignmentExpression extends PAssignmentExpression {

    private PConditionalExpression _conditionalExpression_;

    public AConditionalExpressionAssignmentExpression() {
    }

    public AConditionalExpressionAssignmentExpression(@SuppressWarnings("hiding") PConditionalExpression _conditionalExpression_) {
        setConditionalExpression(_conditionalExpression_);
    }

    @Override
    public Object clone() {
        return new AConditionalExpressionAssignmentExpression(cloneNode(this._conditionalExpression_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAConditionalExpressionAssignmentExpression(this);
    }

    public PConditionalExpression getConditionalExpression() {
        return this._conditionalExpression_;
    }

    public void setConditionalExpression(PConditionalExpression node) {
        if (this._conditionalExpression_ != null) {
            this._conditionalExpression_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._conditionalExpression_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._conditionalExpression_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._conditionalExpression_ == child) {
            this._conditionalExpression_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._conditionalExpression_ == oldChild) {
            setConditionalExpression((PConditionalExpression) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
