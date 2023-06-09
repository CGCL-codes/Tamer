package com.google.clearsilver.jsilver.syntax.node;

import com.google.clearsilver.jsilver.syntax.analysis.*;

@SuppressWarnings("nls")
public final class ANameCommand extends PCommand {

    private PPosition _position_;

    private PVariable _variable_;

    public ANameCommand() {
    }

    public ANameCommand(@SuppressWarnings("hiding") PPosition _position_, @SuppressWarnings("hiding") PVariable _variable_) {
        setPosition(_position_);
        setVariable(_variable_);
    }

    @Override
    public Object clone() {
        return new ANameCommand(cloneNode(this._position_), cloneNode(this._variable_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseANameCommand(this);
    }

    public PPosition getPosition() {
        return this._position_;
    }

    public void setPosition(PPosition node) {
        if (this._position_ != null) {
            this._position_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._position_ = node;
    }

    public PVariable getVariable() {
        return this._variable_;
    }

    public void setVariable(PVariable node) {
        if (this._variable_ != null) {
            this._variable_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._variable_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._position_) + toString(this._variable_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._position_ == child) {
            this._position_ = null;
            return;
        }
        if (this._variable_ == child) {
            this._variable_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._position_ == oldChild) {
            setPosition((PPosition) newChild);
            return;
        }
        if (this._variable_ == oldChild) {
            setVariable((PVariable) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
