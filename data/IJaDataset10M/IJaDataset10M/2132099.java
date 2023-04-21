package org.sablecc.java.node;

import org.sablecc.java.analysis.*;

@SuppressWarnings("nls")
public final class AExpressionPreDecrementExpression extends PPreDecrementExpression {

    private TMinusMinus _minusMinus_;

    private PUnaryExpression _unaryExpression_;

    public AExpressionPreDecrementExpression() {
    }

    public AExpressionPreDecrementExpression(@SuppressWarnings("hiding") TMinusMinus _minusMinus_, @SuppressWarnings("hiding") PUnaryExpression _unaryExpression_) {
        setMinusMinus(_minusMinus_);
        setUnaryExpression(_unaryExpression_);
    }

    @Override
    public Object clone() {
        return new AExpressionPreDecrementExpression(cloneNode(this._minusMinus_), cloneNode(this._unaryExpression_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAExpressionPreDecrementExpression(this);
    }

    public TMinusMinus getMinusMinus() {
        return this._minusMinus_;
    }

    public void setMinusMinus(TMinusMinus node) {
        if (this._minusMinus_ != null) {
            this._minusMinus_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._minusMinus_ = node;
    }

    public PUnaryExpression getUnaryExpression() {
        return this._unaryExpression_;
    }

    public void setUnaryExpression(PUnaryExpression node) {
        if (this._unaryExpression_ != null) {
            this._unaryExpression_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._unaryExpression_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._minusMinus_) + toString(this._unaryExpression_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._minusMinus_ == child) {
            this._minusMinus_ = null;
            return;
        }
        if (this._unaryExpression_ == child) {
            this._unaryExpression_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._minusMinus_ == oldChild) {
            setMinusMinus((TMinusMinus) newChild);
            return;
        }
        if (this._unaryExpression_ == oldChild) {
            setUnaryExpression((PUnaryExpression) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
