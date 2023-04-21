package comp.logo.node;

import comp.logo.analysis.*;

@SuppressWarnings("nls")
public final class AMultPProduct extends PPProduct {

    private PPValue _pValue_;

    private PPMultList _pMultList_;

    public AMultPProduct() {
    }

    public AMultPProduct(@SuppressWarnings("hiding") PPValue _pValue_, @SuppressWarnings("hiding") PPMultList _pMultList_) {
        setPValue(_pValue_);
        setPMultList(_pMultList_);
    }

    @Override
    public Object clone() {
        return new AMultPProduct(cloneNode(this._pValue_), cloneNode(this._pMultList_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAMultPProduct(this);
    }

    public PPValue getPValue() {
        return this._pValue_;
    }

    public void setPValue(PPValue node) {
        if (this._pValue_ != null) {
            this._pValue_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._pValue_ = node;
    }

    public PPMultList getPMultList() {
        return this._pMultList_;
    }

    public void setPMultList(PPMultList node) {
        if (this._pMultList_ != null) {
            this._pMultList_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._pMultList_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._pValue_) + toString(this._pMultList_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._pValue_ == child) {
            this._pValue_ = null;
            return;
        }
        if (this._pMultList_ == child) {
            this._pMultList_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._pValue_ == oldChild) {
            setPValue((PPValue) newChild);
            return;
        }
        if (this._pMultList_ == oldChild) {
            setPMultList((PPMultList) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
