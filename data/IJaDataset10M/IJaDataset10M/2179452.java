package it.enricod.jcontextfree.engine.sablecc.node;

import it.enricod.jcontextfree.engine.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class AXFloatingPointValueSingleValue extends PSingleValue {

    private TX _x_;

    private TMinus _minus_;

    private TFloatingPointLiteral _floatingPointLiteral_;

    public AXFloatingPointValueSingleValue() {
    }

    public AXFloatingPointValueSingleValue(@SuppressWarnings("hiding") TX _x_, @SuppressWarnings("hiding") TMinus _minus_, @SuppressWarnings("hiding") TFloatingPointLiteral _floatingPointLiteral_) {
        setX(_x_);
        setMinus(_minus_);
        setFloatingPointLiteral(_floatingPointLiteral_);
    }

    @Override
    public Object clone() {
        return new AXFloatingPointValueSingleValue(cloneNode(this._x_), cloneNode(this._minus_), cloneNode(this._floatingPointLiteral_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAXFloatingPointValueSingleValue(this);
    }

    public TX getX() {
        return this._x_;
    }

    public void setX(TX node) {
        if (this._x_ != null) {
            this._x_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._x_ = node;
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

    public TFloatingPointLiteral getFloatingPointLiteral() {
        return this._floatingPointLiteral_;
    }

    public void setFloatingPointLiteral(TFloatingPointLiteral node) {
        if (this._floatingPointLiteral_ != null) {
            this._floatingPointLiteral_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._floatingPointLiteral_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._x_) + toString(this._minus_) + toString(this._floatingPointLiteral_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._x_ == child) {
            this._x_ = null;
            return;
        }
        if (this._minus_ == child) {
            this._minus_ = null;
            return;
        }
        if (this._floatingPointLiteral_ == child) {
            this._floatingPointLiteral_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._x_ == oldChild) {
            setX((TX) newChild);
            return;
        }
        if (this._minus_ == oldChild) {
            setMinus((TMinus) newChild);
            return;
        }
        if (this._floatingPointLiteral_ == oldChild) {
            setFloatingPointLiteral((TFloatingPointLiteral) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
