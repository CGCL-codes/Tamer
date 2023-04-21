package org.zamia.verilog.node;

import org.zamia.verilog.analysis.*;

@SuppressWarnings("nls")
public final class AP3FunctionRangeOrType extends PFunctionRangeOrType {

    private TKRealtime _kRealtime_;

    public AP3FunctionRangeOrType() {
    }

    public AP3FunctionRangeOrType(@SuppressWarnings("hiding") TKRealtime _kRealtime_) {
        setKRealtime(_kRealtime_);
    }

    @Override
    public Object clone() {
        return new AP3FunctionRangeOrType(cloneNode(this._kRealtime_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAP3FunctionRangeOrType(this);
    }

    public TKRealtime getKRealtime() {
        return this._kRealtime_;
    }

    public void setKRealtime(TKRealtime node) {
        if (this._kRealtime_ != null) {
            this._kRealtime_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._kRealtime_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._kRealtime_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._kRealtime_ == child) {
            this._kRealtime_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._kRealtime_ == oldChild) {
            setKRealtime((TKRealtime) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
