package ru.amse.baltijsky.javascheme.importer.sablecc.java15.node;

import ru.amse.baltijsky.javascheme.importer.sablecc.java15.analysis.Analysis;

@SuppressWarnings("nls")
public final class AXorExpOrExp extends POrExp {

    private PXorExp _xorExp_;

    public AXorExpOrExp() {
    }

    public AXorExpOrExp(@SuppressWarnings("hiding") PXorExp _xorExp_) {
        setXorExp(_xorExp_);
    }

    @Override
    public Object clone() {
        return new AXorExpOrExp(cloneNode(this._xorExp_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAXorExpOrExp(this);
    }

    public PXorExp getXorExp() {
        return this._xorExp_;
    }

    public void setXorExp(PXorExp node) {
        if (this._xorExp_ != null) {
            this._xorExp_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._xorExp_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._xorExp_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._xorExp_ == child) {
            this._xorExp_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._xorExp_ == oldChild) {
            setXorExp((PXorExp) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
