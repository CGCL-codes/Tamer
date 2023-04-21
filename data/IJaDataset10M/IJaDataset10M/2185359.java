package org.zamia.verilog.node;

import org.zamia.verilog.analysis.*;

@SuppressWarnings("nls")
public final class AP6UnaryOperator extends PUnaryOperator {

    private TTPipe _tPipe_;

    public AP6UnaryOperator() {
    }

    public AP6UnaryOperator(@SuppressWarnings("hiding") TTPipe _tPipe_) {
        setTPipe(_tPipe_);
    }

    @Override
    public Object clone() {
        return new AP6UnaryOperator(cloneNode(this._tPipe_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAP6UnaryOperator(this);
    }

    public TTPipe getTPipe() {
        return this._tPipe_;
    }

    public void setTPipe(TTPipe node) {
        if (this._tPipe_ != null) {
            this._tPipe_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._tPipe_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._tPipe_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._tPipe_ == child) {
            this._tPipe_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._tPipe_ == oldChild) {
            setTPipe((TTPipe) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
