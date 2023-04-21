package ru.amse.baltijsky.javascheme.importer.sablecc.java15.node;

import ru.amse.baltijsky.javascheme.importer.sablecc.java15.analysis.Analysis;

@SuppressWarnings("nls")
public final class AAdditionalBoundGt extends PAdditionalBoundGt {

    private TAnd _and_;

    private PReferenceTypeGt _referenceTypeGt_;

    public AAdditionalBoundGt() {
    }

    public AAdditionalBoundGt(@SuppressWarnings("hiding") TAnd _and_, @SuppressWarnings("hiding") PReferenceTypeGt _referenceTypeGt_) {
        setAnd(_and_);
        setReferenceTypeGt(_referenceTypeGt_);
    }

    @Override
    public Object clone() {
        return new AAdditionalBoundGt(cloneNode(this._and_), cloneNode(this._referenceTypeGt_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAAdditionalBoundGt(this);
    }

    public TAnd getAnd() {
        return this._and_;
    }

    public void setAnd(TAnd node) {
        if (this._and_ != null) {
            this._and_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._and_ = node;
    }

    public PReferenceTypeGt getReferenceTypeGt() {
        return this._referenceTypeGt_;
    }

    public void setReferenceTypeGt(PReferenceTypeGt node) {
        if (this._referenceTypeGt_ != null) {
            this._referenceTypeGt_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._referenceTypeGt_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._and_) + toString(this._referenceTypeGt_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._and_ == child) {
            this._and_ = null;
            return;
        }
        if (this._referenceTypeGt_ == child) {
            this._referenceTypeGt_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._and_ == oldChild) {
            setAnd((TAnd) newChild);
            return;
        }
        if (this._referenceTypeGt_ == oldChild) {
            setReferenceTypeGt((PReferenceTypeGt) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
