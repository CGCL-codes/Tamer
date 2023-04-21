package org.sablecc.java.node;

import org.sablecc.java.analysis.*;

@SuppressWarnings("nls")
public final class AOneTypeParameterList extends PTypeParameterList {

    private PTypeParameter _typeParameter_;

    public AOneTypeParameterList() {
    }

    public AOneTypeParameterList(@SuppressWarnings("hiding") PTypeParameter _typeParameter_) {
        setTypeParameter(_typeParameter_);
    }

    @Override
    public Object clone() {
        return new AOneTypeParameterList(cloneNode(this._typeParameter_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAOneTypeParameterList(this);
    }

    public PTypeParameter getTypeParameter() {
        return this._typeParameter_;
    }

    public void setTypeParameter(PTypeParameter node) {
        if (this._typeParameter_ != null) {
            this._typeParameter_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._typeParameter_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._typeParameter_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._typeParameter_ == child) {
            this._typeParameter_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._typeParameter_ == oldChild) {
            setTypeParameter((PTypeParameter) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
