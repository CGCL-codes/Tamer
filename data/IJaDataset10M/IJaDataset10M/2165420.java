package it.enricod.jcontextfree.engine.sablecc.node;

import it.enricod.jcontextfree.engine.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AYIntegerValueSingleValue extends PSingleValue {

    private TY _y_;

    private TMinus _minus_;

    private TIntegerLiteral _integerLiteral_;

    public AYIntegerValueSingleValue() {
    }

    public AYIntegerValueSingleValue(@SuppressWarnings("hiding") TY _y_, @SuppressWarnings("hiding") TMinus _minus_, @SuppressWarnings("hiding") TIntegerLiteral _integerLiteral_) {
        setY(_y_);
        setMinus(_minus_);
        setIntegerLiteral(_integerLiteral_);
    }

    @Override
    public Object clone() {
        return new AYIntegerValueSingleValue(cloneNode(this._y_), cloneNode(this._minus_), cloneNode(this._integerLiteral_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAYIntegerValueSingleValue(this);
    }

    public TY getY() {
        return this._y_;
    }

    public void setY(TY node) {
        if (this._y_ != null) {
            this._y_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._y_ = node;
    }

    public TMinus getMinus() {
        return this._minus_;
    }

    public void setMinus(TMinus node) {
        if (this._minus_ != null) {
            this._minus_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._minus_ = node;
    }

    public TIntegerLiteral getIntegerLiteral() {
        return this._integerLiteral_;
    }

    public void setIntegerLiteral(TIntegerLiteral node) {
        if (this._integerLiteral_ != null) {
            this._integerLiteral_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._integerLiteral_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._y_) + toString(this._minus_) + toString(this._integerLiteral_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._y_ == child) {
            this._y_ = null;
            return;
        }
        if (this._minus_ == child) {
            this._minus_ = null;
            return;
        }
        if (this._integerLiteral_ == child) {
            this._integerLiteral_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._y_ == oldChild) {
            setY((TY) newChild);
            return;
        }
        if (this._minus_ == oldChild) {
            setMinus((TMinus) newChild);
            return;
        }
        if (this._integerLiteral_ == oldChild) {
            setIntegerLiteral((TIntegerLiteral) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
