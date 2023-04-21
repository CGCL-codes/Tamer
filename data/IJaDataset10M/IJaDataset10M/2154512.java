package ru.amse.baltijsky.javascheme.importer.sablecc.java15.node;

import ru.amse.baltijsky.javascheme.importer.sablecc.java15.analysis.Analysis;

@SuppressWarnings("nls")
public final class ANewArrayExpPrimary extends PPrimary {

    private PNewArrayExp _newArrayExp_;

    public ANewArrayExpPrimary() {
    }

    public ANewArrayExpPrimary(@SuppressWarnings("hiding") PNewArrayExp _newArrayExp_) {
        setNewArrayExp(_newArrayExp_);
    }

    @Override
    public Object clone() {
        return new ANewArrayExpPrimary(cloneNode(this._newArrayExp_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseANewArrayExpPrimary(this);
    }

    public PNewArrayExp getNewArrayExp() {
        return this._newArrayExp_;
    }

    public void setNewArrayExp(PNewArrayExp node) {
        if (this._newArrayExp_ != null) {
            this._newArrayExp_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._newArrayExp_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._newArrayExp_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._newArrayExp_ == child) {
            this._newArrayExp_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._newArrayExp_ == oldChild) {
            setNewArrayExp((PNewArrayExp) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
