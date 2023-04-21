package org.zamia.verilog.node;

import org.zamia.verilog.analysis.*;

@SuppressWarnings("nls")
public final class AP1EnableGateInstances extends PEnableGateInstances {

    private PEnableGateInstance _enableGateInstance_;

    private TTComma _tComma_;

    private PEnableGateInstances _enableGateInstances_;

    public AP1EnableGateInstances() {
    }

    public AP1EnableGateInstances(@SuppressWarnings("hiding") PEnableGateInstance _enableGateInstance_, @SuppressWarnings("hiding") TTComma _tComma_, @SuppressWarnings("hiding") PEnableGateInstances _enableGateInstances_) {
        setEnableGateInstance(_enableGateInstance_);
        setTComma(_tComma_);
        setEnableGateInstances(_enableGateInstances_);
    }

    @Override
    public Object clone() {
        return new AP1EnableGateInstances(cloneNode(this._enableGateInstance_), cloneNode(this._tComma_), cloneNode(this._enableGateInstances_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAP1EnableGateInstances(this);
    }

    public PEnableGateInstance getEnableGateInstance() {
        return this._enableGateInstance_;
    }

    public void setEnableGateInstance(PEnableGateInstance node) {
        if (this._enableGateInstance_ != null) {
            this._enableGateInstance_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._enableGateInstance_ = node;
    }

    public TTComma getTComma() {
        return this._tComma_;
    }

    public void setTComma(TTComma node) {
        if (this._tComma_ != null) {
            this._tComma_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._tComma_ = node;
    }

    public PEnableGateInstances getEnableGateInstances() {
        return this._enableGateInstances_;
    }

    public void setEnableGateInstances(PEnableGateInstances node) {
        if (this._enableGateInstances_ != null) {
            this._enableGateInstances_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._enableGateInstances_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._enableGateInstance_) + toString(this._tComma_) + toString(this._enableGateInstances_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._enableGateInstance_ == child) {
            this._enableGateInstance_ = null;
            return;
        }
        if (this._tComma_ == child) {
            this._tComma_ = null;
            return;
        }
        if (this._enableGateInstances_ == child) {
            this._enableGateInstances_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._enableGateInstance_ == oldChild) {
            setEnableGateInstance((PEnableGateInstance) newChild);
            return;
        }
        if (this._tComma_ == oldChild) {
            setTComma((TTComma) newChild);
            return;
        }
        if (this._enableGateInstances_ == oldChild) {
            setEnableGateInstances((PEnableGateInstances) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
