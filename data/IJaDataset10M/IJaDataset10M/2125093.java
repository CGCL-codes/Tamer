package org.zamia.verilog.node;

import org.zamia.verilog.analysis.*;

@SuppressWarnings("nls")
public final class AP2ChargeStrength extends PChargeStrength {

    private TTLparen _tLparen_;

    private TKLarge _kLarge_;

    private TTRparen _tRparen_;

    public AP2ChargeStrength() {
    }

    public AP2ChargeStrength(@SuppressWarnings("hiding") TTLparen _tLparen_, @SuppressWarnings("hiding") TKLarge _kLarge_, @SuppressWarnings("hiding") TTRparen _tRparen_) {
        setTLparen(_tLparen_);
        setKLarge(_kLarge_);
        setTRparen(_tRparen_);
    }

    @Override
    public Object clone() {
        return new AP2ChargeStrength(cloneNode(this._tLparen_), cloneNode(this._kLarge_), cloneNode(this._tRparen_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAP2ChargeStrength(this);
    }

    public TTLparen getTLparen() {
        return this._tLparen_;
    }

    public void setTLparen(TTLparen node) {
        if (this._tLparen_ != null) {
            this._tLparen_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._tLparen_ = node;
    }

    public TKLarge getKLarge() {
        return this._kLarge_;
    }

    public void setKLarge(TKLarge node) {
        if (this._kLarge_ != null) {
            this._kLarge_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._kLarge_ = node;
    }

    public TTRparen getTRparen() {
        return this._tRparen_;
    }

    public void setTRparen(TTRparen node) {
        if (this._tRparen_ != null) {
            this._tRparen_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._tRparen_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._tLparen_) + toString(this._kLarge_) + toString(this._tRparen_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._tLparen_ == child) {
            this._tLparen_ = null;
            return;
        }
        if (this._kLarge_ == child) {
            this._kLarge_ = null;
            return;
        }
        if (this._tRparen_ == child) {
            this._tRparen_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._tLparen_ == oldChild) {
            setTLparen((TTLparen) newChild);
            return;
        }
        if (this._kLarge_ == oldChild) {
            setKLarge((TKLarge) newChild);
            return;
        }
        if (this._tRparen_ == oldChild) {
            setTRparen((TTRparen) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
