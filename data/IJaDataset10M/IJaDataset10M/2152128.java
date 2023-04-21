package comp.logo.node;

import comp.logo.analysis.*;

@SuppressWarnings("nls")
public final class APSqrt extends PPSqrt {

    private TSqrt _sqrt_;

    private PPValue _pValue_;

    public APSqrt() {
    }

    public APSqrt(@SuppressWarnings("hiding") TSqrt _sqrt_, @SuppressWarnings("hiding") PPValue _pValue_) {
        setSqrt(_sqrt_);
        setPValue(_pValue_);
    }

    @Override
    public Object clone() {
        return new APSqrt(cloneNode(this._sqrt_), cloneNode(this._pValue_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAPSqrt(this);
    }

    public TSqrt getSqrt() {
        return this._sqrt_;
    }

    public void setSqrt(TSqrt node) {
        if (this._sqrt_ != null) {
            this._sqrt_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._sqrt_ = node;
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

    @Override
    public String toString() {
        return "" + toString(this._sqrt_) + toString(this._pValue_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._sqrt_ == child) {
            this._sqrt_ = null;
            return;
        }
        if (this._pValue_ == child) {
            this._pValue_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._sqrt_ == oldChild) {
            setSqrt((TSqrt) newChild);
            return;
        }
        if (this._pValue_ == oldChild) {
            setPValue((PPValue) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
