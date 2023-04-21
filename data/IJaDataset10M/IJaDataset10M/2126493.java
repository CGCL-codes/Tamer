package org.sablecc.java.node;

import org.sablecc.java.analysis.*;

@SuppressWarnings("nls")
public final class ASimpleInclusiveOrExpression extends PInclusiveOrExpression {

    private PExclusiveOrExpression _exclusiveOrExpression_;

    public ASimpleInclusiveOrExpression() {
    }

    public ASimpleInclusiveOrExpression(@SuppressWarnings("hiding") PExclusiveOrExpression _exclusiveOrExpression_) {
        setExclusiveOrExpression(_exclusiveOrExpression_);
    }

    @Override
    public Object clone() {
        return new ASimpleInclusiveOrExpression(cloneNode(this._exclusiveOrExpression_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseASimpleInclusiveOrExpression(this);
    }

    public PExclusiveOrExpression getExclusiveOrExpression() {
        return this._exclusiveOrExpression_;
    }

    public void setExclusiveOrExpression(PExclusiveOrExpression node) {
        if (this._exclusiveOrExpression_ != null) {
            this._exclusiveOrExpression_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._exclusiveOrExpression_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._exclusiveOrExpression_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._exclusiveOrExpression_ == child) {
            this._exclusiveOrExpression_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._exclusiveOrExpression_ == oldChild) {
            setExclusiveOrExpression((PExclusiveOrExpression) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
