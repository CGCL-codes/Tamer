package org.sablecc.java.node;

import org.sablecc.java.analysis.*;

@SuppressWarnings("nls")
public final class ACharacterLiteral extends PLiteral {

    private TCharacterLiteral _characterLiteral_;

    public ACharacterLiteral() {
    }

    public ACharacterLiteral(@SuppressWarnings("hiding") TCharacterLiteral _characterLiteral_) {
        setCharacterLiteral(_characterLiteral_);
    }

    @Override
    public Object clone() {
        return new ACharacterLiteral(cloneNode(this._characterLiteral_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseACharacterLiteral(this);
    }

    public TCharacterLiteral getCharacterLiteral() {
        return this._characterLiteral_;
    }

    public void setCharacterLiteral(TCharacterLiteral node) {
        if (this._characterLiteral_ != null) {
            this._characterLiteral_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._characterLiteral_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._characterLiteral_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._characterLiteral_ == child) {
            this._characterLiteral_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._characterLiteral_ == oldChild) {
            setCharacterLiteral((TCharacterLiteral) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
