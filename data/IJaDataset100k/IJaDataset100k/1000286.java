package org.zamia.verilog.node;

import org.zamia.verilog.analysis.*;

@SuppressWarnings("nls")
public final class AP0DriveStrength extends PDriveStrength {

    private TTLparen _tLparen_;

    private PStrength0 _strength0_;

    private TTComma _tComma_;

    private PStrength1 _strength1_;

    private TTRparen _tRparen_;

    public AP0DriveStrength() {
    }

    public AP0DriveStrength(@SuppressWarnings("hiding") TTLparen _tLparen_, @SuppressWarnings("hiding") PStrength0 _strength0_, @SuppressWarnings("hiding") TTComma _tComma_, @SuppressWarnings("hiding") PStrength1 _strength1_, @SuppressWarnings("hiding") TTRparen _tRparen_) {
        setTLparen(_tLparen_);
        setStrength0(_strength0_);
        setTComma(_tComma_);
        setStrength1(_strength1_);
        setTRparen(_tRparen_);
    }

    @Override
    public Object clone() {
        return new AP0DriveStrength(cloneNode(this._tLparen_), cloneNode(this._strength0_), cloneNode(this._tComma_), cloneNode(this._strength1_), cloneNode(this._tRparen_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAP0DriveStrength(this);
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

    public PStrength0 getStrength0() {
        return this._strength0_;
    }

    public void setStrength0(PStrength0 node) {
        if (this._strength0_ != null) {
            this._strength0_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._strength0_ = node;
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

    public PStrength1 getStrength1() {
        return this._strength1_;
    }

    public void setStrength1(PStrength1 node) {
        if (this._strength1_ != null) {
            this._strength1_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._strength1_ = node;
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
        return "" + toString(this._tLparen_) + toString(this._strength0_) + toString(this._tComma_) + toString(this._strength1_) + toString(this._tRparen_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._tLparen_ == child) {
            this._tLparen_ = null;
            return;
        }
        if (this._strength0_ == child) {
            this._strength0_ = null;
            return;
        }
        if (this._tComma_ == child) {
            this._tComma_ = null;
            return;
        }
        if (this._strength1_ == child) {
            this._strength1_ = null;
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
        if (this._strength0_ == oldChild) {
            setStrength0((PStrength0) newChild);
            return;
        }
        if (this._tComma_ == oldChild) {
            setTComma((TTComma) newChild);
            return;
        }
        if (this._strength1_ == oldChild) {
            setStrength1((PStrength1) newChild);
            return;
        }
        if (this._tRparen_ == oldChild) {
            setTRparen((TTRparen) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
