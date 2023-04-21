package ru.amse.baltijsky.javascheme.importer.sablecc.java15.node;

import ru.amse.baltijsky.javascheme.importer.sablecc.java15.analysis.Analysis;

@SuppressWarnings("nls")
public final class AEqualityExpAndExp extends PAndExp {

    private PEqualityExp _equalityExp_;

    public AEqualityExpAndExp() {
    }

    public AEqualityExpAndExp(@SuppressWarnings("hiding") PEqualityExp _equalityExp_) {
        setEqualityExp(_equalityExp_);
    }

    @Override
    public Object clone() {
        return new AEqualityExpAndExp(cloneNode(this._equalityExp_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAEqualityExpAndExp(this);
    }

    public PEqualityExp getEqualityExp() {
        return this._equalityExp_;
    }

    public void setEqualityExp(PEqualityExp node) {
        if (this._equalityExp_ != null) {
            this._equalityExp_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._equalityExp_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._equalityExp_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._equalityExp_ == child) {
            this._equalityExp_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._equalityExp_ == oldChild) {
            setEqualityExp((PEqualityExp) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
