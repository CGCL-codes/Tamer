package org.zamia.verilog.node;

import org.zamia.verilog.analysis.*;

@SuppressWarnings("nls")
public final class ACmosCmosSwitchtype extends PCmosSwitchtype {

    private TKCmos _kCmos_;

    public ACmosCmosSwitchtype() {
    }

    public ACmosCmosSwitchtype(@SuppressWarnings("hiding") TKCmos _kCmos_) {
        setKCmos(_kCmos_);
    }

    @Override
    public Object clone() {
        return new ACmosCmosSwitchtype(cloneNode(this._kCmos_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseACmosCmosSwitchtype(this);
    }

    public TKCmos getKCmos() {
        return this._kCmos_;
    }

    public void setKCmos(TKCmos node) {
        if (this._kCmos_ != null) {
            this._kCmos_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._kCmos_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._kCmos_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._kCmos_ == child) {
            this._kCmos_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._kCmos_ == oldChild) {
            setKCmos((TKCmos) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
