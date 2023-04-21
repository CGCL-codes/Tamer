package ru.amse.baltijsky.javascheme.importer.sablecc.java15.node;

import ru.amse.baltijsky.javascheme.importer.sablecc.java15.analysis.Analysis;

@SuppressWarnings("nls")
public final class AReferenceTypeTypeArgumentSshr extends PTypeArgumentSshr {

    private PReferenceTypeSshr _referenceTypeSshr_;

    public AReferenceTypeTypeArgumentSshr() {
    }

    public AReferenceTypeTypeArgumentSshr(@SuppressWarnings("hiding") PReferenceTypeSshr _referenceTypeSshr_) {
        setReferenceTypeSshr(_referenceTypeSshr_);
    }

    @Override
    public Object clone() {
        return new AReferenceTypeTypeArgumentSshr(cloneNode(this._referenceTypeSshr_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAReferenceTypeTypeArgumentSshr(this);
    }

    public PReferenceTypeSshr getReferenceTypeSshr() {
        return this._referenceTypeSshr_;
    }

    public void setReferenceTypeSshr(PReferenceTypeSshr node) {
        if (this._referenceTypeSshr_ != null) {
            this._referenceTypeSshr_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._referenceTypeSshr_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._referenceTypeSshr_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._referenceTypeSshr_ == child) {
            this._referenceTypeSshr_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._referenceTypeSshr_ == oldChild) {
            setReferenceTypeSshr((PReferenceTypeSshr) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
