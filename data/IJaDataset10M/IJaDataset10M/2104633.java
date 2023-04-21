package org.zamia.verilog.node;

import org.zamia.verilog.analysis.*;

@SuppressWarnings("nls")
public final class AP0TfInoutDeclaration extends PTfInoutDeclaration {

    private TKInout _kInout_;

    private TKReg _kReg_;

    private TKSigned _kSigned_;

    private PRange _range_;

    private PListOfPortIdentifiers _listOfPortIdentifiers_;

    public AP0TfInoutDeclaration() {
    }

    public AP0TfInoutDeclaration(@SuppressWarnings("hiding") TKInout _kInout_, @SuppressWarnings("hiding") TKReg _kReg_, @SuppressWarnings("hiding") TKSigned _kSigned_, @SuppressWarnings("hiding") PRange _range_, @SuppressWarnings("hiding") PListOfPortIdentifiers _listOfPortIdentifiers_) {
        setKInout(_kInout_);
        setKReg(_kReg_);
        setKSigned(_kSigned_);
        setRange(_range_);
        setListOfPortIdentifiers(_listOfPortIdentifiers_);
    }

    @Override
    public Object clone() {
        return new AP0TfInoutDeclaration(cloneNode(this._kInout_), cloneNode(this._kReg_), cloneNode(this._kSigned_), cloneNode(this._range_), cloneNode(this._listOfPortIdentifiers_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAP0TfInoutDeclaration(this);
    }

    public TKInout getKInout() {
        return this._kInout_;
    }

    public void setKInout(TKInout node) {
        if (this._kInout_ != null) {
            this._kInout_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._kInout_ = node;
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

    public PListOfPortIdentifiers getListOfPortIdentifiers() {
        return this._listOfPortIdentifiers_;
    }

    public void setListOfPortIdentifiers(PListOfPortIdentifiers node) {
        if (this._listOfPortIdentifiers_ != null) {
            this._listOfPortIdentifiers_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._listOfPortIdentifiers_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._kInout_) + toString(this._kReg_) + toString(this._kSigned_) + toString(this._range_) + toString(this._listOfPortIdentifiers_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._kInout_ == child) {
            this._kInout_ = null;
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
        if (this._listOfPortIdentifiers_ == child) {
            this._listOfPortIdentifiers_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._kInout_ == oldChild) {
            setKInout((TKInout) newChild);
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
        if (this._listOfPortIdentifiers_ == oldChild) {
            setListOfPortIdentifiers((PListOfPortIdentifiers) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
