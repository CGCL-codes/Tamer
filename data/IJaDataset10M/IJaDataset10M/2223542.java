package comp.logo.node;

import comp.logo.analysis.*;

@SuppressWarnings("nls")
public final class APExp extends PPExp {

    private TExp _exp_;

    private PPValue _pValue_;

    public APExp() {
    }

    public APExp(@SuppressWarnings("hiding") TExp _exp_, @SuppressWarnings("hiding") PPValue _pValue_) {
        setExp(_exp_);
        setPValue(_pValue_);
    }

    @Override
    public Object clone() {
        return new APExp(cloneNode(this._exp_), cloneNode(this._pValue_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAPExp(this);
    }

    public TExp getExp() {
        return this._exp_;
    }

    public void setExp(TExp node) {
        if (this._exp_ != null) {
            this._exp_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._exp_ = node;
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
        return "" + toString(this._exp_) + toString(this._pValue_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._exp_ == child) {
            this._exp_ = null;
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
        if (this._exp_ == oldChild) {
            setExp((TExp) newChild);
            return;
        }
        if (this._pValue_ == oldChild) {
            setPValue((PPValue) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
