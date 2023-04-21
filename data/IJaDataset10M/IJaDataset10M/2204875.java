package org.sablecc.java.node;

import org.sablecc.java.analysis.*;

@SuppressWarnings("nls")
public final class APostDecrementPostfixExpression extends PPostfixExpression {

    private PPostDecrementExpression _postDecrementExpression_;

    public APostDecrementPostfixExpression() {
    }

    public APostDecrementPostfixExpression(@SuppressWarnings("hiding") PPostDecrementExpression _postDecrementExpression_) {
        setPostDecrementExpression(_postDecrementExpression_);
    }

    @Override
    public Object clone() {
        return new APostDecrementPostfixExpression(cloneNode(this._postDecrementExpression_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAPostDecrementPostfixExpression(this);
    }

    public PPostDecrementExpression getPostDecrementExpression() {
        return this._postDecrementExpression_;
    }

    public void setPostDecrementExpression(PPostDecrementExpression node) {
        if (this._postDecrementExpression_ != null) {
            this._postDecrementExpression_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._postDecrementExpression_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._postDecrementExpression_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._postDecrementExpression_ == child) {
            this._postDecrementExpression_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._postDecrementExpression_ == oldChild) {
            setPostDecrementExpression((PPostDecrementExpression) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
