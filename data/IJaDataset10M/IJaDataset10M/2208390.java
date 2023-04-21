package org.sablecc.java.node;

import org.sablecc.java.analysis.*;

@SuppressWarnings("nls")
public final class ANumericPrimitiveType extends PPrimitiveType {

    private PNumericType _numericType_;

    public ANumericPrimitiveType() {
    }

    public ANumericPrimitiveType(@SuppressWarnings("hiding") PNumericType _numericType_) {
        setNumericType(_numericType_);
    }

    @Override
    public Object clone() {
        return new ANumericPrimitiveType(cloneNode(this._numericType_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseANumericPrimitiveType(this);
    }

    public PNumericType getNumericType() {
        return this._numericType_;
    }

    public void setNumericType(PNumericType node) {
        if (this._numericType_ != null) {
            this._numericType_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._numericType_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._numericType_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._numericType_ == child) {
            this._numericType_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._numericType_ == oldChild) {
            setNumericType((PNumericType) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
