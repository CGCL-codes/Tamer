package org.zamia.verilog.node;

import org.zamia.verilog.analysis.*;

@SuppressWarnings("nls")
public final class AP1OutputVariableType extends POutputVariableType {

    private TKTime _kTime_;

    public AP1OutputVariableType() {
    }

    public AP1OutputVariableType(@SuppressWarnings("hiding") TKTime _kTime_) {
        setKTime(_kTime_);
    }

    @Override
    public Object clone() {
        return new AP1OutputVariableType(cloneNode(this._kTime_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAP1OutputVariableType(this);
    }

    public TKTime getKTime() {
        return this._kTime_;
    }

    public void setKTime(TKTime node) {
        if (this._kTime_ != null) {
            this._kTime_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._kTime_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._kTime_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._kTime_ == child) {
            this._kTime_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._kTime_ == oldChild) {
            setKTime((TKTime) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
