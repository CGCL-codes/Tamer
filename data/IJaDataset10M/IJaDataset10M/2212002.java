package org.sablecc.java.node;

import org.sablecc.java.analysis.*;

@SuppressWarnings("nls")
public final class APreDecrementExpressionUnaryExpression extends PUnaryExpression {

    private PPreDecrementExpression _preDecrementExpression_;

    public APreDecrementExpressionUnaryExpression() {
    }

    public APreDecrementExpressionUnaryExpression(@SuppressWarnings("hiding") PPreDecrementExpression _preDecrementExpression_) {
        setPreDecrementExpression(_preDecrementExpression_);
    }

    @Override
    public Object clone() {
        return new APreDecrementExpressionUnaryExpression(cloneNode(this._preDecrementExpression_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAPreDecrementExpressionUnaryExpression(this);
    }

    public PPreDecrementExpression getPreDecrementExpression() {
        return this._preDecrementExpression_;
    }

    public void setPreDecrementExpression(PPreDecrementExpression node) {
        if (this._preDecrementExpression_ != null) {
            this._preDecrementExpression_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._preDecrementExpression_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._preDecrementExpression_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._preDecrementExpression_ == child) {
            this._preDecrementExpression_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._preDecrementExpression_ == oldChild) {
            setPreDecrementExpression((PPreDecrementExpression) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
