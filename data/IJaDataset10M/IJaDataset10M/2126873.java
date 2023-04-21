package org.zamia.verilog.node;

import org.zamia.verilog.analysis.*;

@SuppressWarnings("nls")
public final class AP0ModulePathPrimary extends PModulePathPrimary {

    private PNumber _number_;

    public AP0ModulePathPrimary() {
    }

    public AP0ModulePathPrimary(@SuppressWarnings("hiding") PNumber _number_) {
        setNumber(_number_);
    }

    @Override
    public Object clone() {
        return new AP0ModulePathPrimary(cloneNode(this._number_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAP0ModulePathPrimary(this);
    }

    public PNumber getNumber() {
        return this._number_;
    }

    public void setNumber(PNumber node) {
        if (this._number_ != null) {
            this._number_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._number_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._number_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._number_ == child) {
            this._number_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._number_ == oldChild) {
            setNumber((PNumber) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
