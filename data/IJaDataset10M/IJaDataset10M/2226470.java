package org.zamia.verilog.node;

import org.zamia.verilog.analysis.*;

@SuppressWarnings("nls")
public final class ATimingCheckCondition extends PTimingCheckCondition {

    private PExpression _expression_;

    public ATimingCheckCondition() {
    }

    public ATimingCheckCondition(@SuppressWarnings("hiding") PExpression _expression_) {
        setExpression(_expression_);
    }

    @Override
    public Object clone() {
        return new ATimingCheckCondition(cloneNode(this._expression_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseATimingCheckCondition(this);
    }

    public PExpression getExpression() {
        return this._expression_;
    }

    public void setExpression(PExpression node) {
        if (this._expression_ != null) {
            this._expression_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._expression_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._expression_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._expression_ == child) {
            this._expression_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._expression_ == oldChild) {
            setExpression((PExpression) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
