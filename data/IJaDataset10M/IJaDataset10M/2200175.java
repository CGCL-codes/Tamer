package comp.logo.node;

import comp.logo.analysis.*;

@SuppressWarnings("nls")
public final class AValuePValueList extends PPValueList {

    private PPValue _pValue_;

    public AValuePValueList() {
    }

    public AValuePValueList(@SuppressWarnings("hiding") PPValue _pValue_) {
        setPValue(_pValue_);
    }

    @Override
    public Object clone() {
        return new AValuePValueList(cloneNode(this._pValue_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAValuePValueList(this);
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
        return "" + toString(this._pValue_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._pValue_ == child) {
            this._pValue_ = null;
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
        throw new RuntimeException("Not a child.");
    }
}
