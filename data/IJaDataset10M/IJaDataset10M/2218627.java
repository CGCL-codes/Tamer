package org.zamia.verilog.node;

import org.zamia.verilog.analysis.*;

@SuppressWarnings("nls")
public final class AP4BinaryOperator extends PBinaryOperator {

    private TTPerc _tPerc_;

    public AP4BinaryOperator() {
    }

    public AP4BinaryOperator(@SuppressWarnings("hiding") TTPerc _tPerc_) {
        setTPerc(_tPerc_);
    }

    @Override
    public Object clone() {
        return new AP4BinaryOperator(cloneNode(this._tPerc_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAP4BinaryOperator(this);
    }

    public TTPerc getTPerc() {
        return this._tPerc_;
    }

    public void setTPerc(TTPerc node) {
        if (this._tPerc_ != null) {
            this._tPerc_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._tPerc_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._tPerc_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._tPerc_ == child) {
            this._tPerc_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._tPerc_ == oldChild) {
            setTPerc((TTPerc) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
