package org.zamia.verilog.node;

import org.zamia.verilog.analysis.*;

@SuppressWarnings("nls")
public final class AP0TaskPortItem extends PTaskPortItem {

    private TKInput _kInput_;

    private TKReg _kReg_;

    private TKSigned _kSigned_;

    private PRange _range_;

    private PIdentifier _identifier_;

    public AP0TaskPortItem() {
    }

    public AP0TaskPortItem(@SuppressWarnings("hiding") TKInput _kInput_, @SuppressWarnings("hiding") TKReg _kReg_, @SuppressWarnings("hiding") TKSigned _kSigned_, @SuppressWarnings("hiding") PRange _range_, @SuppressWarnings("hiding") PIdentifier _identifier_) {
        setKInput(_kInput_);
        setKReg(_kReg_);
        setKSigned(_kSigned_);
        setRange(_range_);
        setIdentifier(_identifier_);
    }

    @Override
    public Object clone() {
        return new AP0TaskPortItem(cloneNode(this._kInput_), cloneNode(this._kReg_), cloneNode(this._kSigned_), cloneNode(this._range_), cloneNode(this._identifier_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAP0TaskPortItem(this);
    }

    public TKInput getKInput() {
        return this._kInput_;
    }

    public void setKInput(TKInput node) {
        if (this._kInput_ != null) {
            this._kInput_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._kInput_ = node;
    }

    public TKReg getKReg() {
        return this._kReg_;
    }

    public void setKReg(TKReg node) {
        if (this._kReg_ != null) {
            this._kReg_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._kReg_ = node;
    }

    public TKSigned getKSigned() {
        return this._kSigned_;
    }

    public void setKSigned(TKSigned node) {
        if (this._kSigned_ != null) {
            this._kSigned_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._kSigned_ = node;
    }

    public PRange getRange() {
        return this._range_;
    }

    public void setRange(PRange node) {
        if (this._range_ != null) {
            this._range_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._range_ = node;
    }

    public PIdentifier getIdentifier() {
        return this._identifier_;
    }

    public void setIdentifier(PIdentifier node) {
        if (this._identifier_ != null) {
            this._identifier_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._identifier_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._kInput_) + toString(this._kReg_) + toString(this._kSigned_) + toString(this._range_) + toString(this._identifier_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._kInput_ == child) {
            this._kInput_ = null;
            return;
        }
        if (this._kReg_ == child) {
            this._kReg_ = null;
            return;
        }
        if (this._kSigned_ == child) {
            this._kSigned_ = null;
            return;
        }
        if (this._range_ == child) {
            this._range_ = null;
            return;
        }
        if (this._identifier_ == child) {
            this._identifier_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._kInput_ == oldChild) {
            setKInput((TKInput) newChild);
            return;
        }
        if (this._kReg_ == oldChild) {
            setKReg((TKReg) newChild);
            return;
        }
        if (this._kSigned_ == oldChild) {
            setKSigned((TKSigned) newChild);
            return;
        }
        if (this._range_ == oldChild) {
            setRange((PRange) newChild);
            return;
        }
        if (this._identifier_ == oldChild) {
            setIdentifier((PIdentifier) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
