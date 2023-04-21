package org.zamia.verilog.node;

import org.zamia.verilog.analysis.*;

@SuppressWarnings("nls")
public final class AP2FunctionRangeOrType extends PFunctionRangeOrType {

    private TKReal _kReal_;

    public AP2FunctionRangeOrType() {
    }

    public AP2FunctionRangeOrType(@SuppressWarnings("hiding") TKReal _kReal_) {
        setKReal(_kReal_);
    }

    @Override
    public Object clone() {
        return new AP2FunctionRangeOrType(cloneNode(this._kReal_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAP2FunctionRangeOrType(this);
    }

    public TKReal getKReal() {
        return this._kReal_;
    }

    public void setKReal(TKReal node) {
        if (this._kReal_ != null) {
            this._kReal_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._kReal_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._kReal_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._kReal_ == child) {
            this._kReal_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._kReal_ == oldChild) {
            setKReal((TKReal) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
