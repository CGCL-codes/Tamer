package ru.amse.baltijsky.javascheme.importer.sablecc.java15.node;

import ru.amse.baltijsky.javascheme.importer.sablecc.java15.analysis.Analysis;

@SuppressWarnings("nls")
public final class ABooleanPrimitiveType extends PPrimitiveType {

    private TBoolean _boolean_;

    public ABooleanPrimitiveType() {
    }

    public ABooleanPrimitiveType(@SuppressWarnings("hiding") TBoolean _boolean_) {
        setBoolean(_boolean_);
    }

    @Override
    public Object clone() {
        return new ABooleanPrimitiveType(cloneNode(this._boolean_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseABooleanPrimitiveType(this);
    }

    public TBoolean getBoolean() {
        return this._boolean_;
    }

    public void setBoolean(TBoolean node) {
        if (this._boolean_ != null) {
            this._boolean_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._boolean_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._boolean_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._boolean_ == child) {
            this._boolean_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._boolean_ == oldChild) {
            setBoolean((TBoolean) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
