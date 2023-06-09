package ru.amse.baltijsky.javascheme.importer.sablecc.java15.node;

import ru.amse.baltijsky.javascheme.importer.sablecc.java15.analysis.Analysis;

@SuppressWarnings("nls")
public final class ASimpleName extends PSimpleName {

    private TIdentifier _identifier_;

    public ASimpleName() {
    }

    public ASimpleName(@SuppressWarnings("hiding") TIdentifier _identifier_) {
        setIdentifier(_identifier_);
    }

    @Override
    public Object clone() {
        return new ASimpleName(cloneNode(this._identifier_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseASimpleName(this);
    }

    public TIdentifier getIdentifier() {
        return this._identifier_;
    }

    public void setIdentifier(TIdentifier node) {
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
        return "" + toString(this._identifier_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._identifier_ == child) {
            this._identifier_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._identifier_ == oldChild) {
            setIdentifier((TIdentifier) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
